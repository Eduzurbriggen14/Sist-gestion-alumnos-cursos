package DAO;

import Entidades.Alumno;
import Entidades.Curso;
import Entidades.UsuarioCursoCalificacion;

import java.util.List;

public interface IUsuarioCursoCalificacionDAO {

    void asignarCalificacion(UsuarioCursoCalificacion usuarioCursoCalificacion) throws Exception;

    UsuarioCursoCalificacion obtenerUsuarioCursoCalificacion(String nombreAlumno, String nombreCurso) throws Exception;

    List<Alumno> obtenerAlumnos() throws Exception;

    List<Curso> obtenerCursosPorAlumno(Alumno alumno) throws Exception;
}