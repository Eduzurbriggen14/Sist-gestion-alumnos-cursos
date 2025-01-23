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
        panel.add(crearVolverPanel());

        add(panel);
    }

    private JPanel crearInscripcionPanel() throws DAOException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        List<Alumno> alumnos = alumnoDAO.recuperarTodos();
        List<Curso> cursos = cursoDAO.recuperarTodos();

        JComboBox<Alumno> comboAlumnos = new JComboBox<>(alumnos.toArray(new Alumno[0]));
        JComboBox<Curso> comboCursos = new JComboBox<>(cursos.toArray(new Curso[0]));

        JLabel labelAnio = new JLabel("Año:");
        JTextField textAnio = new JTextField(4);

        JButton botonInscribir = new JButton("Inscribir");

        botonInscribir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alumno alumnoSeleccionado = (Alumno) comboAlumnos.getSelectedItem();
                Curso cursoSeleccionado = (Curso) comboCursos.getSelectedItem();
                String anioText = textAnio.getText();

                if (alumnoSeleccionado == null || cursoSeleccionado == null || anioText.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Debe seleccionar un alumno, un curso y especificar el año.");
                    return;
                }

                try {
                    int anio = Integer.parseInt(anioText);

                    boolean exito = inscripcionService.inscribirAlumnoEnCurso(
                            alumnoSeleccionado.getNombreUsuario(),
                            cursoSeleccionado.getNombreCurso(),
                            anio
                    );

                    if (exito) {
                        JOptionPane.showMessageDialog(panel, "Alumno inscrito exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(panel, "No se pudo inscribir al alumno.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "El año debe ser un número válido.");
                } catch (DAOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Ocurrió un error al inscribir al alumno.");
                }
            }
        });

        panel.add(new JLabel("Alumno:"));
        panel.add(comboAlumnos);
        panel.add(new JLabel("Curso:"));
        panel.add(comboCursos);
        panel.add(labelAnio);
        panel.add(textAnio);
        panel.add(botonInscribir);

        return panel;
    }

    private JPanel crearInscripcionesPorAlumnoPanel() throws DAOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel labelUsuario = new JLabel("Seleccione el alumno:");
        List<Alumno> alumnos = alumnoDAO.recuperarTodos();
        JComboBox<Alumno> comboAlumnos = new JComboBox<>(alumnos.toArray(new Alumno[0]));

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
                                .append(" - Año: ").append(inscripcion.getAnio())
                                .append("\n");
                    }
                    JOptionPane.showMessageDialog(panel, report.toString());
                }
            }
        });

        panel.add(labelUsuario);
        panel.add(comboAlumnos);
        panel.add(botonVer);

        return panel;
    }

    private JPanel crearInscripcionesPorCursoPanel() throws DAOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel labelCurso = new JLabel("Seleccione el curso:");
        List<Curso> cursos = cursoDAO.recuperarTodos();
        JComboBox<Curso> comboCursos = new JComboBox<>(cursos.toArray(new Curso[0]));

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
                                .append(" - Año: ").append(inscripcion.getAnio())
                                .append("\n");
                    }
                    JOptionPane.showMessageDialog(panel, report.toString());
                }
            }
        });

        panel.add(labelCurso);
        panel.add(comboCursos);
        panel.add(botonVer);

        return panel;
    }

    private JPanel crearVolverPanel() {
        JPanel panel = new JPanel();
        JButton botonVolver = new JButton("Volver");

        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfesorPanel pf = new ProfesorPanel();
                pf.setVisible(true);
                dispose();
            }
        });
        panel.add(botonVolver);
        return panel;
    }
}
