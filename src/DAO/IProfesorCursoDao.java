package DAO;

import Entidades.ProfesorCurso;
import java.util.List;

public interface IProfesorCursoDao {
    void asignarCursoAProfesor(ProfesorCurso profesorCurso) throws Exception;
    List<ProfesorCurso> obtenerCursosPorProfesor(String nombreUsuarioProfesor) throws Exception;
    void eliminarCursoDeProfesor(String nombreUsuarioProfesor, String nombreCurso) throws Exception;
    ProfesorCurso obtenerProfesorCursoPorUsuarioYCurso(String nombreUsuarioProfesor, String nombreCurso) throws Exception;
}