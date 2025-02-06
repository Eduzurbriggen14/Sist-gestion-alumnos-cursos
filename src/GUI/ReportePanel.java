package GUI;

import DAO.DAOException;
import Entidades.Promocion;
import Service.PromocionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportePanel extends JPanel {

    public ReportePanel(List<Map<String, Object>> reporteDatos) throws DAOException {
        setLayout(new BorderLayout());

        JPanel panelTablas = new JPanel();
        panelTablas.setLayout(new BoxLayout(panelTablas, BoxLayout.Y_AXIS));

        double totalGeneralRecaudado = 0;
        String cursoActual = null;
        DefaultTableModel modeloTabla = null;
        double totalCursoRecaudado = 0;

        for (Map<String, Object> fila : reporteDatos) {
            String curso = (String) fila.get("cursoNombre");
            String alumno = (fila.get("alumnoNombre") != null) ? (String) fila.get("alumnoNombre") : null;
            double precioCurso = (double) fila.get("precioCurso");
            Integer promocionId = (Integer) fila.get("promocion_id");
            int id_promocion = (promocionId != null) ? promocionId.intValue() : 0;
            System.out.println("promocion id: " + id_promocion);

            PromocionService promocionService = new PromocionService();
            Promocion promo = promocionService.obtenerPromocionPorId(id_promocion);


            Object abonoObject = fila.get("abonoAlumno");
            boolean tieneAbono = (abonoObject != null && (int) abonoObject == 1);  // Verifica si tiene abono


            // Si el curso cambia o es el primer curso
            if (!curso.equals(cursoActual)) {
                if (modeloTabla != null) {
                    agregarTablaAlPanel(panelTablas, cursoActual, modeloTabla, totalCursoRecaudado);
                }
                modeloTabla = new DefaultTableModel(
                        new Object[]{"Alumno", "Precio por Alumno"}, 0
                );

                cursoActual = curso;
                totalCursoRecaudado = 0;
            }

            double precioPorAlumno;
            LocalDate fechaActual = LocalDate.now();
            if (alumno != null) {
                if (tieneAbono){
                    precioPorAlumno = 0.0;
                }
                else{
                    if (promo != null){
                        LocalDate fechaInicio = promo.getFechaInicio();
                        LocalDate fechaFin = promo.getFechaFin();
                        if ((fechaActual.isEqual(fechaInicio) || fechaActual.isAfter(fechaInicio)) &&
                                (fechaActual.isEqual(fechaFin) || fechaActual.isBefore(fechaFin))) {

                            double descuento = promo.getDescuentoPorPromocion();
                            precioPorAlumno = precioCurso * (1 - descuento / 100);
                        } else {
                            precioPorAlumno = precioCurso;
                        }
                    } else {
                        precioPorAlumno = precioCurso;
                    }
                }
                modeloTabla.addRow(new Object[]{alumno, precioPorAlumno});
                totalCursoRecaudado += precioPorAlumno;
                totalGeneralRecaudado += precioPorAlumno;
            }
        }

        // Agregar la última tabla si existe
        if (modeloTabla != null) {
            agregarTablaAlPanel(panelTablas, cursoActual, modeloTabla, totalCursoRecaudado);
        }

        // Agregar el panel de tablas al panel principal
        add(new JScrollPane(panelTablas), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BorderLayout());

        // Mostrar el total general recaudado
        JLabel lblTotalGeneral = new JLabel("Total General Recaudado: $" + totalGeneralRecaudado);
        lblTotalGeneral.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalGeneral.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTotalGeneral, BorderLayout.NORTH);

        JButton btnVerGrafico = new JButton("Ver gráfico");
        btnVerGrafico.addActionListener(e -> {
            // Aquí llamamos al método para mostrar el gráfico
            mostrarGrafico();
        });
        panelInferior.add(btnVerGrafico, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);


    }

    private void agregarTablaAlPanel(JPanel panelTablas, String curso, DefaultTableModel modeloTabla, double totalCursoRecaudado) {
        JLabel tituloCurso = new JLabel("Curso: " + curso);
        tituloCurso.setFont(new Font("Arial", Font.BOLD, 16));
        tituloCurso.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTable tablaCurso = new JTable(modeloTabla);

        tablaCurso.setRowHeight(30);
        int filaCount = modeloTabla.getRowCount();
        tablaCurso.setPreferredScrollableViewportSize(new Dimension(600, 30 * filaCount));
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
    public static void mostrarReporteEnVentana(List<Map<String, Object>> reporteDatos) throws DAOException {
        JFrame frameReporte = new JFrame("Reporte de Cursos");
        frameReporte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameReporte.setSize(800, 600);
        frameReporte.add(new ReportePanel(reporteDatos));
        frameReporte.setVisible(true);
    }

    private void mostrarGrafico() {
        JFrame frameGrafico = new JFrame("Gráfico de Barras");
        frameGrafico.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameGrafico.setSize(800, 600);
        frameGrafico.add(new GraficoReporte());
        frameGrafico.setVisible(true);
    }
}
