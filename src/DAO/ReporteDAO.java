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
                    a.abonoAlumno AS abono_alumno,
                    CASE 
                        WHEN a.abonoAlumno = 1 THEN 0 
                        ELSE c.precioCurso
                    END AS total_recaudado,
                    c.promocion_id AS promo
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
                fila.put("abonoAlumno", rs.getInt("abono_alumno"));
                fila.put("totalRecaudado", rs.getDouble("total_recaudado"));

                Integer promocionId = rs.getObject("promo") != null ? rs.getInt("promo") : null;
                fila.put("promocion_id", promocionId);
                System.out.println(fila);

                reporte.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reporte;
    }

}
