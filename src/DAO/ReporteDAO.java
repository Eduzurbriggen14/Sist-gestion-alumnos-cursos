package DAO;

import DAO.interfaces.IReporteDAO;
import DB.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteDAO implements IReporteDAO {

    @Override
    public List<Map<String, Object>> obtenerReporteCursos() {
        List<Map<String, Object>> reporte = new ArrayList<>();

        String sql = """
        SELECT
            c.id AS curso_id,
            c.nombreCurso AS curso_nombre,
            c.precioCurso,
            a.id AS alumno_id,
            a.nombre AS alumno_nombre,
            c.precioCurso AS total_recaudado
        FROM 
            curso c
        LEFT JOIN 
            inscripciones i ON c.id = i.curso_id
        LEFT JOIN 
            alumno a ON i.alumno_id = a.id
        ORDER BY 
            c.id;
    """;

        try (Connection con = DBConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("cursoId", rs.getInt("curso_id"));
                fila.put("cursoNombre", rs.getString("curso_nombre"));
                fila.put("precioCurso", rs.getDouble("precioCurso"));
                fila.put("alumnoId", rs.getInt("alumno_id"));
                fila.put("alumnoNombre", rs.getString("alumno_nombre"));
                fila.put("totalRecaudado", rs.getDouble("total_recaudado"));
                reporte.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reporte;
    }

    @Override
    public List<Map<String, Object>> obtenerReporteGrafico() {
        List<Map<String, Object>> grafico = new ArrayList<>();

        String sql = """
                SELECT
                c.nombreCurso AS curso_nombre,
                COALESCE(c.precioCurso * COUNT(i.id), 0) AS total_recaudado
                FROM curso c
                LEFT JOIN inscripciones i ON c.id = i.curso_id
                GROUP BY c.nombreCurso, c.precioCurso;
                """;
        try (Connection con = DBConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("cursoNombre", rs.getString("curso_nombre"));
                fila.put("totalRecaudado", rs.getDouble("total_recaudado"));
                grafico.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grafico;
    }
}
