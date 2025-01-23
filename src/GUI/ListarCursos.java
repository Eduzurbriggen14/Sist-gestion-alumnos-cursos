package GUI;

import DAO.CursoDAO;
import DAO.ICursoDAO;
import Entidades.*;
import Service.AlumnoCursoService;
import Service.AlumnoService;
import Service.CursoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

public class ListarCursos extends JFrame {

    private CursoService cursoService;
    private JTable tablaCursos;
    private Connection conexion;
    private AlumnoCursoService alumnoCursoService;
    private AlumnoService alumnoService;

    public ListarCursos() {
        // Inicializar el DAO y servicios
        this.cursoService = new CursoService();
        this.conexion = conexion;
        this.alumnoCursoService = new AlumnoCursoService();
        this.alumnoService = new AlumnoService();

        // Configurar la ventana principal
        setTitle("Cursos Disponibles");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana, no la aplicación

        // Crear un layout para organizar los componentes
        setLayout(new BorderLayout());

        // Crear la tabla para mostrar los cursos
        tablaCursos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        add(scrollPane, BorderLayout.CENTER);

        try {
            List<Curso> cursos = cursoService.recuperarTodosLosCursos();
            actualizarTabla(cursos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los cursos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Crear el botón "Volver"
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> {
            new AlumnoPanel();
            dispose();
        });

        // Agregar el botón "Volver" en la parte inferior
        JPanel panelBotones = new JPanel();
        panelBotones.add(volverButton);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void actualizarTabla(List<Curso> cursos) {
        // Definir las columnas de la tabla
        String[] columnNames = {"Curso", "Descripción", "Cupo", "Precio", "Semestre", "Año", "Inscribirse"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Rellenar la tabla con los cursos y botones
        for (Curso curso : cursos) {
            Object[] row = new Object[7];
            row[0] = curso.getNombreCurso();
            row[1] = curso.getDescripcionCurso();
            row[2] = curso.getCupo();
            row[3] = curso.getPrecioCurso();
            row[4] = "Inscribirse"; // Texto del botón en la tabla

            model.addRow(row);
        }

        // Asignar el modelo de la tabla
        tablaCursos.setModel(model);

        // Configurar el render y el editor de la columna de botones
        tablaCursos.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());

        // Para cada fila, asignar un ButtonEditor único con el objeto curso completo
        for (int i = 0; i < cursos.size(); i++) {
            final Curso curso = cursos.get(i); // Obtener el objeto Curso completo
            tablaCursos.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), curso, this));
        }
    }

    private void inscribirEnCurso(Curso curso) {
        UsuarioSesion usuarioActual = Sesion.getUsuarioSesion();
        if (usuarioActual != null) {
            try {
                String nombreUsuario = usuarioActual.getNombreUsuario();
                Alumno alumno = alumnoService.recuperarPorNombreUsuario(nombreUsuario);

                alumnoCursoService.inscribirAlumnoEnCurso(alumno, curso);
                JOptionPane.showMessageDialog(this, "Inscripción realizada al curso: " + curso.getNombreCurso());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al realizar la inscripción: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay usuario autenticado.");
        }
    }

    // Clase para renderizar los botones en la tabla
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    // Clase para editar el botón (gestionar el clic)
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private Curso curso;  // Ahora guardamos el objeto curso completo
        private ListarCursos listarCursos;

        public ButtonEditor(JCheckBox checkBox, Curso curso, ListarCursos listarCursos) {
            super(checkBox);
            this.curso = curso;  // Asignar el objeto curso completo
            this.listarCursos = listarCursos;
            button = new JButton("Inscribirse");
            button.setOpaque(true);

            button.addActionListener(e -> {
                listarCursos.inscribirEnCurso(curso);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}
