package GUI;

import DAO.AlumnoDAO;
import DAO.CursoDAO;
import DAO.DAOException;
import DAO.InscripcionDAO;
import Entidades.*;
import Service.AlumnoService;
import Service.CursoService;
import Service.InscripcionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class IngresoDeNotas extends JFrame {

    private JComboBox<String> comboCursos;
    private JTable tablaAlumnos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregarCalificacion;
    private JButton btnVolver;

    private Connection conexion;
    private static InscripcionService inscripcionService;
    private InscripcionDAO inscripcionDAO;
    private AlumnoDAO alumnoDAO;
    private CursoDAO cursoDAO;

    public IngresoDeNotas() throws DAOException {
        setTitle("Gestión de Calificaciones");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.conexion = conexion;
        inscripcionDAO = new InscripcionDAO();
        alumnoDAO = new AlumnoDAO();
        cursoDAO = new CursoDAO();

        // Panel Superior: Selección de curso
        JPanel panelSuperior = new JPanel(new FlowLayout());
        JLabel lblCurso = new JLabel("Seleccione Curso:");
        comboCursos = new JComboBox<>();
        cargarCursos();
        panelSuperior.add(lblCurso);
        panelSuperior.add(comboCursos);

        modeloTabla = new DefaultTableModel(new String[]{"ID Alumno", "Nombre", "Curso"}, 0);
        tablaAlumnos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaAlumnos);

        btnAgregarCalificacion = new JButton("Agregar Calificación");
        btnAgregarCalificacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaCalificaciones();
            }
        });

        btnVolver = new JButton("Volver al Panel del Profesor");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfesorPanel().setVisible(true);
                dispose();
            }
        });

        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(btnAgregarCalificacion);
        panelInferior.add(btnVolver);

        setLayout(new BorderLayout());
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        comboCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarAlumnos();
            }
        });
    }

    private void cargarCursos() throws DAOException {
        cursoDAO = new CursoDAO();
        List<Curso> listaCursos = cursoDAO.recuperarTodos();
        for (Curso curso : listaCursos) {
            comboCursos.addItem(curso.getNombreCurso());
        }
    }

    private void cargarAlumnos() {
        modeloTabla.setRowCount(0);
        String cursoSeleccionado = (String) comboCursos.getSelectedItem();

        InscripcionService inscripcionService = new InscripcionService(inscripcionDAO, alumnoDAO, cursoDAO);
        try {
            List<Inscripcion> listaInscripciones = inscripcionDAO.obtenerPorCurso(cursoSeleccionado);

            for (Inscripcion inscripcion : listaInscripciones) {
                int id_usuario = alumnoDAO.obtenerIdAlumnoPorNombreUsuario(inscripcion.getNombreUsuario());
                Object[] fila = new Object[]{
                        id_usuario,
                        inscripcion.getNombreUsuario(),
                        inscripcion.getNombreCurso()
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los alumnos del curso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVentanaCalificaciones() {
        int filaSeleccionada = tablaAlumnos.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idAlumno = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            String nombreCurso = (String) modeloTabla.getValueAt(filaSeleccionada, 2);
            new VentanaCalificaciones(idAlumno, nombreUsuario, nombreCurso).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para agregar calificaciones.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
