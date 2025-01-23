package GUI;

import Entidades.Curso;
import Entidades.Semestre;
import Service.CursoService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class EditarDatosCurso extends JFrame {
    private JComboBox<Curso> cursoComboBox;  // Cambiado a JComboBox<Curso>
    private JTextField nombreCursoField;
    private JTextField descripcionCursoField;
    private JTextField cupoField;
    private JTextField precioCursoField;
    private JComboBox<String> semestreComboBox;
    private JTextField anioField;
    private JButton actualizarButton;
    private JButton cancelarButton;
    private CursoService cursoService;
    private Connection conexion;

    public EditarDatosCurso() {
        setTitle("Editar Curso");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cursoService = new CursoService();
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // ComboBox para seleccionar el curso
        JLabel cursoLabel = new JLabel("Seleccionar Curso:");
        cursoLabel.setBounds(10, 20, 150, 25);
        panel.add(cursoLabel);

        // ComboBox que carga todos los cursos disponibles
        cursoComboBox = new JComboBox<>();
        cargarCursos();
        cursoComboBox.setBounds(170, 20, 200, 25);
        panel.add(cursoComboBox);

        // Campos para editar los datos del curso
        JLabel nombreCursoLabel = new JLabel("Nombre del Curso:");
        nombreCursoLabel.setBounds(10, 60, 150, 25);
        panel.add(nombreCursoLabel);

        nombreCursoField = new JTextField(20);
        nombreCursoField.setBounds(170, 60, 200, 25);
        panel.add(nombreCursoField);

        JLabel descripcionCursoLabel = new JLabel("Descripción del Curso:");
        descripcionCursoLabel.setBounds(10, 100, 150, 25);
        panel.add(descripcionCursoLabel);

        descripcionCursoField = new JTextField(20);
        descripcionCursoField.setBounds(170, 100, 200, 25);
        panel.add(descripcionCursoField);

        JLabel cupoLabel = new JLabel("Cupo:");
        cupoLabel.setBounds(10, 140, 150, 25);
        panel.add(cupoLabel);

        cupoField = new JTextField(20);
        cupoField.setBounds(170, 140, 200, 25);
        panel.add(cupoField);

        JLabel precioCursoLabel = new JLabel("Precio del Curso:");
        precioCursoLabel.setBounds(10, 180, 150, 25);
        panel.add(precioCursoLabel);

        precioCursoField = new JTextField(20);
        precioCursoField.setBounds(170, 180, 200, 25);
        panel.add(precioCursoField);

        // Botones
        actualizarButton = new JButton("Actualizar");
        actualizarButton.setBounds(10, 300, 150, 25);
        panel.add(actualizarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(200, 300, 150, 25);
        panel.add(cancelarButton);

        // Acción de cargar los datos del curso seleccionado
        cursoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatosCursoSeleccionado();
            }
        });

        // Acción del botón de actualizar
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los valores ingresados
                String nombreCurso = nombreCursoField.getText().trim();
                String descripcionCurso = descripcionCursoField.getText().trim();
                String cupoStr = cupoField.getText().trim();
                String precioCursoStr = precioCursoField.getText().trim();

                if (nombreCurso.isEmpty() || descripcionCurso.isEmpty() || cupoStr.isEmpty() || precioCursoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos deben ser llenados.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int cupo;
                double precioCurso;
                int anio;

                try {
                    cupo = Integer.parseInt(cupoStr);
                    if (cupo <= 0) {
                        JOptionPane.showMessageDialog(null, "El cupo debe ser un número mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El cupo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    precioCurso = Double.parseDouble(precioCursoStr);
                    if (precioCurso < 0) {
                        JOptionPane.showMessageDialog(null, "El precio del curso no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El precio del curso debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

               /* try {
                    anio = Integer.parseInt(anioStr);
                    if (anio < 2000 || anio > 2100) {
                        JOptionPane.showMessageDialog(null, "Por favor, ingrese un año entre 2000 y 2100.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El año debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String semestreSeleccionado = (String) semestreComboBox.getSelectedItem();
                Semestre semestre = null;

                if ("A-Marzo/Julio".equals(semestreSeleccionado)) {
                    semestre = Semestre.SEMESTRE_A;
                } else if ("B-Agosto/Noviembre".equals(semestreSeleccionado)) {
                    semestre = Semestre.SEMESTRE_B;
                }*/

                // Crear el curso con los nuevos datos
                Curso cursoSeleccionado = (Curso) cursoComboBox.getSelectedItem();
                cursoSeleccionado.setNombreCurso(nombreCurso);
                cursoSeleccionado.setDescripcionCurso(descripcionCurso);
                cursoSeleccionado.setCupo(cupo);
                cursoSeleccionado.setPrecioCurso(precioCurso);
                cursoService.modificarCurso(cursoSeleccionado);

                JOptionPane.showMessageDialog(null, "Curso actualizado correctamente.");

                new AdminPanel();
                dispose();
            }
        });

        // Acción del botón de cancelar
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminPanel();
                dispose();
            }
        });
    }

    private void cargarCursos() {
        List<Curso> cursos = cursoService.recuperarTodosLosCursos();
        for (Curso curso : cursos) {
            cursoComboBox.addItem(curso);  // Agregar el curso completo al JComboBox
        }
    }

    private void cargarDatosCursoSeleccionado() {
        Curso cursoSeleccionado = (Curso) cursoComboBox.getSelectedItem();  // Obtener el objeto completo
        if (cursoSeleccionado != null) {
            nombreCursoField.setText(cursoSeleccionado.getNombreCurso());
            descripcionCursoField.setText(cursoSeleccionado.getDescripcionCurso());
            cupoField.setText(String.valueOf(cursoSeleccionado.getCupo()));
            precioCursoField.setText(String.valueOf(cursoSeleccionado.getPrecioCurso()));

        }
    }
}
