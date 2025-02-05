package GUI;

import DAO.DAOException;
import Entidades.Curso;
import Entidades.Promocion;
import Service.CursoService;
import Service.PromocionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AgregarPromocion extends JFrame {
    private JComboBox<Curso> comboCursos;
    private JComboBox<Promocion> comboPromociones;
    private JButton btnCrearPromocion;
    private JButton btnAgregar;
    private JButton btnCancelar;
    private CursoService cursoService;
    private PromocionService promocionService;

    public AgregarPromocion() throws DAOException {
        setTitle("Agregar Promoción a Curso");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        cursoService = new CursoService();
        promocionService = new PromocionService();

        add(new JLabel("Seleccionar Curso:"));
        comboCursos = new JComboBox<>();
        cargarCursos();
        add(comboCursos);

        add(new JLabel("Seleccionar Promoción:"));
        comboPromociones = new JComboBox<>();
        cargarPromociones();
        add(comboPromociones);

        btnCrearPromocion = new JButton("Crear Nueva Promoción");
        add(btnCrearPromocion);

        btnAgregar = new JButton("Agregar");
        btnCancelar = new JButton("Cancelar");
        add(btnAgregar);
        add(btnCancelar);

        btnCrearPromocion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PromocionPanel().setVisible(true);
            }
        });

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Curso cursoSeleccionado = (Curso) comboCursos.getSelectedItem();
                Promocion promoSeleccionada = (Promocion) comboPromociones.getSelectedItem();

                if (cursoSeleccionado == null || promoSeleccionada == null) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un curso y una promoción.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    System.out.println("id curso " + cursoSeleccionado.getId() + " promocion " + promoSeleccionada.getId());
                    boolean tienePromocion = cursoService.recuperarPromocionCurso(cursoSeleccionado.getId());

                    if (tienePromocion) {
                        JOptionPane.showMessageDialog(null, "Este curso ya tiene una promoción asignada.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    cursoService.agregarPromocionACurso(cursoSeleccionado.getId(), promoSeleccionada.getId());
                    JOptionPane.showMessageDialog(null, "Promoción asignada correctamente.");

                } catch (DAOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al asignar la promoción: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminPanel().setVisible(true);
                dispose();
            }
        });
    }

    private void cargarCursos() {
        List<Curso> cursos = cursoService.recuperarTodosLosCursos();
        System.out.println("Cursos cargados: " + cursos.size());

        for (Curso curso : cursos) {
            System.out.println("Curso ID: " + curso.getId());
            comboCursos.addItem(curso);
        }
    }

    private void cargarPromociones() throws DAOException {
        List<Promocion> promociones = promocionService.obtenerTodasLasPromociones();
        System.out.println("Promociones cargadas: " + promociones.size());

        for (Promocion promo : promociones) {
            System.out.println("Promoción ID: " + promo.getId());
            comboPromociones.addItem(promo);
        }
    }
}
