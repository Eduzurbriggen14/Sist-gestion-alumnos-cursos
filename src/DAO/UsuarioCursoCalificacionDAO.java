package DAO;

import DB.DBConfig;
import Entidades.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioCursoCalificacionDAO implements IUsuarioCursoCalificacionDAO {
    private Connection connection;


    @Override
    public void asignarCalificacion(UsuarioCursoCalificacion usuarioCursoCalificacion) throws Exception {
        String sql = "INSERT INTO usuario_curso_calificacion (nombreAlumno, nombreCurso, nota1, nota2, recuperatorio) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuarioCursoCalificacion.getAlumno().getNombreUsuario());
            stmt.setString(2, usuarioCursoCalificacion.getCurso().getNombreCurso());

            // Inicializar valores de notas
            double nota1 = 0;
            double nota2 = 0;
            double recuperatorio = 0;

            for (Calificacion calificacion : usuarioCursoCalificacion.getCalificaciones()) {
                switch (calificacion.getTipo()) {
                    case "nota1":
                        nota1 = calificacion.getCalificacion();
                        break;
                    case "nota2":
                        nota2 = calificacion.getCalificacion();
                        break;
                    case "recuperatorio":
                        recuperatorio = calificacion.getCalificacion();
                        break;
                }
            }

            stmt.setDouble(3, nota1);
            stmt.setDouble(4, nota2);
            stmt.setDouble(5, recuperatorio);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al asignar calificación: " + e.getMessage(), e);
        }
    }

    @Override
    public UsuarioCursoCalificacion obtenerUsuarioCursoCalificacion(String nombreUsuario, String nombreCurso) throws Exception {
        String sql = "SELECT nota1, nota2, recuperatorio FROM usuario_curso_calificacion WHERE nombreUsuario = ? AND nombreCurso = ?";

        String sqlAlumno = "SELECT nombre FROM alumno WHERE nombreUsuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, nombreCurso);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Obtener el nombre del alumno
                    String nombreAlumno;
                    try (PreparedStatement stmtAlumno = connection.prepareStatement(sqlAlumno)) {
                        stmtAlumno.setString(1, nombreUsuario);
                        try (ResultSet rsAlumno = stmtAlumno.executeQuery()) {
                            if (rsAlumno.next()) {
                                nombreAlumno = rsAlumno.getString("nombre");
                            } else {
                                throw new Exception("No se encontró el alumno con el nombre de usuario especificado.");
                            }
                        }
                    }

                    Alumno alumno = new Alumno(nombreAlumno, null, nombreUsuario, null, TipoUsuario.ALUMNO, false);
                    Curso curso = new Curso(nombreCurso);
                    UsuarioCursoCalificacion usuarioCursoCalificacion = new UsuarioCursoCalificacion(alumno, curso);

                    List<Calificacion> calificaciones = new ArrayList<>();
                    calificaciones.add(new Calificacion("nota1", rs.getDouble("nota1")));
                    calificaciones.add(new Calificacion("nota2", rs.getDouble("nota2")));
                    calificaciones.add(new Calificacion("recuperatorio", rs.getDouble("recuperatorio")));

                    usuarioCursoCalificacion.setCalificaciones(calificaciones);

                    usuarioCursoCalificacion.setCondicion(usuarioCursoCalificacion.determinarCondicion());

                    return usuarioCursoCalificacion;
                } else {
                    throw new Exception("No se encontró el registro para el alumno y curso especificados.");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener el registro de usuario-curso-calificación: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Alumno> obtenerAlumnos() throws Exception {
        String sql = "SELECT nombreUsuario, nombre FROM alumnos";
        List<Alumno> alumnos = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nombreUsuario = rs.getString("nombreUsuario");
                String nombre = rs.getString("nombre");


                Alumno alumno = new Alumno(nombre, null, nombreUsuario, null, TipoUsuario.ALUMNO, false);
                alumnos.add(alumno);
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener alumnos: " + e.getMessage(), e);
        }
        return alumnos;
    }

    @Override
    public List<Curso> obtenerCursosPorAlumno(Alumno alumno) throws Exception {
        String sql = "SELECT nombreCurso FROM usuario_curso_calificacion WHERE nombreAlumno = ?";
        List<Curso> cursos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombreUsuario());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cursos.add(new Curso(rs.getString("nombreCurso")));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener cursos para el alumno: " + e.getMessage(), e);
        }
        return cursos;
    }
}
