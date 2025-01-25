package GUI;

import Entidades.Curso;
import Entidades.Profesor;
import Entidades.ProfesorCurso;
import Service.ProfesorCursoService;
import Service.ProfesorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class ListarCursosPorPRofesor extends JFrame {
    private JComboBox<Profesor> comboBoxProfesor;
    private JButton btnBuscar;
    private JButton btnVolver;
    private JTable tablaCursos;
    private DefaultTableModel model;
    private ProfesorService profesorService;
    private ProfesorCursoService profesorCursoService;

    private Connection conexion;

    public ListarCursosPorPRofesor() {
        this.conexion = conexion;
        setTitle("Cursos por Profesor");
        setLayout(new BorderLayout());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        comboBoxProfesor = new JComboBox<>();
        cargarProfesores();

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna "Acción" es editable
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
                new AdminPanel();
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
                    model.addRow(new Object[]{
                            profesorCurso.getCurso().getNombreCurso(),
                            profesorCurso.getAnio(),
                            "Eliminar"
                    });
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
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor para manejar los clics en el botón
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String nombreCurso;
        private int anio;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Eliminar");
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            nombreCurso = (String) table.getValueAt(row, 0);
            anio = (int) table.getValueAt(row, 1);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                int confirm = JOptionPane.showConfirmDialog(button, "¿Deseas eliminar el curso " + nombreCurso + " del año " + anio + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    eliminar(nombreCurso);
                }
            }
            clicked = false;
            return null;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        private void eliminar(String nombreCurso) {
            Profesor profesorSeleccionado = (Profesor) comboBoxProfesor.getSelectedItem();
            System.out.println("profe seleccionado "+ profesorSeleccionado.getNombreUsuario());
            System.out.println("nombre curso " + nombreCurso);
            if (profesorSeleccionado != null) {
                try {
                    profesorCursoService = new ProfesorCursoService();
                    profesorCursoService.eliminarCursoDeProfesor(profesorSeleccionado.getNombreUsuario(), nombreCurso);

                    buscarCursos();
                    JOptionPane.showMessageDialog(button, "El profesor fue eliminado del curso con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(button, "Error al eliminar el profesor del curso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
