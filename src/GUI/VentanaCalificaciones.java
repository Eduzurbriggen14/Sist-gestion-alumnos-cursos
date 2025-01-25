package GUI;

import DAO.InscripcionDAO;
import Entidades.Inscripcion;
import Service.CalificacionInscripcionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Date;

public class VentanaCalificaciones extends JFrame {

    private JComboBox<String> comboTipoCalificacion;
    private JSpinner spinnerValorNota;
    private JSpinner spinnerFecha;
    private JButton btnGuardar;
    private int idAlumno;
    private String nombreUsuario;
    private String nombreCurso;
    private Connection conexion;

    public VentanaCalificaciones(int idAlumno, String nombreUsuario, String nombreCurso) {
        this.idAlumno = idAlumno;
        this.nombreUsuario = nombreUsuario;
        this.nombreCurso = nombreCurso;
        this.conexion = conexion;

        setTitle("Agregar Calificación para " + nombreUsuario);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tipo de calificación
        panel.add(new JLabel("Tipo de Calificación:"));
        comboTipoCalificacion = new JComboBox<>(new String[]{"nota1", "nota2", "recuperatorio"});
        panel.add(comboTipoCalificacion);

        // Valor de la calificación
        panel.add(new JLabel("Valor de la Nota:"));
        spinnerValorNota = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10.0, 0.1));
        panel.add(spinnerValorNota);

        panel.add(new JLabel("Fecha:"));
        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd"));
        panel.add(spinnerFecha);

        // Botón guardar
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCalificacion();
            }
        });

        panel.add(btnGuardar);

        // Layout principal
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    private void guardarCalificacion() {
        // Obtener datos de los campos
        String tipo = (String) comboTipoCalificacion.getSelectedItem();
        double valorNota = (double) spinnerValorNota.getValue();
        Date fecha = (Date) spinnerFecha.getValue();
        java.sql.Date fechaSql = new java.sql.Date(fecha.getTime());

        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        Inscripcion insc = inscripcionDAO.obtener(nombreUsuario, nombreCurso);

        int id_inscriopcion = insc.getId();
        System.out.println("id inscripcion -----"+ id_inscriopcion);

        CalificacionInscripcionService calificacionInscripcionService = new CalificacionInscripcionService();
        boolean res = calificacionInscripcionService.agregarCalificacion(id_inscriopcion,valorNota, fechaSql, tipo);

        System.out.println("Guardando calificación...");
        System.out.println("Alumno ID: " + idAlumno);
        System.out.println("Tipo: " + tipo);
        System.out.println("Nota: " + valorNota);
        System.out.println("Fecha: " + fecha);

        JOptionPane.showMessageDialog(this, "Calificación guardada con éxito.");
        dispose(); // Cierra la ventana
    }
}
