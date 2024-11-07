package DAO;

import Entidades.ProfesorCurso;
import Entidades.Profesor;
import Entidades.Curso;
import Entidades.Semestre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorCursoDao implements IProfesorCursoDao {

    private Connection connection;

    @Override
    public void asignarCursoAProfesor(ProfesorCurso profesorCurso) throws Exception {
        String sql = "INSERT INTO profesor_curso (id_profesor, id_curso, semestre, anio) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Primero obtenemos el id del profesor por su nombre de usuario
            int idProfesor = obtenerIdProfesorPorNombreUsuario(profesorCurso.getProfesor().getNombreUsuario());
            int idCurso = obtenerIdCursoPorNombre(profesorCurso.getCurso().getNombreCurso());

            stmt.setInt(1, idProfesor);
            stmt.setInt(2, idCurso);
            stmt.setString(3, profesorCurso.getSemestre().toString());
            stmt.setInt(4, profesorCurso.getAnio());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al asignar curso al profesor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ProfesorCurso> obtenerCursosPorProfesor(String nombreUsuarioProfesor) throws Exception {
        String sql = "SELECT p.nombre, c.nombreCurso, pc.semestre, pc.anio " +
                "FROM profesor_curso pc " +
                "JOIN profesor p ON pc.id_profesor = p.id " +
                "JOIN curso c ON pc.id_curso = c.id " +
                "WHERE p.nombreUsuario = ?";

        List<ProfesorCurso> profesorCursos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuarioProfesor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Profesor profesor = new Profesor(rs.getString("nombre"), null, rs.getString("nombreUsuario"), null, null);  // Completa los datos si es necesario
                    Curso curso = new Curso(rs.getString("nombreCurso"));
                    Semestre semestre = Semestre.valueOf(rs.getString("semestre"));
                    int anio = rs.getInt("anio");

                    ProfesorCurso profesorCurso = new ProfesorCurso(profesor, curso, semestre, anio);
                    profesorCursos.add(profesorCurso);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener cursos del profesor: " + e.getMessage(), e);
        }
        return profesorCursos;
    }

    @Override
    public void eliminarCursoDeProfesor(String nombreUsuarioProfesor, String nombreCurso) throws Exception {
        String sql = "DELETE FROM profesor_curso WHERE id_profesor = ? AND id_curso = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int idProfesor = obtenerIdProfesorPorNombreUsuario(nombreUsuarioProfesor);
            int idCurso = obtenerIdCursoPorNombre(nombreCurso);

            stmt.setInt(1, idProfesor);
            stmt.setInt(2, idCurso);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al eliminar curso del profesor: " + e.getMessage(), e);
        }
    }

    @Override
    public ProfesorCurso obtenerProfesorCursoPorUsuarioYCurso(String nombreUsuarioProfesor, String nombreCurso) throws Exception {
        String sql = "SELECT p.nombre, c.nombreCurso, pc.semestre, pc.anio " +
                "FROM profesor_curso pc " +
                "JOIN profesor p ON pc.id_profesor = p.id " +
                "JOIN curso c ON pc.id_curso = c.id " +
                "WHERE p.nombreUsuario = ? AND c.nombreCurso = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuarioProfesor);
            stmt.setString(2, nombreCurso);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Profesor profesor = new Profesor(rs.getString("nombre"), null, rs.getString("nombreUsuario"), null, null);
                    Curso curso = new Curso(rs.getString("nombreCurso"));
                    Semestre semestre = Semestre.valueOf(rs.getString("semestre"));
                    int anio = rs.getInt("anio");

                    return new ProfesorCurso(profesor, curso, semestre, anio);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener el profesor-curso: " + e.getMessage(), e);
        }
        return null;
    }

    // Métodos auxiliares para obtener los IDs de Profesor y Curso por nombre
    private int obtenerIdProfesorPorNombreUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT id FROM profesor WHERE nombreUsuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    private int obtenerIdCursoPorNombre(String nombreCurso) throws SQLException {
        String sql = "SELECT id FROM curso WHERE nombreCurso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreCurso);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }
}
