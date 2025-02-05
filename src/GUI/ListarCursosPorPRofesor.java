package GUI;

import Entidades.*;
import Service.ProfesorCursoService;
import Service.ProfesorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ListarCursosPorPRofesor extends JFrame {
    private JComboBox<Profesor> comboBoxProfesor;
    private JButton btnBuscar;
    private JButton btnVolver;
    private JTable tablaCursos;
    private DefaultTableModel model;
    private ProfesorService profesorService;
    private ProfesorCursoService profesorCursoService;

    public ListarCursosPorPRofesor() {
        setTitle("Cursos por Profesor");
        setLayout(new BorderLayout());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        comboBoxProfesor = new JComboBox<>();
        cargarProfesores();

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna de acción es editable
            }
        };
        model.addColumn("Nombre del Curso");
        model.addColumn("Año");
        model.addColumn("Acción");

        tablaCursos = new JTable(model);
        tablaCursos.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        tablaCursos.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

        btnBuscar = new JButton("Buscar Cursos");
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCursos();
            }
        });

        btnVolver = new JButton("Volver al Panel de Admin");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminPanel(); // Cambia esto por el código que maneje la vuelta al panel admin
            }
        });

        JPanel panelTop = new JPanel();
        panelTop.add(new JLabel("Selecciona un Profesor:"));
        panelTop.add(comboBoxProfesor);

        JPanel panelBottom = new JPanel();
        panelBottom.add(btnBuscar);
        panelBottom.add(btnVolver);

        JScrollPane scrollPane = new JScrollPane(tablaCursos);

        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarProfesores() {
        try {
            profesorService = new ProfesorService();
            List<Profesor> profesores = profesorService.recuperarTodos();
            for (Profesor profesor : profesores) {
                comboBoxProfesor.addItem(profesor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarCursos() {
        model.setRowCount(0);

        Profesor profesorSeleccionado = (Profesor) comboBoxProfesor.getSelectedItem();
        if (profesorSeleccionado != null) {
            try {
                profesorCursoService = new ProfesorCursoService();
                List<ProfesorCurso> profesorCursos = profesorCursoService.obtenerCursosPorProfesor(profesorSeleccionado.getNombreUsuario());
                for (ProfesorCurso profesorCurso : profesorCursos) {
                    if (profesorCurso.getCurso() != null && profesorCurso.getCurso().getNombreCurso() != null) {
                        model.addRow(new Object[]{
                                profesorCurso.getCurso().getNombreCurso(),
                                profesorCurso.getAnio(),
                                "Eliminar"
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un profesor.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Renderizador para mostrar botones en la tabla
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
        private String nombreCurso;
        private int anio;
        private boolean clicked;
        private int rowIndex; // Agregar variable para el índice de fila

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
            nombreCurso = (String) table.getValueAt(row, 0);
            anio = (int) table.getValueAt(row, 1);
            rowIndex = row;
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                int confirm = JOptionPane.showConfirmDialog(button, "¿Deseas eliminar el curso " + nombreCurso + " del año " + anio + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    eliminarCurso(nombreCurso);
                }
            }
            clicked = false;
            return null;
        }

        private void eliminarCurso(String nombreCurso) {
            Profesor profesorSeleccionado = (Profesor) comboBoxProfesor.getSelectedItem();
            if (profesorSeleccionado != null) {
                try {
                    profesorCursoService = new ProfesorCursoService();
                    profesorCursoService.eliminarCursoDeProfesor(profesorSeleccionado.getNombreUsuario(), nombreCurso);

                    SwingUtilities.invokeLater(() -> {
                        buscarCursos();
                        JOptionPane.showMessageDialog(button, "Curso eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(button, "Error al eliminar el curso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

}
