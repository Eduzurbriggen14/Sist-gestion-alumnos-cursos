package GUI;

import DAO.DAOException;
import DB.DBConfig;
import Entidades.Curso;
import Entidades.Profesor;
import Entidades.ProfesorCurso;
import Service.CursoService;
import Service.ProfesorCursoService;
import Service.ProfesorService;
import Service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class AsignarProfesorCurso extends JFrame {
    private JComboBox<String> comboProfesores;
    private JComboBox<String> comboCursos;
    private JTextField txtAnio;
    private JButton btnAsignar;
    private JButton btnVolver;
    private Connection connection;

    public AsignarProfesorCurso() throws DAOException, ServiceException {
        this.connection = DBConfig.getConexion();
        setTitle("Asignar Profesor a Curso");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        // Panel para seleccionar el profesor
        JPanel panelProfesor = new JPanel();
        panelProfesor.add(new JLabel("Profesor:"));
        comboProfesores = new JComboBox<>();
        panelProfesor.add(comboProfesores);
        add(panelProfesor);

        // Panel para seleccionar el curso
        JPanel panelCurso = new JPanel();
        panelCurso.add(new JLabel("Curso:"));
        comboCursos = new JComboBox<>();
        panelCurso.add(comboCursos);
        add(panelCurso);

        // Panel para ingresar el año
        JPanel panelAnio = new JPanel();
        panelAnio.add(new JLabel("Año:"));
        txtAnio = new JTextField(10);
        panelAnio.add(txtAnio);
        add(panelAnio);

        // Panel para el botón de asignar
        JPanel panelBoton = new JPanel();
        btnAsignar = new JButton("Asignar");
        panelBoton.add(btnAsignar);
        add(panelBoton);

        // Panel para volver
        JPanel panelVolver = new JPanel();
        btnVolver = new JButton("Volver");
        panelVolver.add(btnVolver);
        add(panelVolver);

        // Cargar datos en los combos
        cargarProfesoresCombo();
        cargarCursosCombo();

        btnAsignar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String profesorSeleccionado = (String) comboProfesores.getSelectedItem();
                String cursoSeleccionado = (String) comboCursos.getSelectedItem();
                String anioTexto = txtAnio.getText();

                System.out.println("Profesor seleccionado: " + profesorSeleccionado);
                System.out.println("Curso seleccionado: " + cursoSeleccionado);

                if (profesorSeleccionado != null && cursoSeleccionado != null && !anioTexto.isEmpty()) {
                    try {
                        int anio = Integer.parseInt(anioTexto);
                        ProfesorCursoService service = new ProfesorCursoService();
                        List<ProfesorCurso> listaDeCursosPorProfesor = service.obtenerCursosPorProfesor(profesorSeleccionado);

                        boolean cursoYaAsignado = false;

                        // Verificar si el profesor ya está asignado a ese curso y año
                        for (ProfesorCurso cursos : listaDeCursosPorProfesor) {
                            if (cursos.getCurso().getNombreCurso().equals(cursoSeleccionado) && cursos.getAnio() == anio) {
                                cursoYaAsignado = true;
                                break; // No es necesario seguir buscando si ya se encontró
                            }
                        }

                        // Si no está asignado, asignar el curso
                        if (cursoYaAsignado) {
                            JOptionPane.showMessageDialog(null, "El profesor ya está asignado a este curso para el año " + anio, "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            service.asignarCursoAProfesor(profesorSeleccionado, cursoSeleccionado, anio);
                            JOptionPane.showMessageDialog(null, "Curso asignado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al asignar el curso.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminPanel().setVisible(true);
                dispose();
            }
        });
    }

    private void cargarProfesoresCombo() throws DAOException, ServiceException {
        ProfesorService profeDao = new ProfesorService();
        List<Profesor> profesores = profeDao.recuperarTodos();
        for (Profesor pf : profesores) {
            comboProfesores.addItem(pf.getNombreUsuario());
        }
    }

    private void cargarCursosCombo() throws DAOException {
        CursoService cursoDao = new CursoService();
        List<Curso> cursos = cursoDao.recuperarTodosLosCursos();
        for (Curso curso : cursos) {
            comboCursos.addItem(curso.getNombreCurso());
        }
    }
}
