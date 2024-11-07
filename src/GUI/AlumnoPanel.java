package GUI;

import Entidades.Alumno;
import Entidades.Sesion;
import Entidades.UsuarioSesion;
import Service.AlumnoCursoService;
import Service.AlumnoService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AlumnoPanel extends JFrame {
    private JButton verNotas;
    private JButton verCursos;
    private JButton inscribirseCurso; // Nuevo botón
    private JButton editarMisDatos; // Nuevo botón para editar datos
    private Connection conexion;
    private AlumnoCursoService alumnoCursoService;
    private AlumnoService alu;

    public AlumnoPanel() {
        this.conexion = conexion;
        setTitle("Panel de Alumno");
        setSize(300, 250);  // Ajusta el tamaño de la ventana para incluir el nuevo botón
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        alumnoCursoService = new AlumnoCursoService();
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

        inscribirseCurso = new JButton("Inscribirme a Curso");
        inscribirseCurso.setBounds(10, 80, 150, 25);
        panel.add(inscribirseCurso);

        editarMisDatos = new JButton("Editar Mis Datos");  // Nuevo botón para editar datos
        editarMisDatos.setBounds(10, 110, 150, 25); // Ajusta la posición del nuevo botón
        panel.add(editarMisDatos);

        verNotas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para mostrar las notas del alumno
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
                    dispose();  // Cierra el panel actual
                } else {
                    System.out.println("No hay usuario autenticado.");
                    JOptionPane.showMessageDialog(AlumnoPanel.this, "No hay usuario autenticado.");
                }
            }
        });

        inscribirseCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para inscribir al alumno a un curso
                // Puedes abrir una nueva ventana o realizar alguna acción aquí
                JOptionPane.showMessageDialog(AlumnoPanel.this, "Inscripción a curso realizada.");
            }
        });

        editarMisDatos.addActionListener(new ActionListener() {  // Acción del botón Editar Mis Datos
            @Override
            public void actionPerformed(ActionEvent e) {
                UsuarioSesion us = Sesion.getUsuarioSesion();
                if (us != null){
                    String nombreUsuario = us.getNombreUsuario();
                    alu = new AlumnoService();
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
