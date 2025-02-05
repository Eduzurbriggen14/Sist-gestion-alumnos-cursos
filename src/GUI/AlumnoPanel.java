package GUI;

import Entidades.Alumno;
import Entidades.Sesion;
import Entidades.UsuarioSesion;
import Service.AlumnoService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlumnoPanel extends JFrame {
    private JButton verNotas;
    private JButton verCursos;
    private JButton editarMisDatos;
    private AlumnoService alu;

    public AlumnoPanel() {
        setTitle("Panel de Alumno");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        alu = new AlumnoService();
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        verNotas = new JButton("Ver Notas");
        verNotas.setBounds(10, 20, 150, 25);
        panel.add(verNotas);

        verCursos = new JButton("Ver Cursos");
        verCursos.setBounds(10, 50, 150, 25);
        panel.add(verCursos);

        editarMisDatos = new JButton("Editar Mis Datos");
        editarMisDatos.setBounds(10, 80, 150, 25);
        panel.add(editarMisDatos);

        verNotas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VerNotasUsuario().setVisible(true);
                dispose();
            }
        });

        verCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsuarioSesion usuarioActual = Sesion.getUsuarioSesion();
                System.out.println("Usuario actual: " + usuarioActual);

                if (usuarioActual != null) {
                    System.out.println("Usuario autenticado. Abriendo ListarCursosView...");
                    ListarCursos lc = new ListarCursos();
                    lc.setVisible(true);
                    dispose();
                } else {
                    System.out.println("No hay usuario autenticado.");
                    JOptionPane.showMessageDialog(AlumnoPanel.this, "No hay usuario autenticado.");
                }
            }
        });

        editarMisDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsuarioSesion us = Sesion.getUsuarioSesion();
                if (us != null){
                    String nombreUsuario = us.getNombreUsuario();
                    try {
                        Alumno alumno = alu.recuperarPorNombreUsuario(nombreUsuario);
                        EditarDatosAlumno editar = new EditarDatosAlumno(alumno);
                        editar.setVisible(true);
                        dispose();
                    } catch (ServiceException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
}
