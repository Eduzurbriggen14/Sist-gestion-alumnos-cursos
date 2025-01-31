package GUI;

import DAO.AlumnoDAO;
import DAO.CursoDAO;
import DAO.InscripcionDAO;
import Entidades.*;
import Service.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotasPorCurso extends JFrame {
    private JComboBox<String> comboCursos;
    private JPanel panelPrincipal;
    private JButton btnVolver;

    private InscripcionDAO inscripcionDAO;
    private CursoDAO cursoDAO;
    private AlumnoDAO alumnoDAO;

    public NotasPorCurso() {
        this.inscripcionDAO = new InscripcionDAO();
        this.cursoDAO = new CursoDAO();
        this.alumnoDAO = new AlumnoDAO();
        // Configurar la ventana
        setTitle("Cursos y Alumnos");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        // ComboBox para seleccionar el curso
        comboCursos = new JComboBox<>();
        comboCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cursoSeleccionado = (String) comboCursos.getSelectedItem();
                if (cursoSeleccionado != null) {
                    try {
                        cargarAlumnosDelCurso(cursoSeleccionado);
                    } catch (ServiceException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        cargarCursos();

        JPanel panelCombo = new JPanel();
        panelCombo.add(new JLabel("Seleccione un curso:"));
        panelCombo.add(comboCursos);

        // Botón para volver al panel del profesor
        btnVolver = new JButton("Volver al Panel del Profesor");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfesorPanel pp = new ProfesorPanel();
                pp.setVisible(true);
                dispose();
            }
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVolver);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal); // Se envuelve el panelPrincipal en un JScrollPane
        scrollPane.setPreferredSize(new Dimension(750, 400));
        // Añadir el panel principal
        add(panelCombo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void cargarCursos() {
        CursoService cursoService = new CursoService();
        List<Curso> cursos = cursoService.recuperarTodosLosCursos();

        for (Curso curso : cursos) {
            comboCursos.addItem(curso.getNombreCurso());
        }
    }

    private void cargarAlumnosDelCurso(String nombreCurso) throws ServiceException {
        InscripcionService inscripcionService = new InscripcionService(inscripcionDAO, alumnoDAO, cursoDAO);
        List<Inscripcion> inscripcionesPorCurso = inscripcionService.obtenerInscripcionesPorCurso(nombreCurso);

        // Limpiar el panel principal antes de añadir nuevos datos
        panelPrincipal.removeAll();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.revalidate();
        panelPrincipal.repaint();

        // Crear el título del curso
        JLabel labelCurso = new JLabel("Reporte de Alumnos - Curso: " + nombreCurso);
        labelCurso.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(labelCurso);

        // Para mantener el conteo de las condiciones de los alumnos
        double cantAprobados = 0, cantDesaprobados = 0, cantRecuperatorio = 0, cantCursando = 0;
        int id_inscripcion = -1;
        // Para cada alumno inscrito en el curso
        for (Inscripcion insc : inscripcionesPorCurso) {
            String nombreUsuario = insc.getNombreUsuario();
            Inscripcion inscId = inscripcionDAO.obtener(nombreUsuario, nombreCurso);

            if (inscId == null) {
                System.out.println("No se encontró la inscripción para el usuario: " + nombreUsuario + " en el curso: " + nombreCurso);
                continue;
            }

            id_inscripcion = inscId.getId();
            CalificacionInscripcionService calificacionInscripcionService = new CalificacionInscripcionService();
            List<CalificacionInscripcion> calificaciones = calificacionInscripcionService.obtenerCalificacionesPorInscripcion(id_inscripcion);

            // Ordenar las calificaciones
            List<CalificacionInscripcion> notasOrdenadas = new ArrayList<>(calificaciones);
            notasOrdenadas.sort((c1, c2) -> {
                if (c1.getTipo().equals("nota1")) return -1;
                if (c1.getTipo().equals("nota2") && !c2.getTipo().equals("nota1")) return -1;
                if (c1.getTipo().equals("recuperatorio")) return 1;
                return 0;
            });

            // Variables para el cálculo de promedio
            double sumaNotas = 0;
            int contadorNotas = notasOrdenadas.size(); // Corregido: contar notas totales
            double nota1 = 0, nota2 = 0, recuperatorio = 0;

            // Primero asignar las notas
            for (CalificacionInscripcion calificacion : notasOrdenadas) {
                System.out.println(calificacion);
                String tipo = String.valueOf(calificacion.getTipo());
                double valorNota = calificacion.getValorNota();
                sumaNotas += valorNota;

                switch (tipo) {
                    case "nota1":
                        nota1 = valorNota;
                        break;
                    case "nota2":
                        nota2 = valorNota;
                        break;
                    case "recuperatorio":
                        recuperatorio = valorNota;
                        break;
                }
            }

            double promedio = (contadorNotas > 0) ? sumaNotas / contadorNotas : 0;
            String condicion;
            if (contadorNotas == 0) {
                condicion = Condicion.SIN_NOTA.name();
            } else if (contadorNotas < 2) {
                condicion = Condicion.CURSANDO.name();
            } else if (contadorNotas == 2) {
                if (nota1 < 6 && nota2 < 6) {
                    condicion = Condicion.DESAPROBADO.name();
                }else{
                    condicion = (nota1 >= 6.0 && nota2 >= 6.0) ?
                            Condicion.APROBADO.name() :
                            Condicion.RECUPERATORIO.name();
                }
            } else {
                if ((nota1 >= 6.0 || nota2 >= 6.0) && recuperatorio >= 6.0) {
                    condicion = Condicion.APROBADO.name();
                } else if (recuperatorio < 6.0) {
                    condicion = Condicion.DESAPROBADO.name();
                } else {
                    condicion = Condicion.RECUPERATORIO.name();
                }
            }

            // Actualizar el conteo por condición
            switch (condicion) {
                case ("APROBADO"):
                    cantAprobados++;
                    break;
                case ("DESAPROBADO"):
                    cantDesaprobados++;
                    break;
                case ("RECUPERATORIO"):
                    cantRecuperatorio++;
                    break;
                case ("CURSANDO"):
                    cantCursando++;
                    break;
                default:
                    break;
            }
            System.out.println("actualizando inscripcion " + id_inscripcion + condicion);
            inscripcionDAO.actualizarEstadoInscripcion(id_inscripcion, condicion);

            if (condicion.equals(Condicion.DESAPROBADO.name()) || condicion.equals(Condicion.APROBADO.name())) {
                System.out.println("antes de actualizar estado de inscripcion");
                inscripcionDAO.actualizarEstadoActivo(id_inscripcion, false);
            }

            // Crear una tabla para este alumno
            JPanel panelAlumno = new JPanel();
            panelAlumno.setLayout(new BorderLayout());

            // Título con nombre del alumno, promedio y condición
            String titulo = "Alumno: " + nombreUsuario + " | Promedio: " + promedio + " | Condición: " + condicion;
            JLabel labelTitulo = new JLabel(titulo);
            panelAlumno.add(labelTitulo, BorderLayout.NORTH);

            // Crear la tabla para este alumno
            JTable tablaAlumno = new JTable();
            DefaultTableModel modeloTabla = new DefaultTableModel(
                    new String[]{"Curso", "Calificación", "Tipo Nota", "Fecha"}, 0
            );
            tablaAlumno.setModel(modeloTabla);
            System.out.println(notasOrdenadas);

            // Agregar las calificaciones a la tabla
            for (CalificacionInscripcion calificacion : notasOrdenadas) {
                modeloTabla.addRow(new Object[]{
                        nombreCurso,
                        calificacion.getValorNota(),
                        calificacion.getTipo(),
                        calificacion.getFecha()
                });
            }

            // Agregar la tabla al panel
            JScrollPane scrollPane = new JScrollPane(tablaAlumno);
            panelAlumno.add(scrollPane, BorderLayout.CENTER);

            // Añadir el panel del alumno al panel principal
            panelPrincipal.add(panelAlumno);
        }

        // Generar el gráfico de estadísticas del curso
        double cantAlumnosPorCurso = cantAprobados + cantDesaprobados + cantRecuperatorio + cantCursando;

        if (cantAlumnosPorCurso == 0 && id_inscripcion <1  ) {
            JOptionPane.showMessageDialog(null, "No hay alumnos inscritos en este curso.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double porcentajeAprobados = (cantAprobados / cantAlumnosPorCurso) * 100;
        double porcentajeDesaprobados = (cantDesaprobados / cantAlumnosPorCurso) * 100;
        double porcentajeRecuperatorio = (cantRecuperatorio / cantAlumnosPorCurso) * 100;
        double porcentajeCursando = (cantCursando / cantAlumnosPorCurso) * 100;

        Map<String, Double> estadisticasCurso = new HashMap<>();
        estadisticasCurso.put("Porcentaje Aprobados", porcentajeAprobados);
        estadisticasCurso.put("Porcentaje Desaprobados", porcentajeDesaprobados);
        estadisticasCurso.put("Porcentaje Recuperatorio", porcentajeRecuperatorio);
        estadisticasCurso.put("Porcentaje Cursando", porcentajeCursando);

        ChartPanel graficoPastel = generarGraficoPastel(estadisticasCurso, nombreCurso);
        panelPrincipal.add(graficoPastel);

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private ChartPanel generarGraficoPastel(Map<String, Double> estadisticas, String nombreCurso) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Añadir solo condiciones con valores > 0
        estadisticas.forEach((condicion, porcentaje) -> {
            if (porcentaje > 0) {
                String etiquetaConPorcentaje = condicion + " (" + String.format("%.2f", porcentaje) + "%)";
                dataset.setValue(etiquetaConPorcentaje, porcentaje);
            }
        });

        JFreeChart chart = ChartFactory.createPieChart(
                "Estadísticas del Curso: " + nombreCurso,
                dataset,
                true,   // legend
                true,   // tooltips
                false   // URLs
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        return chartPanel;
    }
}
