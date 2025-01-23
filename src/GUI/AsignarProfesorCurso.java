package GUI;

import DAO.DAOException;
import DB.DBConfig;
import Entidades.Curso;
import Entidades.Profesor;
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

        // Listener para el botón Asignar
        btnAsignar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String profesorSeleccionado = (String) comboProfesores.getSelectedItem();
                String cursoSeleccionado = (String) comboCursos.getSelectedItem();
                String anioTexto = txtAnio.getText();

                System.out.println("profe seleccionado " +profesorSeleccionado);
                System.out.println("Curso seleccionado " +cursoSeleccionado);

                if (profesorSeleccionado != null && cursoSeleccionado != null && !anioTexto.isEmpty()) {
                    try {
                        int anio = Integer.parseInt(anioTexto);
                        ProfesorCursoService service = new ProfesorCursoService();
                        service.asignarCursoAProfesor(profesorSeleccionado, cursoSeleccionado, anio);
                    } catch (Exception ex) {
                        ex.printStackTrace();
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
