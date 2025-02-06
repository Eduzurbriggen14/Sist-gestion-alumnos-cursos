package GUI;

import DAO.DAOException;
import Entidades.Promocion;
import Service.PromocionService;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PromocionPanel extends JFrame {
    private JTextField nombreField;
    private JTextArea descripcionField;
    private JTextField descuentoField;
    private JDateChooser fechaInicioChooser;
    private JDateChooser fechaFinChooser;
    private JButton guardarButton;
    private JButton cancelarButton;
    private PromocionService promocionService;

    public PromocionPanel() {
        setTitle("Crear Promoción");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        this.promocionService = new PromocionService();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(20);

        JLabel descripcionLabel = new JLabel("Descripción:");
        descripcionField = new JTextArea(3, 20);
        descripcionField.setLineWrap(true);
        descripcionField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descripcionField);

        JLabel descuentoLabel = new JLabel("Descuento (%):");
        descuentoField = new JTextField(10);

        JLabel fechaInicioLabel = new JLabel("Fecha de Inicio:");
        fechaInicioChooser = new JDateChooser();

        JLabel fechaFinLabel = new JLabel("Fecha de Fin:");
        fechaFinChooser = new JDateChooser();

        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");

        guardarButton.addActionListener(this::guardarPromocion);
        cancelarButton.addActionListener(e -> {
            try {
                new AgregarPromocion().setVisible(true);
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nombreLabel, gbc);

        gbc.gridx = 1;
        add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(descripcionLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(descuentoLabel, gbc);

        gbc.gridx = 1;
        add(descuentoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(fechaInicioLabel, gbc);

        gbc.gridx = 1;
        add(fechaInicioChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(fechaFinLabel, gbc);

        gbc.gridx = 1;
        add(fechaFinChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);

        add(buttonPanel, gbc);
    }

    private void guardarPromocion(ActionEvent e) {
        String nombre = nombreField.getText().trim();
        String descripcion = descripcionField.getText().trim();
        String descuentoText = descuentoField.getText().trim();
        Date fechaInicio = fechaInicioChooser.getDate();
        Date fechaFin = fechaFinChooser.getDate();

        if (nombre.isEmpty() || descripcion.isEmpty() || descuentoText.isEmpty() || fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fechaInicioDate = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaFinDate = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (fechaInicio.after(fechaFin)) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio no puede ser posterior a la fecha de fin.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double descuento = Double.parseDouble(descuentoText);
            Promocion promocion = new Promocion(nombre, descripcion, descuento, fechaInicioDate, fechaFinDate);
            promocionService.guardarPromocion(promocion);

            JOptionPane.showMessageDialog(this, "Promoción guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            nombreField.setText("");
            descripcionField.setText("");
            descuentoField.setText("");
            fechaInicioChooser.setDate(null);
            fechaFinChooser.setDate(null);

            new AgregarPromocion().setVisible(true);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El descuento debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la promoción: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
