package DAO;

import DAO.interfaces.IProfesorCursoDao;
import DB.DBConfig;
import Entidades.Profesor;
import Entidades.Curso;
import Entidades.ProfesorCurso;
import Service.CursoService;
import Service.ProfesorService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorCursoDao implements IProfesorCursoDao {

    private CursoService cs;
    private ProfesorService ps;
    private Connection connection;

    public ProfesorCursoDao(){
        try {
            String url = "jdbc:mysql://localhost:3306/tp_java";
            String user = "root";
            String password = "Zurdo123";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void asignarCursoAProfesor(String nombreUsuarioProfesor, String nombreCurso, int anio) throws Exception {
        String sql = "INSERT INTO profesor_curso (profesor_id, curso_id, anio) VALUES (?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos está cerrada.");
            }

            // Recuperar el curso y profesor
            Curso curso = new CursoService().recuperarCurso(nombreCurso);
            Profesor profesor = new ProfesorService().recuperar(nombreUsuarioProfesor);

            if (curso == null) {
                throw new Exception("Curso no encontrado: " + nombreCurso);
            }
            if (profesor == null) {
                throw new Exception("Profesor no encontrado: " + nombreUsuarioProfesor);
            }

            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, profesor.getId());
            stmt.setInt(2, curso.getId());
            stmt.setInt(3, anio);

            stmt.executeUpdate();

        } catch (SQLException e) {
            // Log the full stack trace for debugging
            e.printStackTrace();
            throw new Exception("Error al asignar curso al profesor: " + e.getMessage(), e);
        } finally {
            // Close resources properly
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public List<ProfesorCurso> obtenerCursosPorProfesor(String nombreUsuarioProfesor) throws Exception {
        String sql = "SELECT c.nombreCurso, pc.anio " +
                "FROM profesor_curso pc " +
                "JOIN profesor p ON pc.profesor_id = p.id " +
                "JOIN curso c ON pc.curso_id = c.id " +
                "WHERE p.nombreUsuario = ?";

        List<ProfesorCurso> profesorCursos = new ArrayList<>();
        try (Connection connection = DBConfig.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuarioProfesor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Curso curso = new Curso(rs.getString("nombreCurso"));
                    int anio = rs.getInt("anio");
                    ProfesorCurso profesorCurso = new ProfesorCurso(null, curso, anio);
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
        PreparedStatement stmt = null;
        String sql = "DELETE FROM profesor_curso WHERE profesor_id = ? AND curso_id = ?";

        try {

            // Asegúrate de que estas funciones no estén cerrando la conexión
            int idProfesor = obtenerIdProfesorPorNombreUsuario(nombreUsuarioProfesor);
            int idCurso = obtenerIdCursoPorNombre(nombreCurso);
            System.out.println("id profesor " + idProfesor + "\nid curso " + idCurso);
            if (idCurso == -1 || idProfesor == -1){
                throw new DAOException("no se encontro id de curso o profesor");
            }
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idProfesor);
            stmt.setInt(2, idCurso);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new Exception("No se encontró una relación entre el profesor y el curso para eliminar.");
            }
        } catch (SQLException e) {
            // Log the full stack trace for debugging
            e.printStackTrace();
            throw new Exception("Error al eliminar curso del profesor: " + e.getMessage(), e);
        }
    }


    @Override
    public ProfesorCurso obtenerProfesorCursoPorUsuarioYCurso(String nombreUsuarioProfesor, String nombreCurso) throws Exception {
        String sql = "SELECT p.nombre, c.nombreCurso, pc.anio " +
                "FROM profesor_curso pc " +
                "JOIN profesor p ON pc.profesor_id = p.id " +
                "JOIN curso c ON pc.curso_id = c.id " +
                "WHERE p.nombreUsuario = ? AND c.nombreCurso = ?";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuarioProfesor);
            stmt.setString(2, nombreCurso);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Profesor profesor = new Profesor(rs.getString("nombre"), null, rs.getString("nombreUsuario"), null, null);
                    Curso curso = new Curso(rs.getString("nombreCurso"));
                    int anio = rs.getInt("anio");

                    return new ProfesorCurso(profesor, curso, anio);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener el profesor-curso: " + e.getMessage(), e);
        }
        return null;
    }

    private int obtenerIdProfesorPorNombreUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT id FROM profesor WHERE nombreUsuario = ?";
        try (Connection connection = DBConfig.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Profesor no encontrado: " + nombreUsuario);
                }
            }
        }
    }

    private int obtenerIdCursoPorNombre(String nombreCurso) throws SQLException {
        String sql = "SELECT id FROM curso WHERE nombreCurso = ?";
        try (Connection connection = DBConfig.getConexion();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreCurso);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Curso no encontrado: " + nombreCurso);
                }
            }
        }
    }
}
