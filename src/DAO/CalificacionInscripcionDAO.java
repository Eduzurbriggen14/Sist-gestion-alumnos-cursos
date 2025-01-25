package DAO;

import DAO.interfaces.ICalificacionInscripcion;
import Entidades.CalificacionInscripcion;
import Entidades.Inscripcion;
import Entidades.TipoCalificacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalificacionInscripcionDAO implements ICalificacionInscripcion {

    private Connection conexion;

    public CalificacionInscripcionDAO() {
        try {
            String url = "jdbc:mysql://localhost:3306/tp_java";
            String user = "root";
            String password = "Zurdo123";
            conexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean agregarCalificacion(int inscripcion_id, double nota, Date fecha, String tipoNota) {
        String sql = "INSERT INTO calificacion_inscripcion (id_inscripcion, valor_nota, fecha, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, inscripcion_id);
            stmt.setDouble(2, nota);
            stmt.setDate(3, new Date(fecha.getTime()));
            stmt.setString(4, tipoNota);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<CalificacionInscripcion> obtenerCalificacionesPorInscripcion(int idInscripcion) {
        List<CalificacionInscripcion> calificaciones = new ArrayList<>();
        String sql = "SELECT ci.id, ci.valor_nota, ci.fecha, ci.tipo " +
                "FROM calificacion_inscripcion ci " +
                "WHERE ci.id_inscripcion = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idInscripcion);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CalificacionInscripcion calificacion = new CalificacionInscripcion(
                        new Inscripcion(idInscripcion),
                        rs.getDouble("valor_nota"),
                        rs.getDate("fecha"),
                        TipoCalificacion.valueOf(rs.getString("tipo").toUpperCase())
                );
                calificacion.setId(rs.getInt("id"));
                calificaciones.add(calificacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return calificaciones;
    }

}


