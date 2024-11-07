package GUI;

import Entidades.*;
import Service.AlumnoService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditarDatosAlumno extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField userNameField;
    private JButton saveButton;
    private JButton cancelButton;
    private Usuario usuarioActual;
    private AlumnoService alu;

    public EditarDatosAlumno(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Editar Mis Datos");
        setSize(400, 300); // Tamaño de ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Etiqueta y campo de texto para Nombre
        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        nameField = new JTextField(20);
        nameField.setBounds(100, 20, 250, 25);
        nameField.setText(usuarioActual.getNombre()); // Cargar el nombre actual
        panel.add(nameField);

        // Etiqueta y campo de texto para Nombre Usuario
        JLabel userNameLabel = new JLabel("Nombre Usuario:");
        userNameLabel.setBounds(10, 60, 120, 25);
        panel.add(userNameLabel);

        userNameField = new JTextField(20);
        userNameField.setBounds(100, 60, 250, 25);
        userNameField.setText(usuarioActual.getNombreUsuario()); // Cargar el nombre de usuario actual
        panel.add(userNameField);

        // Etiqueta y campo de texto para Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 100, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(100, 100, 250, 25);
        emailField.setText(usuarioActual.getCorreo()); // Cargar el email actual
        panel.add(emailField);

        // Etiqueta y campo de texto para Contraseña
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(10, 140, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 140, 250, 25);
        panel.add(passwordField);

        // Botón para guardar los cambios
        saveButton = new JButton("Guardar Cambios");
        saveButton.setBounds(10, 180, 150, 25);
        panel.add(saveButton);

        // Botón para cancelar
        cancelButton = new JButton("Cancelar");
        cancelButton.setBounds(170, 180, 150, 25);
        panel.add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String userName = userNameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                System.out.println("Guardando datos: " + name + ", " + userName + ", " + email+ " "+ password);  // Depuración

                if (name.isEmpty() || userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son requeridos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                alu = new AlumnoService();
                try {
                    Alumno alumno = alu.recuperarPorNombreUsuario(usuarioActual.getNombreUsuario());
                    alumno.setNombre(name);
                    alumno.setPassw(password);
                    alumno.setNombreUsuario(userName);
                    alumno.setCorreo(email);
                    alu.modificar(alumno);
                    JOptionPane.showMessageDialog(null, "Datos actualizados con éxito.");
                    new AlumnoPanel();
                    dispose();
                } catch (ServiceException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al actualizar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para el botón de cancelar
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AlumnoPanel();
                dispose();
            }
        });
    }
}
