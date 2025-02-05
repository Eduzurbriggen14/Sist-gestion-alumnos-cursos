package DAO;

import DAO.interfaces.IAlumnoCursoDAO;
import Entidades.AlumnoCurso;
import Entidades.Alumno;
import Entidades.Curso;
import DB.DBConfig;
import Service.AlumnoService;
import Service.CursoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoCursoDAO implements IAlumnoCursoDAO {

    private static final int LIMITE_CURSOS = 3;
    private AlumnoService alumnoService;
    private CursoService cursoService;
    private Connection conn;

    public AlumnoCursoDAO() {
        alumnoService = new AlumnoService();
        cursoService = new CursoService();
        this.conn = DBConfig.getConexion();
    }

    @Override
    public void inscribirAlumno(Alumno alumno, Curso curso) throws DAOException{
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DBConfig.getConexion();

            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos está cerrada o no disponible.");
            }

            connection.setAutoCommit(false);
            int idAlumno = -1;
            int idCurso = -1;
            try {
                idAlumno = alumnoService.recuperIdAlumno(alumno.getNombreUsuario());
                idCurso = cursoService.recuperCursoID(curso.getNombreCurso());
            } catch (Service.ServiceException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }

            System.out.println("id alumno "+ idAlumno);
            System.out.println("id curso "+ idCurso);

            String sql = "INSERT INTO AlumnoCurso (alumno_id, curso_id) VALUES (?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idAlumno );
            stmt.setInt(2, idCurso);

            stmt.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
   /* public void inscribirAlumno(Alumno alumno, Curso curso) throws DAOException {
        try {
            conn.setAutoCommit(false); // Desactivamos el autocommit para manejar transacciones

            int id_alumno = alumnoService.recuperIdAlumno(alumno.getNombreUsuario());
            System.out.println("id alumno " + id_alumno);

            int id_curso = cursoService.recuperCursoID(curso.getNombreCurso());
            System.out.println("id curso " + id_curso);

            if (id_alumno == -1) {
                throw new DAOException("Alumno no encontrado.");
            }

            if (id_curso == -1) {
                throw new DAOException("Curso no encontrado.");
            }

            System.out.println("Alumno y curso encontrados");

            insertarAlumnoCurso(conn, id_alumno, id_curso);
            actualizarCupoCurso(conn, id_curso);

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();  // En caso de error, hacemos rollback de la transacción
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new DAOException("Error al inscribir al alumno en el curso", e);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();  // Cerramos la conexión al final
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/

    private void insertarAlumnoCurso(Connection conn, int id_alumno, int id_curso) throws SQLException {
        String sqlInsert = "INSERT INTO alumno_curso (alumno_id, curso_id) VALUES (?, ?)";
        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            stmtInsert.setInt(1, id_alumno);
            stmtInsert.setInt(2, id_curso);
            stmtInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al insertar el alumno en el curso", e);
        }
    }

    private void actualizarCupoCurso(Connection conn, int id_curso) throws SQLException {
        String sqlUpdateCupo = "UPDATE curso SET cupo = cupo - 1 WHERE id_curso = ?";
        try (PreparedStatement stmtUpdateCupo = conn.prepareStatement(sqlUpdateCupo)) {
            stmtUpdateCupo.setInt(1, id_curso);
            stmtUpdateCupo.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al actualizar el cupo del curso", e);
        }
    }

    @Override
    public void eliminarInscripcion(Alumno alumno, Curso curso) throws DAOException {
        String sqlDelete = "DELETE FROM alumno_curso WHERE nombreUsuario = ? AND nombreCurso = ?";
        String sqlUpdateCupo = "UPDATE curso SET cupo = cupo + 1 WHERE nombreCurso = ?";

        try (Connection conn = DBConfig.getConexion();
             PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
             PreparedStatement psUpdateCupo = conn.prepareStatement(sqlUpdateCupo)) {

            conn.setAutoCommit(false);

            psDelete.setString(1, alumno.getNombreUsuario());
            psDelete.setString(2, curso.getNombreCurso());
            int rowsAffected = psDelete.executeUpdate();

            if (rowsAffected > 0) {
                psUpdateCupo.setString(1, curso.getNombreCurso());
                psUpdateCupo.executeUpdate();
                conn.commit();
                System.out.println("Inscripción eliminada y cupo actualizado.");
            } else {
                conn.rollback();
                System.out.println("No se encontró la inscripción.");
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar la inscripción", e);
        }
    }

    @Override
    public List<AlumnoCurso> obtenerCursosPorAlumno(String nombreUsuario) throws DAOException {
        String sql = "SELECT * FROM alumno_curso WHERE nombreUsuario = ?";
        List<AlumnoCurso> cursos = new ArrayList<>();

        try (Connection conn = DBConfig.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CursoDAO cursoDAO = new CursoDAO();
                Curso curso = cursoDAO.recuperar(rs.getString("nombreCurso"));
                AlumnoDAO alumnoDao = new AlumnoDAO();
                Alumno alumno = alumnoDao.recuperar(nombreUsuario);

                AlumnoCurso alumnoCurso = new AlumnoCurso(alumno, curso);
                alumnoCurso.setCalificacionFinal(rs.getDouble("calificacion_final"));
                cursos.add(alumnoCurso);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al obtener cursos por alumno", e);
        }

        return cursos;
    }
}
