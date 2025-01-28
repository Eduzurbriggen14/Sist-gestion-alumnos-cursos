package DAO;

import DAO.interfaces.IInscripcionDAO;
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


    public InscripcionDAO() {
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
    public void insertar(Inscripcion inscripcion) {
        int idAlumno = obtenerIdAlumno(inscripcion.getNombreUsuario());
        int idCurso = obtenerIdCurso(inscripcion.getNombreCurso());

        System.out.println("ID Alumno: " + idAlumno);
        System.out.println("ID Curso: " + idCurso);
        if (idAlumno == -1 || idCurso == -1) {
            System.out.println("Error: Alumno o curso no encontrado.");
            return;
        }

        String sql = "INSERT INTO inscripciones (alumno_id, curso_id, anio) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, idCurso);
            stmt.setInt(3, inscripcion.getAnio());
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

        String sql = "UPDATE inscripciones SET anio = ? WHERE alumno_id = ? AND curso_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, inscripcion.getAnio());
            stmt.setInt(2, idAlumno);
            stmt.setInt(3, idCurso);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Inscripcion obtener(String nombreUsuario, String nombreCurso) {
        int idAlumno = obtenerIdAlumno(nombreUsuario);
        int idCurso = obtenerIdCurso(nombreCurso);

        System.out.println("id alumno-- "+ idAlumno + "\nid curso ---" + idCurso);

        if (idAlumno == -1 || idCurso == -1) {
            System.out.println("Error: Alumno o curso no encontrado.");
            return null;
        }

        String sql = "SELECT * FROM inscripciones WHERE alumno_id = ? AND curso_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, idCurso);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Inscripcion insc = new Inscripcion(
                        nombreUsuario,
                        nombreCurso,
                        rs.getInt("anio")
                );
                insc.setId(rs.getInt("id"));
                return insc;

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
                        rs.getInt("anio")
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
                Inscripcion inscripto = new Inscripcion(nUsuario, nCurso, rs.getInt("anio"));
                inscripto.setId(rs.getInt("id"));
                inscripciones.add(inscripto);
            }
        } catch (SQLException | DAOException e) {
            e.printStackTrace();
        }
        return inscripciones;
    }

    public void actualizarEstadoInscripcion(int id, String estado) {
        String sql = "UPDATE inscripciones SET condicion = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        rs.getInt("anio")
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
