package GUI;

import Entidades.*;
import Service.AlumnoService;
import Service.ProfesorService;
import Service.ServiceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

// Clase para mostrar la tabla de usuarios
public class TablaUsuario extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private Connection conexion;
    private AlumnoService alu;
    private ProfesorService prof;
    private String tipo;

    public TablaUsuario(List<?> usuarios, String tipoUsuario) {
        this.conexion = conexion;
        this.tipo = tipoUsuario;
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Definir las columnas en función del tipo de usuario
        if ("Alumno".equalsIgnoreCase(tipoUsuario)) {
            setTitle("Lista de Alumnos");
            tableModel = new DefaultTableModel(new Object[]{"Usuario", "Nombre", "Correo", "Abono", "Editar"}, 0);
            cargarTablaAlumno((List<Alumno>) usuarios);
        } else if ("Profesor".equalsIgnoreCase(tipoUsuario)) {
            setTitle("Lista de Profesores");
            tableModel = new DefaultTableModel(new Object[]{"Usuario", "Nombre", "Correo", "Editar"}, 0);
            cargarTablaProfesor((List<Profesor>) usuarios);
        }

        userTable = new JTable(tableModel);

        // Renderizador de celda para el botón de edición
        userTable.getColumn("Editar").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox()));

        // Agregar la tabla a la ventana
        setLayout(new BorderLayout());
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Crear un panel para centrar el botón "Volver"
        JPanel buttonPanel = new JPanel();
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminPanel();
                dispose(); // Cierra la ventana al hacer clic en "Volver"
            }
        });

        // Agregar el botón al panel y centrarlo
        buttonPanel.add(volverButton);
        add(buttonPanel, BorderLayout.SOUTH); // Añadir el panel en la parte inferior
    }

    // Método para cargar datos en la tabla de alumnos
    public void cargarTablaAlumno(List<Alumno> alumnos) {
        tableModel.setRowCount(0); // Limpiar la tabla

        for (Alumno alumno : alumnos) {
            tableModel.addRow(new Object[]{
                    alumno.getNombreUsuario(),
                    alumno.getNombre(),
                    alumno.getCorreo(),
                    alumno.getAbonoAlumno(),
                    "Editar"
            });
        }
    }

    // Método para cargar datos en la tabla de profesores
    public void cargarTablaProfesor(List<Profesor> profesores) {
        tableModel.setRowCount(0); // Limpiar la tabla

        for (Profesor profesor : profesores) {
            tableModel.addRow(new Object[]{
                    profesor.getNombreUsuario(),
                    profesor.getNombre(),
                    profesor.getCorreo(),
                    "Editar"
            });
        }
    }

    // Renderizador para el botón de eliminación
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Eliminar");
            setBackground(Color.RED); // Establecer color de fondo en rojo
            setForeground(Color.WHITE); // Establecer color del texto en blanco
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor para el botón de eliminación
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Eliminar");
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Eliminar" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                int row = userTable.getSelectedRow();
                if (row < 0) {
                    clicked = false;
                    return label;
                }

                String nombreUsuario = tableModel.getValueAt(row, 0).toString();

                if (tipo.equalsIgnoreCase("alumno")) {
                    alu = new AlumnoService();
                    try {
                        alu.eliminar(nombreUsuario);
                        JOptionPane.showMessageDialog(null, "Usuario eliminado: " + nombreUsuario);
                        // Usamos invokeLater para actualizar la tabla después de que termine la edición
                        SwingUtilities.invokeLater(() -> {
                            try {
                                cargarTablaAlumno(alu.recuperarTodos());
                            } catch (ServiceException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (ServiceException e) {
                        JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (tipo.equalsIgnoreCase("profesor")) {
                    prof = new ProfesorService();
                    try {
                        prof.eliminar(nombreUsuario);
                        JOptionPane.showMessageDialog(null, "Usuario eliminado: " + nombreUsuario);
                        SwingUtilities.invokeLater(() -> {
                            try {
                                cargarTablaProfesor(prof.recuperarTodos());
                            } catch (ServiceException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (ServiceException e) {
                        JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            clicked = false;
            return label;
        }




        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}