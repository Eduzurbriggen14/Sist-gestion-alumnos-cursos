package GUI;

import DAO.ReporteDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class GraficoReporte extends JPanel {

    public GraficoReporte() {
        ReporteDAO reporteDAO = new ReporteDAO();
        List<Map<String , Object>> reporteDatos = reporteDAO.obtenerReporteGrafico();
        setLayout(new BorderLayout());

        if (reporteDatos == null || reporteDatos.isEmpty()) {
            JLabel mensajeError = new JLabel("No hay datos para mostrar en el gráfico.");
            mensajeError.setHorizontalAlignment(SwingConstants.CENTER);
            mensajeError.setFont(new Font("Arial", Font.BOLD, 14));
            add(mensajeError);
            return;
        }

        // Crear dataset para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map<String, Object> fila : reporteDatos) {
            if (fila.containsKey("cursoNombre") && fila.containsKey("totalRecaudado")) {
                try {
                    String curso = (String) fila.get("cursoNombre");
                    double total = (Double) fila.get("totalRecaudado");
                    dataset.addValue(total, "Ingresos", curso);
                } catch (ClassCastException | NumberFormatException e) {
                    System.err.println("Error al procesar fila: " + fila);
                }
            } else {
                System.err.println("Fila no contiene las claves esperadas: " + fila);
            }
        }

        // Verificar si el dataset contiene datos válidos
        if (dataset.getRowCount() == 0) {
            JLabel mensajeError = new JLabel("No se pudo generar el gráfico porque no hay datos válidos.");
            mensajeError.setHorizontalAlignment(SwingConstants.CENTER);
            mensajeError.setFont(new Font("Arial", Font.BOLD, 14));
            add(mensajeError);
            return;
        }

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
                "Ingresos por Curso",
                "Cursos",
                "Ingresos ($)",
                dataset
        );

        // Agregar el gráfico al panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        add(chartPanel);
    }
}
