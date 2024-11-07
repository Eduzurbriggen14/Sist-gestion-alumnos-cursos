package Service;

import DAO.IUsuarioCursoCalificacionDAO;
import DAO.UsuarioCursoCalificacionDAO;
import Entidades.*;
import java.sql.Connection;
import java.util.List;

public class UsuarioCursoCalificacionService {
    private IUsuarioCursoCalificacionDAO usuarioCursoCalificacionDAO;

    public UsuarioCursoCalificacionService() {
        this.usuarioCursoCalificacionDAO = new UsuarioCursoCalificacionDAO();
    }

    public void asignarCalificacion(UsuarioCursoCalificacion usuarioCursoCalificacion) throws Exception {
        usuarioCursoCalificacionDAO.asignarCalificacion(usuarioCursoCalificacion);
    }

    public UsuarioCursoCalificacion obtenerUsuarioCursoCalificacion(String nombreUsuario, String nombreCurso) throws Exception {
        return usuarioCursoCalificacionDAO.obtenerUsuarioCursoCalificacion(nombreUsuario, nombreCurso);
    }

    public List<Alumno> obtenerAlumnos() throws Exception {
        return usuarioCursoCalificacionDAO.obtenerAlumnos();
    }

    public List<Curso> obtenerCursosPorAlumno(Alumno alumno) throws Exception {
        return usuarioCursoCalificacionDAO.obtenerCursosPorAlumno(alumno);
    }
}
