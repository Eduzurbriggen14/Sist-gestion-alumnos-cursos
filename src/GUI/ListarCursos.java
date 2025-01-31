package GUI;

import Entidades.*;
import Service.CursoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class ListarCursos extends JFrame {

    private CursoService cursoService;
    private JTable tablaCursos;
    private Connection conexion;

    public ListarCursos() {
        // Inicializar el DAO y servicios
        this.cursoService = new CursoService();
        this.conexion = conexion;

        // Configurar la ventana principal
        setTitle("Cursos Disponibles");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear un layout para organizar los componentes
        setLayout(new BorderLayout());

        // Crear la tabla para mostrar los cursos
        tablaCursos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        add(scrollPane, BorderLayout.CENTER);

        try {
            List<Curso> cursos = cursoService.recuperarTodosLosCursos();
            actualizarTabla(cursos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los cursos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Crear el botón "Volver"
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> {
            new AlumnoPanel();
            dispose();
        });

        // Agregar el botón "Volver" en la parte inferior
        JPanel panelBotones = new JPanel();
        panelBotones.add(volverButton);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void actualizarTabla(List<Curso> cursos) {
        // Definir las columnas de la tabla
        String[] columnNames = {"Curso", "Descripción", "Cupo", "Precio"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Rellenar la tabla con los cursos
        for (Curso curso : cursos) {
            Object[] row = new Object[4];  // Solo 4 columnas ahora
            row[0] = curso.getNombreCurso();
            row[1] = curso.getDescripcionCurso();
            row[2] = curso.getCupo();
            row[3] = curso.getPrecioCurso();

            model.addRow(row);
        }

        // Asignar el modelo de la tabla
        tablaCursos.setModel(model);
    }
}
