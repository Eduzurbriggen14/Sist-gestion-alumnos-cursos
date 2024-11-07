package GUI;

import Entidades.Administrador;
import Entidades.Alumno;
import Entidades.Profesor;
import Entidades.TipoUsuario;
import Service.AdministradorService;
import Service.AlumnoService;
import Service.ProfesorService;
import Service.ServiceException;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class RegistroUsuario extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField userNameField;
    private JComboBox<String> userTypeComboBox;
    private JButton registerButton;
    private JButton cancelButton; // Nuevo botón de cancelar

    private AlumnoService alumnoService;
    private ProfesorService profService;
    private AdministradorService adminService;
    private Connection conexion;

    public RegistroUsuario() {
        setTitle("Registro de Usuario");
        setSize(400, 300); // Tamaño de ventana aumentado
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        setVisible(true);

        alumnoService = new AlumnoService();
        profService = new ProfesorService();
        adminService = new AdministradorService();
        this.conexion = conexion;
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Etiqueta y campo de texto para Nombre
        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        nameField = new JTextField(20);
        nameField.setBounds(100, 20, 250, 25); // Aumentado el ancho
        panel.add(nameField);

        // Etiqueta y campo de texto para Nombre Usuario
        JLabel userNameLabel = new JLabel("Nombre Usuario:");
        userNameLabel.setBounds(10, 60, 120, 25);
        panel.add(userNameLabel);

        userNameField = new JTextField(20);
        userNameField.setBounds(100, 60, 250, 25); // Aumentado el ancho
        panel.add(userNameField);

        // Etiqueta y campo de texto para Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 100, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(100, 100, 250, 25); // Aumentado el ancho
        panel.add(emailField);

        // Etiqueta y campo de texto para Contraseña
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(10, 140, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 140, 250, 25); // Aumentado el ancho
        panel.add(passwordField);

        // Selección del Tipo de Usuario
        JLabel userTypeLabel = new JLabel("Tipo de Usuario:");
        userTypeLabel.setBounds(10, 180, 120, 25);
        panel.add(userTypeLabel);

        userTypeComboBox = new JComboBox<>(new String[]{"Alumno", "Profesor", "Administrador"});
        userTypeComboBox.setBounds(140, 180, 210, 25); // Aumentado el ancho
        panel.add(userTypeComboBox);

        // Botón de registro
        registerButton = new JButton("Registrar");
        registerButton.setBounds(10, 220, 150, 25);
        panel.add(registerButton);

        // Botón de cancelar
        cancelButton = new JButton("Cancelar");
        cancelButton.setBounds(170, 220, 150, 25);
        panel.add(cancelButton);

        // Acción para el botón de registrar
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String nombreUsuario = userNameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String tipoUsuarioString = (String) userTypeComboBox.getSelectedItem();

                if (name.isEmpty() || nombreUsuario.isEmpty() || email.isEmpty() || password.isEmpty() || tipoUsuarioString == null || tipoUsuarioString.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son requeridos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TipoUsuario tipo = TipoUsuario.valueOf(tipoUsuarioString.toUpperCase());
                switch (tipoUsuarioString) {
                    case "Alumno":
                        int abonoResponse = JOptionPane.showConfirmDialog(null,
                                "¿Desea agregar un abono a este usuario?",
                                "Agregar Abono",
                                JOptionPane.YES_NO_OPTION);
                        boolean tieneAbono = (abonoResponse == JOptionPane.YES_OPTION);


                        Alumno alu = new Alumno(name, password, nombreUsuario, email, tipo, tieneAbono );
                        try {

                            alumnoService.guardar(alu);
                            JOptionPane.showMessageDialog(null, "Alumno registrado: " + name);
                        } catch (ServiceException ex) {
                            throw new RuntimeException(ex);
                        }

                        break;
                    case "Profesor":
                        Profesor prof = new Profesor(name, password, nombreUsuario, email, tipo);
                        try{
                            profService.guardar(prof);
                            JOptionPane.showMessageDialog(null, "Profesor registrado: " + name);
                        }catch (ServiceException ex) {
                            throw new RuntimeException(ex);
                        }

                        break;
                    case "Administrador":

                        Administrador admin = new Administrador(name, password, nombreUsuario, email, tipo);
                        try{
                            adminService.guardar(admin);
                            JOptionPane.showMessageDialog(null, "Administrador registrado: " + name);
                        }catch (ServiceException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Tipo de usuario no válido.");
                        break;
                }
                new AdminPanel();
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminPanel();
                dispose();
            }
        });
    }
}