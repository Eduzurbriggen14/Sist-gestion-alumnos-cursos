package GUI;

import DAO.AlumnoDAO;
import DAO.CursoDAO;
import DAO.DAOException;
import DAO.InscripcionDAO;
import Entidades.Alumno;
import Entidades.Curso;
import Entidades.Inscripcion;
import Service.InscripcionService;
import Service.ReporteService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class InscripcionPanel extends JFrame {

    private static InscripcionService inscripcionService;
    private InscripcionDAO inscripcionDAO;
    private AlumnoDAO alumnoDAO;
    private CursoDAO cursoDAO;

    public InscripcionPanel() throws DAOException {
        inscripcionDAO = new InscripcionDAO();
        alumnoDAO = new AlumnoDAO();
        cursoDAO = new CursoDAO();

        inscripcionService = new InscripcionService(inscripcionDAO, alumnoDAO, cursoDAO);

        setTitle("Sistema de Inscripciones");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(crearInscripcionPanel());
        panel.add(crearInscripcionesPorAlumnoPanel());
        panel.add(crearInscripcionesPorCursoPanel());
        //panel.add(crearGenerarReportePanel());
        panel.add(crearVolverPanel());

        add(panel);
    }

    private JPanel crearInscripcionPanel() throws DAOException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        List<Alumno> alumnos = alumnoDAO.recuperarTodos();
        List<Curso> cursos = cursoDAO.recuperarTodos();

        JComboBox<Alumno> comboAlumnos = new JComboBox<>(alumnos.toArray(new Alumno[0]));
        JComboBox<Curso> comboCursos = new JComboBox<>(cursos.toArray(new Curso[0]));

        JLabel labelCalificacion = new JLabel("Calificación final:");
        JTextField textCalificacion = new JTextField("0", 5);

        JButton botonInscribir = new JButton("Inscribir");

        botonInscribir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alumno alumnoSeleccionado = (Alumno) comboAlumnos.getSelectedItem();
                Curso cursoSeleccionado = (Curso) comboCursos.getSelectedItem();
                double calificacion = Double.parseDouble(textCalificacion.getText());

                System.out.println("Alumno seleccionado: " + alumnoSeleccionado);
                System.out.println("Curso seleccionado: " + cursoSeleccionado);

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

        panel.add(new JLabel("Alumno:"));
        panel.add(comboAlumnos);
        panel.add(new JLabel("Curso:"));
        panel.add(comboCursos);
        panel.add(labelCalificacion);
        panel.add(textCalificacion);
        panel.add(botonInscribir);

        return panel;
    }

    private JPanel crearInscripcionesPorAlumnoPanel() throws DAOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel labelUsuario = new JLabel("Seleccione el alumno:");
        List<Alumno> alumnos = alumnoDAO.recuperarTodos(); // Obtener lista de alumnos
        JComboBox<Alumno> comboAlumnos = new JComboBox<>(alumnos.toArray(new Alumno[0])); // Convertir lista a array para JComboBox

        JButton botonVer = new JButton("Ver inscripciones");

        botonVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alumno alumnoSeleccionado = (Alumno) comboAlumnos.getSelectedItem();
                if (alumnoSeleccionado == null) {
                    JOptionPane.showMessageDialog(panel, "Debe seleccionar un alumno.");
                    return;
                }

                List<Inscripcion> inscripciones = inscripcionService.obtenerInscripcionesPorAlumno(alumnoSeleccionado.getNombreUsuario());

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
        panel.add(comboAlumnos); // Añadir combo box en lugar de text field
        panel.add(botonVer);

        return panel;
    }

    private JPanel crearInscripcionesPorCursoPanel() throws DAOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel labelCurso = new JLabel("Seleccione el curso:");
        List<Curso> cursos = cursoDAO.recuperarTodos(); // Obtener lista de cursos
        JComboBox<Curso> comboCursos = new JComboBox<>(cursos.toArray(new Curso[0])); // Convertir lista a array para JComboBox

        JButton botonVer = new JButton("Ver inscripciones");

        botonVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Curso cursoSeleccionado = (Curso) comboCursos.getSelectedItem();
                if (cursoSeleccionado == null) {
                    JOptionPane.showMessageDialog(panel, "Debe seleccionar un curso.");
                    return;
                }

                List<Inscripcion> inscripciones = inscripcionService.obtenerInscripcionesPorCurso(cursoSeleccionado.getNombreCurso());

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
        panel.add(comboCursos); // Añadir combo box en lugar de text field
        panel.add(botonVer);

        return panel;
    }


  /*  private static JPanel crearGenerarReportePanel() {
        JPanel panel = new JPanel();
        JButton botonGenerarReporte = new JButton("Generar reporte de inscripciones");

        botonGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReporteService reporteService = new ReporteService();
                List<Map<String, Object>> reporte = null;
                try {
                    reporte = reporteService.generarReporte();
                } catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }


                mostrarReporte(reporte);
            }
        });

        panel.add(botonGenerarReporte);
        return panel;
    }*/


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
