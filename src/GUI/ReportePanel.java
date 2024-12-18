package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ReportePanel extends JPanel {

    public ReportePanel(List<Map<String, Object>> reporteDatos) {
        setLayout(new BorderLayout());

        // Crear un panel para las tablas
        JPanel panelTablas = new JPanel();
        panelTablas.setLayout(new BoxLayout(panelTablas, BoxLayout.Y_AXIS)); // Una tabla debajo de otra

        double totalGeneralRecaudado = 0;

        // Agrupar datos por curso
        String cursoActual = null;
        DefaultTableModel modeloTabla = null;
        double totalCursoRecaudado = 0;

        // Iterar sobre los datos del reporte
        for (Map<String, Object> fila : reporteDatos) {
            String curso = (String) fila.get("cursoNombre");
            String alumno = (fila.get("alumnoNombre") != null) ? (String) fila.get("alumnoNombre") : null;
            double precioCurso = (double) fila.get("precioCurso");

            // Si el curso cambia o es el primer curso
            if (!curso.equals(cursoActual)) {
                // Si ya se estaba procesando un curso, agregarlo al panel
                if (modeloTabla != null) {
                    agregarTablaAlPanel(panelTablas, cursoActual, modeloTabla, totalCursoRecaudado);
                }

                // Crear nueva tabla para el nuevo curso
                modeloTabla = new DefaultTableModel(
                        new Object[]{"Alumno", "Precio por Alumno"}, 0
                );

                cursoActual = curso;
                totalCursoRecaudado = 0; // Reiniciar el total del curso
            }

            // Solo agregar filas si hay inscripciones
            if (alumno != null) {
                modeloTabla.addRow(new Object[]{alumno, precioCurso});
                totalCursoRecaudado += precioCurso;
                totalGeneralRecaudado += precioCurso; // Sumar al total general
            }
        }

        // Agregar la última tabla si existe
        if (modeloTabla != null) {
            agregarTablaAlPanel(panelTablas, cursoActual, modeloTabla, totalCursoRecaudado);
        }

        // Agregar el panel de tablas al panel principal
        add(new JScrollPane(panelTablas), BorderLayout.CENTER);

        // Mostrar el total general recaudado
        JLabel lblTotalGeneral = new JLabel("Total General Recaudado: $" + totalGeneralRecaudado);
        lblTotalGeneral.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalGeneral.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTotalGeneral, BorderLayout.SOUTH);
    }

    private void agregarTablaAlPanel(JPanel panelTablas, String curso, DefaultTableModel modeloTabla, double totalCursoRecaudado) {
        JLabel tituloCurso = new JLabel("Curso: " + curso);
        tituloCurso.setFont(new Font("Arial", Font.BOLD, 16));
        tituloCurso.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTable tablaCurso = new JTable(modeloTabla);

        // Ajustar la altura de la tabla según la cantidad de filas
        tablaCurso.setRowHeight(30);  // Altura por defecto de cada fila
        int filaCount = modeloTabla.getRowCount();
        tablaCurso.setPreferredScrollableViewportSize(new Dimension(600, 30 * filaCount));  // Ajustar altura total
        JScrollPane scrollTabla = new JScrollPane(tablaCurso);

        JLabel totalCurso = new JLabel("Total Recaudado para " + curso + ": $" + totalCursoRecaudado);
        totalCurso.setFont(new Font("Arial", Font.PLAIN, 14));
        totalCurso.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Crear el panel que contiene la tabla y el total recaudado
        JPanel panelCurso = new JPanel();
        panelCurso.setLayout(new BoxLayout(panelCurso, BoxLayout.Y_AXIS));
        panelCurso.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCurso.add(tituloCurso);
        panelCurso.add(scrollTabla);
        panelCurso.add(totalCurso);

        // Agregar el panel del curso al panel de tablas
        panelTablas.add(panelCurso);
    }

    // Método estático para mostrar el reporte en una ventana JFrame
    public static void mostrarReporteEnVentana(List<Map<String, Object>> reporteDatos) {
        JFrame frameReporte = new JFrame("Reporte de Cursos");
        frameReporte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameReporte.setSize(800, 600);
        frameReporte.add(new ReportePanel(reporteDatos));
        frameReporte.setVisible(true);
    }
}
