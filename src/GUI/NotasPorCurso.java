package GUI;

import DAO.AlumnoDAO;
import DAO.CursoDAO;
import DAO.InscripcionDAO;
import Entidades.*;
import Service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotasPorCurso extends JFrame {
    private JComboBox<String> comboCursos;
    private JTable tablaAlumnos;
    private JScrollPane scrollPane;

    private InscripcionDAO inscripcionDAO;
    private CursoDAO cursoDAO;
    private AlumnoDAO alumnoDAO;

    public NotasPorCurso() {
        this.inscripcionDAO = new InscripcionDAO();
        this.cursoDAO = new CursoDAO();
        this.alumnoDAO = new AlumnoDAO();
        // Configurar la ventana
        setTitle("Cursos y Alumnos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Inicialización y configuración de la tabla
        tablaAlumnos = new JTable();
        configurarTabla();  // Aquí configuras el modelo de la tabla

        // Scroll para la tabla
        scrollPane = new JScrollPane(tablaAlumnos);

        // ComboBox para seleccionar el curso
        comboCursos = new JComboBox<>();
        comboCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cursoSeleccionado = (String) comboCursos.getSelectedItem();
                System.out.println(cursoSeleccionado);
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

        // Añadir el panel principal
        panel.add(panelCombo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Añadir el panel a la ventana
        add(panel);
    }

    private void cargarCursos() {
        CursoService cursoService = new CursoService();
        List<Curso> cursos = cursoService.recuperarTodosLosCursos();

        for (Curso curso : cursos) {
            comboCursos.addItem(curso.getNombreCurso());
        }
    }

   /* private void cargarAlumnosDelCurso(String nombreCurso) throws ServiceException {
        InscripcionService inscripcionService = new InscripcionService(inscripcionDAO, alumnoDAO, cursoDAO);

        List<Inscripcion> inscripcionesPorCurso = inscripcionService.obtenerInscripcionesPorCurso(nombreCurso);

        DefaultTableModel modeloTabla = (DefaultTableModel) tablaAlumnos.getModel();
        modeloTabla.setRowCount(0);

        for (Inscripcion insc : inscripcionesPorCurso) {
            String nombreUsuario = insc.getNombreUsuario();

            InscripcionDAO inscripcionDAO = new InscripcionDAO();
            Inscripcion inscId = inscripcionDAO.obtener(nombreUsuario, nombreCurso);
            if (inscId == null) {
                System.out.println("No se encontró la inscripción para el usuario: " + nombreUsuario + " en el curso: " + nombreCurso);
                continue;
            }

            int id_inscriopcion = inscId.getId();
            System.out.println("id en notas por curso " + id_inscriopcion);

            CalificacionInscripcionService calificacionInscripcionService = new CalificacionInscripcionService();
            List<CalificacionInscripcion> calificaciones = calificacionInscripcionService.obtenerCalificacionesPorInscripcion(id_inscriopcion);

            AlumnoConCalificacion alumnoConCalificacion = new AlumnoConCalificacion(nombreUsuario, nombreCurso, calificaciones);

            for (CalificacionInscripcion calificacion : calificaciones) {
                modeloTabla.addRow(new Object[]{
                        nombreUsuario,
                        nombreCurso,
                        calificacion.getValorNota(),
                        calificacion.getTipo(),
                        calificacion.getFecha()
                });
            }
        }
    }*/
   private void cargarAlumnosDelCurso(String nombreCurso) throws ServiceException {
       InscripcionService inscripcionService = new InscripcionService(inscripcionDAO, alumnoDAO, cursoDAO);

       List<Inscripcion> inscripcionesPorCurso = inscripcionService.obtenerInscripcionesPorCurso(nombreCurso);

       // Panel contenedor para las tablas de los alumnos
       JPanel panelAlumnos = new JPanel();
       panelAlumnos.setLayout(new BoxLayout(panelAlumnos, BoxLayout.Y_AXIS)); // Agrega las tablas verticalmente

       for (Inscripcion insc : inscripcionesPorCurso) {
           String nombreUsuario = insc.getNombreUsuario();

           InscripcionDAO inscripcionDAO = new InscripcionDAO();
           Inscripcion inscId = inscripcionDAO.obtener(nombreUsuario, nombreCurso);
           if (inscId == null) {
               System.out.println("No se encontró la inscripción para el usuario: " + nombreUsuario + " en el curso: " + nombreCurso);
               continue;
           }

           int id_inscriopcion = inscId.getId();
           System.out.println("id en notas por curso " + id_inscriopcion);

           CalificacionInscripcionService calificacionInscripcionService = new CalificacionInscripcionService();
           List<CalificacionInscripcion> calificaciones = calificacionInscripcionService.obtenerCalificacionesPorInscripcion(id_inscriopcion);

           // Crear el modelo de tabla para el alumno
           DefaultTableModel modeloTabla = new DefaultTableModel();
           modeloTabla.addColumn("Curso");
           modeloTabla.addColumn("Nota");
           modeloTabla.addColumn("Tipo");
           modeloTabla.addColumn("Fecha");

           // Calcular el promedio
           double sumaNotas = 0;
           int contadorNotas = 0;

           for (CalificacionInscripcion calificacion : calificaciones) {
               modeloTabla.addRow(new Object[]{
                       nombreCurso,
                       calificacion.getValorNota(),
                       calificacion.getTipo(),
                       calificacion.getFecha()
               });

               sumaNotas += calificacion.getValorNota();
               contadorNotas++;
           }

           JTable tablaAlumno = new JTable(modeloTabla);
           double promedio = (contadorNotas > 0) ? sumaNotas / contadorNotas : 0;
           String condicion = "";
           if (contadorNotas < 2){
               condicion = String.valueOf(Condicion.CURSANDO);               
           }else if(contadorNotas == 2 && promedio >= 6 ){
               condicion = String.valueOf(Condicion.APROBADO);
           } else if (contadorNotas == 3 && promedio >= 6 ) {
               condicion = String.valueOf(Condicion.APROBADO);               
           } else if (contadorNotas == 3 && promedio < 6) {
               condicion = String.valueOf(Condicion.DESAPROBADO);
               
           }


           JLabel labelAlumno = new JLabel("Alumno: " + nombreUsuario + " - Promedio: " + promedio +
                   " - CONDICION: "+ condicion.toUpperCase());

           panelAlumnos.add(labelAlumno);
           panelAlumnos.add(new JScrollPane(tablaAlumno));
       }

       JFrame frame = new JFrame("Notas por Curso");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.getContentPane().add(new JScrollPane(panelAlumnos));
       frame.pack();
       frame.setVisible(true);
   }



    private void configurarTabla() {
        DefaultTableModel modeloTabla = new DefaultTableModel(
                new String[]{"Alumno", "Curso", "Calificación", "Tipo Nota", "Fecha"}, 0
        );
        tablaAlumnos.setModel(modeloTabla);
    }

}
