package GUI;

import DAO.DAOException;
import DAO.ReporteDAO;
import Entidades.Promocion;
import Service.PromocionService;
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
        List<Map<String, Object>> reporteDatos = reporteDAO.obtenerReporteCursos();
        setLayout(new BorderLayout());

        if (reporteDatos == null || reporteDatos.isEmpty()) {
            JLabel mensajeError = new JLabel("No hay datos para mostrar en el gráfico.");
            mensajeError.setHorizontalAlignment(SwingConstants.CENTER);
            mensajeError.setFont(new Font("Arial", Font.BOLD, 14));
            add(mensajeError);
            return;
        }

        double totalCursoRecaudado = 0;
        String cursoActual = null;

        // Crear dataset para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> fila : reporteDatos) {
            if (fila.containsKey("cursoNombre") && fila.containsKey("totalRecaudado")) {
                try {
                    String curso = (String) fila.get("cursoNombre");
                    double precioCurso = (double) fila.get("precioCurso");
                    Integer promocionId = (Integer) fila.get("promocion_id");
                    int id_promocion = (promocionId != null) ? promocionId.intValue() : 0;

                    PromocionService promocionService = new PromocionService();
                    Promocion promo = promocionService.obtenerPromocionPorId(id_promocion);

                    Object abonoObject = fila.get("abonoAlumno");
                    boolean tieneAbono = (abonoObject != null && (int) abonoObject == 1);  // Verifica si tiene abono

                    // Si el curso cambia o es el primer curso
                    if (!curso.equals(cursoActual)) {
                        cursoActual = curso;
                        totalCursoRecaudado = 0;
                    }

                    double precioPorAlumno = 0;
                    if (tieneAbono) {
                        precioPorAlumno = 0.0;
                    } else {
                        if (promo != null) {
                            double descuento = promo.getDescuentoPorPromocion();
                            precioPorAlumno = precioCurso * (1 - descuento / 100);
                        } else {
                            precioPorAlumno = precioCurso;
                        }
                    }

                    totalCursoRecaudado += precioPorAlumno;
                    dataset.addValue(totalCursoRecaudado, "Ingresos", curso);

                } catch (ClassCastException | NumberFormatException e) {
                    System.err.println("Error al procesar fila: " + fila);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
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
        add(chartPanel, BorderLayout.CENTER);
    }
}
