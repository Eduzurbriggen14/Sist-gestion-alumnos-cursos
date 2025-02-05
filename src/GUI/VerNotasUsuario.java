package GUI;

import DAO.AlumnoDAO;
import DAO.CursoDAO;
import DAO.InscripcionDAO;
import Entidades.*;
import Service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VerNotasUsuario extends JFrame {

    private AlumnoService alumnoService;
    private AlumnoCursoService alumnoCursoService;
    private CursoService cursoService;
    private JTable tablaNotas;
    private JComboBox<String> comboCursos;
    private Connection conexion;

    private AlumnoDAO alumnoDAO;
    private CursoDAO cursoDAO;
    private InscripcionDAO inscripcionDAO;

    public VerNotasUsuario() {
        // Inicializar los servicios
        this.alumnoService = new AlumnoService();
        this.alumnoCursoService = new AlumnoCursoService();
        this.cursoService = new CursoService();
        this.alumnoDAO = new AlumnoDAO();
        this.cursoDAO = new CursoDAO();
        this.inscripcionDAO = new InscripcionDAO();

        this.conexion = conexion;

        // Configurar la ventana principal
        setTitle("Ver Notas del Usuario");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear el ComboBox para seleccionar el curso
        comboCursos = new JComboBox<>();
        comboCursos.addActionListener(e -> cargarNotasDelCurso());
        add(comboCursos, BorderLayout.NORTH);

        // Crear la tabla para mostrar las notas
        tablaNotas = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaNotas);
        add(scrollPane, BorderLayout.CENTER);

        // Cargar los cursos del usuario al ComboBox
        try {
            cargarCursosDelUsuario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los cursos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Crear el botón "Volver"
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> {
            new AlumnoPanel();  // Regresar al panel anterior
            dispose();
        });

        // Agregar el botón "Volver" en la parte inferior
        JPanel panelBotones = new JPanel();
        panelBotones.add(volverButton);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCursosDelUsuario() {
        try {
            // Suponiendo que tienes una sesión activa con el usuario
            UsuarioSesion usuarioActual = Sesion.getUsuarioSesion();
            if (usuarioActual != null) {
                Set<String> cursosUnicos = new HashSet<>();
                List<Inscripcion> listaInscripcionesAlumno = inscripcionDAO.obtenerPorAlumno(usuarioActual.getNombreUsuario());
                for (Inscripcion inscripcion : listaInscripcionesAlumno) {
                    String nombreCurso = inscripcion.getNombreCurso();
                    cursosUnicos.add(nombreCurso);
                }
                List<String> listaCursos = new ArrayList<>(cursosUnicos);
                for (String curso : listaCursos){
                    comboCursos.addItem(curso);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No hay usuario autenticado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los cursos del usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarNotasDelCurso() {
        String cursoSeleccionado = (String) comboCursos.getSelectedItem();
        if (cursoSeleccionado != null) {
            try {
                // Suponiendo que tienes el usuario y los cursos bien configurados
                UsuarioSesion usuarioActual = Sesion.getUsuarioSesion();
                if (usuarioActual != null) {
                    InscripcionService inscripcionService = new InscripcionService(inscripcionDAO, alumnoDAO, cursoDAO);
                    Inscripcion inscripcionUsusario = inscripcionService.obtener(usuarioActual.getNombreUsuario(), cursoSeleccionado);
                    int id = inscripcionUsusario.getId();

                    CalificacionInscripcionService calificacionInscripcionService = new CalificacionInscripcionService();
                    List<CalificacionInscripcion> calificaciones = calificacionInscripcionService.obtenerCalificacionesPorInscripcion(id);
                    actualizarTablaConNotas(calificaciones);

                } else {
                    JOptionPane.showMessageDialog(this, "No hay usuario autenticado.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar las notas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarTablaConNotas(List<CalificacionInscripcion> calificaciones) {
        String[] columnNames = {"Fecha", "Valor Nota", "Tipo de Calificación"};

        // Crear un modelo de tabla
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (CalificacionInscripcion calificacion : calificaciones) {
            Object[] row = new Object[3];
            row[0] = calificacion.getFecha();
            row[1] = calificacion.getValorNota();
            row[2] = calificacion.getTipo().toString();

            model.addRow(row);
        }
        tablaNotas.setModel(model);
    }
}

