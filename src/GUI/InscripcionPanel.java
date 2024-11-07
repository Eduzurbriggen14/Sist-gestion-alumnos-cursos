package GUI;

import DAO.AlumnoDAO;
import DAO.CursoDAO;
import DAO.DAOException;
import DAO.InscripcionDAO;
import Entidades.Alumno;
import Entidades.Curso;
import Entidades.Inscripcion;
import Service.InscripcionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InscripcionPanel extends JFrame {

    private static InscripcionService inscripcionService;
    private InscripcionDAO inscripcionDAO;
    private AlumnoDAO alumnoDAO;
    private CursoDAO cursoDAO;

    public InscripcionPanel() throws DAOException {
        // Inicializar los DAOs
        inscripcionDAO = new InscripcionDAO();  // Instancia de InscripcionDAO
        alumnoDAO = new AlumnoDAO();  // Instancia de AlumnoDAO
        cursoDAO = new CursoDAO();    // Instancia de CursoDAO

        // Crear el servicio de inscripción con los DAOs inicializados
        inscripcionService = new InscripcionService(inscripcionDAO, alumnoDAO, cursoDAO);

        // Configuración de la ventana principal
        setTitle("Sistema de Inscripciones");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Crear el panel principal y añadirlo al JFrame
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Añadir los botones y campos de texto a la ventana
        panel.add(crearInscripcionPanel());
        panel.add(crearInscripcionesPorAlumnoPanel());
        panel.add(crearInscripcionesPorCursoPanel());
        panel.add(crearGenerarReportePanel());
        panel.add(crearVolverPanel()); // Añadir el botón Volver

        add(panel);
    }

    private JPanel crearInscripcionPanel() throws DAOException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        // Recuperar los alumnos y cursos desde la base de datos
        List<Alumno> alumnos = alumnoDAO.recuperarTodos();
        List<Curso> cursos = cursoDAO.recuperarTodos();

        // Crear los JComboBox para seleccionar alumno y curso
        JComboBox<Alumno> comboAlumnos = new JComboBox<>(alumnos.toArray(new Alumno[0]));
        JComboBox<Curso> comboCursos = new JComboBox<>(cursos.toArray(new Curso[0]));

        // Crear el campo para la calificación final
        JLabel labelCalificacion = new JLabel("Calificación final:");
        JTextField textCalificacion = new JTextField("0", 5);

        JButton botonInscribir = new JButton("Inscribir");

        botonInscribir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alumno alumnoSeleccionado = (Alumno) comboAlumnos.getSelectedItem();
                Curso cursoSeleccionado = (Curso) comboCursos.getSelectedItem();
                double calificacion = Double.parseDouble(textCalificacion.getText());

                System.out.println("Alumno seleccionado: " + alumnoSeleccionado);  // Debería mostrar el nombre del alumno
                System.out.println("Curso seleccionado: " + cursoSeleccionado);    // Debería mostrar el nombre del curso

                if (alumnoSeleccionado == null || cursoSeleccionado == null) {
                    JOptionPane.showMessageDialog(panel, "Debe seleccionar un alumno y un curso.");
                    return;
                }

                boolean exito = false;

                try {
                    exito = inscripcionService.inscribirAlumnoEnCurso(alumnoSeleccionado.getNombreUsuario(), cursoSeleccionado.getNombreCurso(), calificacion);
                } catch (DAOException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
                if (exito) {
                    JOptionPane.showMessageDialog(panel, "Alumno inscrito exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(panel, "No se pudo inscribir al alumno.");
                }
            }
        });

        // Añadir los componentes al panel
        panel.add(new JLabel("Alumno:"));
        panel.add(comboAlumnos);
        panel.add(new JLabel("Curso:"));
        panel.add(comboCursos);
        panel.add(labelCalificacion);
        panel.add(textCalificacion);
        panel.add(botonInscribir);

        return panel;
    }

    private static JPanel crearInscripcionesPorAlumnoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel labelUsuario = new JLabel("Nombre de usuario:");
        JTextField textUsuario = new JTextField(20);
        JButton botonVer = new JButton("Ver inscripciones");

        botonVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = textUsuario.getText();
                List<Inscripcion> inscripciones = inscripcionService.obtenerInscripcionesPorAlumno(nombreUsuario);

                if (inscripciones.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "No hay inscripciones para este alumno.");
                } else {
                    StringBuilder report = new StringBuilder("Inscripciones:\n");
                    for (Inscripcion inscripcion : inscripciones) {
                        report.append("Curso: ").append(inscripcion.getNombreCurso())
                                .append(" - Calificación Final: ").append(inscripcion.getCalificacionFinal())
                                .append("\n");
                    }
                    JOptionPane.showMessageDialog(panel, report.toString());
                }
            }
        });

        panel.add(labelUsuario);
        panel.add(textUsuario);
        panel.add(botonVer);

        return panel;
    }

    private static JPanel crearInscripcionesPorCursoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel labelCurso = new JLabel("Nombre del curso:");
        JTextField textCurso = new JTextField(20);
        JButton botonVer = new JButton("Ver inscripciones");

        botonVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreCurso = textCurso.getText();
                List<Inscripcion> inscripciones = inscripcionService.obtenerInscripcionesPorCurso(nombreCurso);

                if (inscripciones.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "No hay inscripciones para este curso.");
                } else {
                    StringBuilder report = new StringBuilder("Inscripciones:\n");
                    for (Inscripcion inscripcion : inscripciones) {
                        report.append("Alumno: ").append(inscripcion.getNombreUsuario())
                                .append(" - Calificación Final: ").append(inscripcion.getCalificacionFinal())
                                .append("\n");
                    }
                    JOptionPane.showMessageDialog(panel, report.toString());
                }
            }
        });

        panel.add(labelCurso);
        panel.add(textCurso);
        panel.add(botonVer);

        return panel;
    }

    private static JPanel crearGenerarReportePanel() {
        JPanel panel = new JPanel();
        JButton botonGenerarReporte = new JButton("Generar reporte de inscripciones");

        botonGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inscripcionService.generarReporteInscripciones();
            }
        });

        panel.add(botonGenerarReporte);
        return panel;
    }

    private JPanel crearVolverPanel() {
        JPanel panel = new JPanel();
        JButton botonVolver = new JButton("Volver");

        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfesorPanel panel1 = new ProfesorPanel();
                dispose();

            }
        });

        panel.add(botonVolver);
        return panel;
    }



}
