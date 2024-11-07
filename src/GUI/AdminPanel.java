package GUI;

import Entidades.Alumno;
import Entidades.Profesor;
import Service.AlumnoService;
import Service.ProfesorService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class AdminPanel extends JFrame {
    private JButton crearUsuario;
    private JButton crearCurso;
    private JButton editarCurso;
    private JButton generarReporte;
    private JButton verAlumnos;
    private JButton verProfesores;
    private Connection conexion;
    private AlumnoService alu;
    private ProfesorService prof;

    public AdminPanel() {
        this.conexion = conexion;
        setTitle("Panel de Administrador");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        crearUsuario = new JButton("Crear Usuario");
        crearUsuario.setBounds(10, 20, 150, 25);
        panel.add(crearUsuario);

        // Botón para crear un nuevo curso
        crearCurso = new JButton("Crear Curso");
        crearCurso.setBounds(10, 80, 150, 25);
        panel.add(crearCurso);

        editarCurso = new JButton("Editar Curso");
        editarCurso.setBounds(10, 140, 150, 25);
        panel.add(editarCurso);

        generarReporte = new JButton("Generar Reportes");
        generarReporte.setBounds(10, 200, 150, 25);
        panel.add(generarReporte);

        verAlumnos = new JButton("Ver Alumnos");
        verAlumnos.setBounds(10, 260, 150, 25);
        panel.add(verAlumnos);

        verProfesores = new JButton("Ver Profesores");
        verProfesores.setBounds(10, 320, 150, 25);
        panel.add(verProfesores);

        crearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistroUsuario();
                dispose();
            }
        });

        crearCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistroCurso();
                dispose();
            }
        });

        editarCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditarDatosCurso();
                dispose();
            }
        });

        generarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Funcionalidad de reportes aún no implementada.");
            }
        });

        verAlumnos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alu = new AlumnoService();
                List<Alumno> alumnos;
                try {
                    alumnos = alu.recuperarTodos();
                } catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }
                String eleccion = "Alumno";
                TablaUsuario t = new TablaUsuario(alumnos, eleccion);
                t.setVisible(true);
                dispose();
            }
        });

        verProfesores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prof = new ProfesorService();
                List<Profesor> profesores;
                try {
                    profesores = prof.recuperarTodos();
                } catch (ServiceException ex) {
                    throw new RuntimeException(ex);
                }
                String eleccion = "Profesor";
                TablaUsuario t = new TablaUsuario(profesores, eleccion);
                t.setVisible(true);
                dispose();
            }
        });
    }
}
