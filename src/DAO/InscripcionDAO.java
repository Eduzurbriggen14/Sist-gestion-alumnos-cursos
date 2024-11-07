package DAO;

import Entidades.Alumno;
import Entidades.Curso;
import Entidades.Inscripcion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO implements IInscripcionDAO {
    private Connection connection;
    private AlumnoDAO alu;
    private CursoDAO c;

    // Constructor donde se inicializa la conexión
    public InscripcionDAO() {
        try {
            // Inicializa la conexión con la base de datos
            String url = "jdbc:mysql://localhost:3306/tp_java"; // Reemplaza con la URL de tu DB
            String user = "root"; // Reemplaza con tu usuario de DB
            String password = "Zurdo123"; // Reemplaza con tu contraseña de DB
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertar(Inscripcion inscripcion) {
        // Obtiene los idAlumno y idCurso a partir de los nombres
        int idAlumno = obtenerIdAlumno(inscripcion.getNombreUsuario());
        int idCurso = obtenerIdCurso(inscripcion.getNombreCurso());
        System.out.println("ID Alumno: " + idAlumno);
        System.out.println("ID Curso: " + idCurso);
        if (idAlumno == -1 || idCurso == -1) {
            System.out.println("Error: Alumno o curso no encontrado.");
            return;
        }

        // Inserta en la base de datos utilizando los idAlumno y idCurso
        String sql = "INSERT INTO inscripciones (alumno_id, curso_id, calificacion) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);  // ID del alumno
            stmt.setInt(2, idCurso);   // ID del curso
            stmt.setDouble(3, inscripcion.getCalificacionFinal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Inscripcion inscripcion) {
        int idAlumno = obtenerIdAlumno(inscripcion.getNombreUsuario());
        int idCurso = obtenerIdCurso(inscripcion.getNombreCurso());
        System.out.println("ID Alumno: " + idAlumno);
        System.out.println("ID Curso: " + idCurso);
        if (idAlumno == -1 || idCurso == -1) {
            System.out.println("Error: Alumno o curso no encontrado.");
            return;
        }

        String sql = "UPDATE inscripciones SET calificacion = ? WHERE alumno_id = ? AND curso_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, inscripcion.getCalificacionFinal());
            stmt.setInt(2, idAlumno);  // ID del alumno
            stmt.setInt(3, idCurso);   // ID del curso
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Inscripcion obtener(String nombreUsuario, String nombreCurso) {
        int idAlumno = obtenerIdAlumno(nombreUsuario);
        int idCurso = obtenerIdCurso(nombreCurso);

        if (idAlumno == -1 || idCurso == -1) {
            System.out.println("Error: Alumno o curso no encontrado.");
            return null;
        }

        String sql = "SELECT * FROM inscripciones WHERE alumno_id = ? AND curso_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);  // ID del alumno
            stmt.setInt(2, idCurso);   // ID del curso
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Inscripcion(
                        rs.getString("nombreUsuario"),
                        rs.getString("nombreCurso"),
                        rs.getDouble("calificacion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Inscripcion> obtenerPorAlumno(String nombreUsuario) {
        List<Inscripcion> inscripciones = new ArrayList<>();
        int idAlumno = obtenerIdAlumno(nombreUsuario);

        if (idAlumno == -1) {
            System.out.println("Error: Alumno no encontrado.");
            return inscripciones;
        }

        String sql = "SELECT * FROM inscripciones WHERE alumno_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                alu = new AlumnoDAO();
                c = new CursoDAO();
                Alumno alumno = alu.recuperarPorId(rs.getInt("alumno_id"));
                Curso curso = c.recuperarPorId(rs.getInt("curso_id"));

                String nUsuario = alumno.getNombreUsuario();
                String nombreCurso = curso.getNombreCurso();
                inscripciones.add(new Inscripcion(
                        nUsuario,
                        nombreCurso,
                        rs.getDouble("calificacion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return inscripciones;
    }

    @Override
    public List<Inscripcion> obtenerPorCurso(String nombreCurso) {
        List<Inscripcion> inscripciones = new ArrayList<>();
        int idCurso = obtenerIdCurso(nombreCurso);

        if (idCurso == -1) {
            System.out.println("Error: Curso no encontrado.");
            return inscripciones;
        }

        // Sentencia para obtener inscripciones por ID del curso
        String sql = "SELECT * FROM inscripciones WHERE curso_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);  // ID del curso
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                alu = new AlumnoDAO();
                c = new CursoDAO();
                Alumno alumno = alu.recuperarPorId(rs.getInt("alumno_id"));
                Curso curso = c.recuperarPorId(rs.getInt("curso_id"));

                String nUsuario = alumno.getNombreUsuario();
                String nCurso = curso.getNombreCurso();
                inscripciones.add(new Inscripcion(
                        nUsuario,
                        nCurso,
                        rs.getDouble("calificacionFinal")
                ));
            }
        } catch (SQLException | DAOException e) {
            e.printStackTrace();
        }
        return inscripciones;
    }

    @Override
    public List<Inscripcion> obtenerTodos() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = "SELECT * FROM inscripciones";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int idAlumno = rs.getInt("alumno_id");
                int idCurso = rs.getInt("curso_id");
                double calificacion = rs.getDouble("calificacion");

                // Aquí puedes usar los IDs para obtener los nombres del alumno y el curso
                alu = new AlumnoDAO();
                c = new CursoDAO();

                Alumno alumno = alu.recuperarPorId(idAlumno);
                Curso curso = c.recuperarPorId(idCurso);

                String nombreUsuario = alumno.getNombre();
                String nombreCurso = curso.getNombreCurso();

                inscripciones.add(new Inscripcion(
                        nombreUsuario,
                        nombreCurso,
                        calificacion
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return inscripciones;
    }

    private int obtenerIdAlumno(String nombreUsuario) {
        String sql = "SELECT id FROM alumno WHERE nombreUsuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int obtenerIdCurso(String nombreCurso) {
        String sql = "SELECT id FROM curso WHERE nombreCurso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreCurso);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
