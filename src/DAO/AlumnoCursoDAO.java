package DAO;

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

    // Constructor de la clase donde se inicializa la conexión
    public AlumnoCursoDAO() {
        alumnoService = new AlumnoService();
        cursoService = new CursoService();
        this.conn = DBConfig.getConexion();
    }

    @Override
    public void inscribirAlumno(Alumno alumno, Curso curso) throws DAOException{
        Connection connection = null; // Declarar la conexión
        PreparedStatement stmt = null; // Declarar el statement para ejecutar consultas

        try {
            // Obtener la conexión a la base de datos
            connection = DBConfig.getConexion();

            // Verifica si la conexión no es nula y está abierta
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos está cerrada o no disponible.");
            }

            connection.setAutoCommit(false); // Desactivar autocommit para controlar las transacciones manualmente
            int idAlumno = -1;
            int idCurso = -1;
            try {
                idAlumno = alumnoService.recuperIdAlumno(alumno.getNombreUsuario());
                idCurso = cursoService.recuperCursoID(curso.getNombreCurso());
            } catch (Service.ServiceException e) {
                // Handle the exception appropriately (logging, user feedback, etc.)
                System.out.println("An error occurred: " + e.getMessage());
            }

            System.out.println("id alumno "+ idAlumno);
            System.out.println("id curso "+ idCurso);

            // Crear la consulta para insertar al alumno en el curso
            String sql = "INSERT INTO AlumnoCurso (alumno_id, curso_id) VALUES (?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idAlumno ); // Asignar el ID del alumno
            stmt.setInt(2, idCurso); // Asignar el ID del curso

            // Ejecutar la consulta de inserción
            stmt.executeUpdate();

            // Hacer commit de la transacción
            connection.commit();

        } catch (SQLException e) {
            // Si ocurre un error, hacer rollback
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    // Manejar el error del rollback (si ocurre)
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

            conn.commit();  // Si todo va bien, confirmamos la transacción
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
            stmtInsert.executeUpdate();  // Aquí puede lanzar SQLException
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al insertar el alumno en el curso", e);
        }
    }

    private void actualizarCupoCurso(Connection conn, int id_curso) throws SQLException {
        String sqlUpdateCupo = "UPDATE curso SET cupo = cupo - 1 WHERE id_curso = ?";
        try (PreparedStatement stmtUpdateCupo = conn.prepareStatement(sqlUpdateCupo)) {
            stmtUpdateCupo.setInt(1, id_curso);
            stmtUpdateCupo.executeUpdate();  // Aquí también puede lanzar SQLException
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

            conn.setAutoCommit(false); // Desactivamos el autocommit para manejar transacciones

            psDelete.setString(1, alumno.getNombreUsuario());
            psDelete.setString(2, curso.getNombreCurso());
            int rowsAffected = psDelete.executeUpdate();  // Aquí puede lanzar SQLException

            if (rowsAffected > 0) {
                // Incrementar el cupo del curso
                psUpdateCupo.setString(1, curso.getNombreCurso());
                psUpdateCupo.executeUpdate();  // Aquí también puede lanzar SQLException
                conn.commit();  // Confirmamos la transacción
                System.out.println("Inscripción eliminada y cupo actualizado.");
            } else {
                conn.rollback();  // Hacemos rollback si no se encuentra la inscripción
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
            ResultSet rs = ps.executeQuery();  // Aquí puede lanzar SQLException

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
