package GUI;

import Entidades.Calificacion;
import Service.UsuarioCursoCalificacionService;
import Entidades.Alumno;
import Entidades.Curso;
import Entidades.UsuarioCursoCalificacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class IngresoNotas extends JFrame {
    private JComboBox<String> comboCursos;
    private JComboBox<String> comboAlumnos;
    private JComboBox<String> comboTipoNota;
    private JTextField txtNota;
    private JButton btnGuardar;
    private UsuarioCursoCalificacionService service;
    private Connection conexion;


    public IngresoNotas(List<String> cursos) {
        //this.service = new UsuarioCursoCalificacionService();
        this.conexion = conexion;
        setTitle("Ingreso de Notas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        // ComboBox de cursos
        comboCursos = new JComboBox<>(cursos.toArray(new String[0]));
        comboCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarListaAlumnos();
            }
        });

        // ComboBox de alumnos
        comboAlumnos = new JComboBox<>();

        // ComboBox de tipo de nota
        String[] tiposDeNota = {"Nota 1", "Nota 2", "Recuperatorio"};
        comboTipoNota = new JComboBox<>(tiposDeNota);

        // Campo para ingresar la nota
        txtNota = new JTextField();

        // Botón para guardar
        btnGuardar = new JButton("Guardar Nota");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarNota();
            }
        });

        // Agregar componentes al panel
        panel.add(new JLabel("Curso:"));
        panel.add(comboCursos);
        panel.add(new JLabel("Alumno:"));
        panel.add(comboAlumnos);
        panel.add(new JLabel("Tipo de Nota:"));
        panel.add(comboTipoNota);
        panel.add(new JLabel("Nota:"));
        panel.add(txtNota);
        panel.add(btnGuardar);

        // Agregar panel al frame
        add(panel);
    }

    // Método para actualizar la lista de alumnos según el curso seleccionado
    private void actualizarListaAlumnos() {
        String cursoSeleccionado = (String) comboCursos.getSelectedItem();
        try {
            // Obtenemos la lista de alumnos desde el servicio
            List<Alumno> alumnos = service.obtenerAlumnos(); // Asegúrate de que este método devuelva todos los alumnos disponibles
            comboAlumnos.removeAllItems();
            for (Alumno alumno : alumnos) {
                comboAlumnos.addItem(alumno.getNombreUsuario()); // Asumiendo que Alumno tiene un método getNombreUsuario()
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener alumnos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para guardar la nota en la base de datos
    private void guardarNota() {
        String cursoSeleccionado = (String) comboCursos.getSelectedItem();
        String alumnoSeleccionado = (String) comboAlumnos.getSelectedItem();
        String tipoNota = (String) comboTipoNota.getSelectedItem();
        String nota = txtNota.getText();

        if (cursoSeleccionado == null || alumnoSeleccionado == null || nota.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double notaIngresada = Double.parseDouble(nota);

            // Obtener el objeto Alumno por el nombre de usuario
            Alumno alumno = service.obtenerAlumnos().stream()
                    .filter(a -> a.getNombreUsuario().equals(alumnoSeleccionado))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Alumno no encontrado"));

            // Crear una instancia de Curso
            Curso curso = new Curso(cursoSeleccionado); // Suponiendo que tienes un constructor adecuado

            // Crear la instancia de UsuarioCursoCalificacion
            UsuarioCursoCalificacion usuarioCursoCalificacion = new UsuarioCursoCalificacion(alumno, curso);

            // Crear la calificación usando el tipo de nota como String
            Calificacion calificacion = new Calificacion(tipoNota, notaIngresada);

            // Agregar la calificación
            usuarioCursoCalificacion.agregarCalificacion(calificacion);

            // Asignar la calificación a través del servicio
            service.asignarCalificacion(usuarioCursoCalificacion);
            JOptionPane.showMessageDialog(this, "Nota " + tipoNota + " guardada correctamente para " + alumnoSeleccionado);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La nota debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la nota: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
