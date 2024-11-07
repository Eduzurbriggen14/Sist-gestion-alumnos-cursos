package GUI;

import DB.DBConfig;
import Entidades.Sesion;
import Entidades.TipoUsuario;
import Entidades.Usuario;
import Entidades.UsuarioSesion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Connection conexion;
    public Login() {
        setTitle("Inicio de Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.conexion = conexion;

        JPanel panel = new JPanel();
        add(panel);
        ordenarComponentes(panel);

        setVisible(true);
    }

    private void ordenarComponentes(JPanel panel) {
        panel.setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 20, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(100, 20, 165, 25);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBounds(10, 80, 150, 25);
        panel.add(loginButton);

        registerButton = new JButton("Registrarse");
        registerButton.setBounds(10, 110, 150, 25);
        panel.add(registerButton);

        // botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Verificar credenciales
                UsuarioSesion usuario = verificarCredenciales(email, password);

                if (usuario != null) {
                    Sesion.iniciarSesion(usuario);
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                    if (usuario.getTipo() == TipoUsuario.ADMINISTRADOR) {
                        new AdminPanel();
                        dispose();
                    } else if (usuario.getTipo() == TipoUsuario.ALUMNO) {
                        new AlumnoPanel();
                        dispose();
                    } else if (usuario.getTipo() == TipoUsuario.PROFESOR) {
                        new ProfesorPanel();
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
                }
            }
        });


        //botón de registro
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistroUsuario();
                dispose();
            }
        });
    }

    public UsuarioSesion verificarCredenciales(String email, String password) {
        String queryAdmin = "SELECT * FROM administrador WHERE correo = ? AND passw = ?";
        String queryAlumno = "SELECT * FROM alumno WHERE correo = ? AND passw = ?";
        String queryProfesor = "SELECT * FROM profesor WHERE correo = ? AND passw = ?";

        try (Connection connection = DBConfig.getConexion()) {
            // Verificar en la tabla 'admin'
            UsuarioSesion usuario = verificarEnTabla(connection, queryAdmin, email, password, "ADMINISTRADOR");
            if (usuario != null) return usuario;

            // Verificar en la tabla 'alumno'
            usuario = verificarEnTabla(connection, queryAlumno, email, password, "ALUMNO");
            if (usuario != null) return usuario;

            // Verificar en la tabla 'profesor'
            usuario = verificarEnTabla(connection, queryProfesor, email, password, "PROFESOR");
            if (usuario != null) return usuario;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private UsuarioSesion verificarEnTabla(Connection connection, String query, String email, String passw, String tipoStr) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, passw);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Obtener todos los campos de la base de datos, incluidos 'nombreUsuario' y 'correo'
                String nombre = resultSet.getString("nombre");
                String nombreUsuario = resultSet.getString("nombreUsuario");
                String correo = resultSet.getString("correo");

                // Convertir tipoStr a TipoUsuario
                TipoUsuario tipo = TipoUsuario.valueOf(tipoStr.toUpperCase());

                return new UsuarioSesion(nombre, passw, nombreUsuario, correo, tipo);
            }
        }
        return null;
    }


}