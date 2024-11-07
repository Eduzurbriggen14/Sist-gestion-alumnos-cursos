package DAO;

import Entidades.AlumnoCurso;
import Entidades.Alumno;
import Entidades.Curso;

import java.sql.Date;
import java.util.List;

public interface IAlumnoCursoDAO {
    void inscribirAlumno(Alumno alumno, Curso curso) throws DAOException;
    void eliminarInscripcion(Alumno alumno, Curso curso) throws DAOException;
    List<AlumnoCurso> obtenerCursosPorAlumno(String nombreUsuario) throws DAOException;
}
