package GUI;

import DAO.DAOException;
import DAO.ReporteDAO;
import Entidades.Alumno;
import Entidades.Profesor;
import Service.AlumnoService;
import Service.ProfesorService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class AdminPanel extends JFrame {
    private JButton crearUsuario;
    private JButton crearCurso;
    private JButton editarCurso;
    private JButton reporteCursos;
    private JButton verAlumnos;
    private JButton verProfesores;
    private JButton asignarProfesorCurso;
    private JButton listadosCursosPorProfesor;
    private JButton agregarPromocion; // Nuevo botón

    private Connection conexion;
    private AlumnoService alu;
    private ProfesorService prof;

    public AdminPanel() {
        this.conexion = conexion;
        setTitle("Panel de Administrador");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10)); // Distribuye los botones en filas y columnas
        add(panel);

        crearUsuario = new JButton("Crear Usuario");
        crearCurso = new JButton("Crear Curso");
        editarCurso = new JButton("Editar Curso");
        reporteCursos = new JButton("Reporte de Cursos");
        verAlumnos = new JButton("Ver Alumnos");
        verProfesores = new JButton("Ver Profesores");
        asignarProfesorCurso = new JButton("Asignar Profesor a Curso");
        listadosCursosPorProfesor = new JButton("Listado Cursos x Profesor");
        agregarPromocion = new JButton("Agregar Promoción"); // Nuevo botón

        // Agregar botones al panel
        panel.add(crearUsuario);
        panel.add(crearCurso);
        panel.add(editarCurso);
        panel.add(reporteCursos);
        panel.add(verAlumnos);
        panel.add(verProfesores);
        panel.add(asignarProfesorCurso);
        panel.add(listadosCursosPorProfesor);
        panel.add(agregarPromocion); // Agregamos el nuevo botón

        // Listeners para los botones
        reporteCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReporteDAO reporteDAO = new ReporteDAO();
                List<Map<String, Object>> listaReporte = reporteDAO.obtenerReporteCursos();

                if (listaReporte == null || listaReporte.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No se encontraron datos para mostrar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    ReportePanel.mostrarReporteEnVentana(listaReporte);
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        crearUsuario.addActionListener(e -> {
            new RegistroUsuario();
            dispose();
        });

        crearCurso.addActionListener(e -> {
            new RegistroCurso();
            dispose();
        });

        editarCurso.addActionListener(e -> {
            new EditarDatosCurso();
            dispose();
        });

        verAlumnos.addActionListener(e -> {
            alu = new AlumnoService();
            List<Alumno> alumnos;
            try {
                alumnos = alu.recuperarTodos();
            } catch (ServiceException ex) {
                throw new RuntimeException(ex);
            }
            TablaUsuario t = new TablaUsuario(alumnos, "Alumno");
            t.setVisible(true);
            dispose();
        });

        verProfesores.addActionListener(e -> {
            prof = new ProfesorService();
            List<Profesor> profesores;
            try {
                profesores = prof.recuperarTodos();
            } catch (ServiceException ex) {
                throw new RuntimeException(ex);
            }
            TablaUsuario t = new TablaUsuario(profesores, "Profesor");
            t.setVisible(true);
            dispose();
        });

        asignarProfesorCurso.addActionListener(e -> {
            try {
                AsignarProfesorCurso asignarProfesorCurso = new AsignarProfesorCurso();
                asignarProfesorCurso.setVisible(true);
                dispose();
            } catch (DAOException | ServiceException ex) {
                throw new RuntimeException(ex);
            }
        });

        listadosCursosPorProfesor.addActionListener(e -> {
            new ListarCursosPorPRofesor().setVisible(true);
            dispose();
        });

        // Acción para el nuevo botón "Agregar Promoción"
        agregarPromocion.addActionListener(e -> {
            try {
                new AgregarPromocion().setVisible(true);
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        setVisible(true);
    }
}
