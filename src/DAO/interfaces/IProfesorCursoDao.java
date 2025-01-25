package DAO.interfaces;

import Entidades.ProfesorCurso;

import java.util.List;

public interface IProfesorCursoDao {

    void asignarCursoAProfesor(String nombreUsuarioProfesor, String nombreCurso, int anio) throws Exception;

    List<ProfesorCurso> obtenerCursosPorProfesor(String nombreUsuarioProfesor) throws Exception;

    void eliminarCursoDeProfesor(String nombreUsuarioProfesor, String nombreCurso) throws Exception;

    ProfesorCurso obtenerProfesorCursoPorUsuarioYCurso(String nombreUsuarioProfesor, String nombreCurso) throws Exception;
}