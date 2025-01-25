package GUI;

import DAO.DAOException;
import DAO.ReporteDAO;
import Entidades.Alumno;
import Entidades.Profesor;
import Service.AlumnoService;
import Service.ProfesorService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class AdminPanel extends JFrame {
    private JButton crearUsuario;
    private JButton crearCurso;
    private JButton editarCurso;
    private JButton reporteCursos; // Botón para el reporte de cursos
    private JButton verAlumnos;
    private JButton verProfesores;
    private JButton asignarProfesorCurso;
    private JButton listadosCursosPorProfesor;
    private Connection conexion;
    private AlumnoService alu;
    private ProfesorService prof;

    public AdminPanel() {
        this.conexion = conexion;
        setTitle("Panel de Administrador");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        crearUsuario = new JButton("Crear Usuario");
        crearUsuario.setBounds(10, 20, 150, 25);
        panel.add(crearUsuario);

        // Botón para crear un nuevo curso
        crearCurso = new JButton("Crear Curso");
        crearCurso.setBounds(10, 80, 150, 25);
        panel.add(crearCurso);

        editarCurso = new JButton("Editar Curso");
        editarCurso.setBounds(10, 140, 150, 25);
        panel.add(editarCurso);

        // Nuevo botón para reporte de cursos
        reporteCursos = new JButton("Reporte de Cursos");
        reporteCursos.setBounds(10, 200, 150, 25);
        panel.add(reporteCursos);

        verAlumnos = new JButton("Ver Alumnos");
        verAlumnos.setBounds(10, 260, 150, 25);
        panel.add(verAlumnos);

        verProfesores = new JButton("Ver Profesores");
        verProfesores.setBounds(10, 320, 150, 25);
        panel.add(verProfesores);

        asignarProfesorCurso = new JButton("Asignar Profesor a Curso");
        asignarProfesorCurso.setBounds(10, 380, 200, 25); // Ajusta la posición y tamaño
        panel.add(asignarProfesorCurso);

        listadosCursosPorProfesor = new JButton("Listado de Cursos por Profesor");
        listadosCursosPorProfesor.setBounds(10, 440, 250, 25); // Ajusta la posición y tamaño
        panel.add(listadosCursosPorProfesor);

        reporteCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReporteDAO reporteDAO = new ReporteDAO();
                List<Map<String, Object>> listaReporte = reporteDAO.obtenerReporteCursos();

                // Verificar si los datos no son nulos ni vacíos
                if (listaReporte == null || listaReporte.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No se encontraron datos para mostrar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Mostrar el reporte
                ReportePanel.mostrarReporteEnVentana(listaReporte);
            }
        });

        crearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistroUsuario();
                dispose();
            }
        });

        crearCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistroCurso();
                dispose();
            }
        });

        editarCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditarDatosCurso();
                dispose();
            }
        });

        verAlumnos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alu = new AlumnoService();
                List<Alumno> alumnos;
                try {
                    alumnos = alu.recuperarTodos();
                } catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }
                String eleccion = "Alumno";
                TablaUsuario t = new TablaUsuario(alumnos, eleccion);
                t.setVisible(true);
                dispose();
            }
        });

        verProfesores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prof = new ProfesorService();
                List<Profesor> profesores;
                try {
                    profesores = prof.recuperarTodos();
                } catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }
                String eleccion = "Profesor";
                TablaUsuario t = new TablaUsuario(profesores, eleccion);
                t.setVisible(true);
                dispose();
            }
        });

        asignarProfesorCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AsignarProfesorCurso asignarProfesorCurso = null;
                try {
                    asignarProfesorCurso = new AsignarProfesorCurso();
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                } catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }
                asignarProfesorCurso.setVisible(true);
                dispose();
            }
        });

        listadosCursosPorProfesor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ListarCursosPorPRofesor listar = new ListarCursosPorPRofesor();
                listar.setVisible(true);
                dispose();
            }
        });
    }
}
