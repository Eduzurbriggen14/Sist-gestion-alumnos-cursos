package GUI;

import DAO.DAOException;
import Entidades.Promocion;
import Service.PromocionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PromocionPanel extends JFrame {
    private JTextField nombreField;
    private JTextArea descripcionField;
    private JTextField descuentoField;
    private JButton guardarButton;
    private JButton cancelarButton;
    private PromocionService promocionService;

    public PromocionPanel() {
        setTitle("Crear Promoción");
        setSize(400, 300);
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

        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPromocion();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new AgregarPromocion().setVisible(true);
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
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
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);

        add(buttonPanel, gbc);
    }

    private void guardarPromocion() {
        String nombre = nombreField.getText().trim();
        String descripcion = descripcionField.getText().trim();
        String descuentoText = descuentoField.getText().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || descuentoText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double descuento = Double.parseDouble(descuentoText);
            Promocion promocion = new Promocion(nombre, descripcion, descuento);

            promocionService.guardarPromocion(promocion);

            JOptionPane.showMessageDialog(this, "Promoción guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            nombreField.setText("");
            descripcionField.setText("");
            descuentoField.setText("");
            new AgregarPromocion().setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El descuento debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la promoción: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
