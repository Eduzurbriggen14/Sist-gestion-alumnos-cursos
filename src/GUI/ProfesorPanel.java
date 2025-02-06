package GUI;

import DAO.DAOException;
import Entidades.*;
import Service.AlumnoService;
import Service.ProfesorService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class ProfesorPanel extends JFrame {
    private JButton inscribirAlumno;
    private JButton verNotasCurso;
    private JButton editarMisDatos;
    private JButton notas;
    private Connection conexion;
    private ProfesorService prof;

    public ProfesorPanel() {
        this.conexion = conexion;
        prof = new ProfesorService();
        setTitle("Panel de Profesor");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Botón para inscribir alumno
        inscribirAlumno = new JButton("Inscribir Alumno");
        inscribirAlumno.setBounds(10, 20, 200, 25);
        panel.add(inscribirAlumno);

        // Botón para ver notas por curso
        verNotasCurso = new JButton("Ver Notas por Curso");
        verNotasCurso.setBounds(10, 50, 200, 25);
        panel.add(verNotasCurso);

        // Botón para editar los datos del profesor
        editarMisDatos = new JButton("Editar Mis Datos");
        editarMisDatos.setBounds(10, 80, 200, 25);
        panel.add(editarMisDatos);

        notas = new JButton("Agregar notas alumnos");
        notas.setBounds(10,110,200,25);
        panel.add(notas);

        // Acción para el botón "Inscribir Alumno"
        inscribirAlumno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InscripcionPanel i = null;
                try {
                    i = new InscripcionPanel();
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
                i.setVisible(true);
                dispose();
            }
        });

        // Acción para el botón "Ver Notas por Curso"
        verNotasCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NotasPorCurso notas = new NotasPorCurso();
                notas.setVisible(true);
                dispose();
            }
        });

        // Acción para el botón "Editar Mis Datos"
        editarMisDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsuarioSesion us = Sesion.getUsuarioSesion();
                if (us != null) {
                    String nombreUsuario = us.getNombreUsuario();
                    prof = new ProfesorService();
                    try {
                        Profesor profesor = prof.recuperar(nombreUsuario);
                        EditarDatosProfesor editar = new EditarDatosProfesor(profesor);
                        editar.setVisible(true);
                        dispose();
                    } catch (ServiceException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        notas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    IngresoDeNotas ingresoDeNotas = new IngresoDeNotas();
                    ingresoDeNotas.setVisible(true);
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();

            }
        });
    }
}
