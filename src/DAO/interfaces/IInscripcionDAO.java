package DAO.interfaces;

import Entidades.Inscripcion;

import java.util.List;

public interface IInscripcionDAO {
    public void insertar(Inscripcion inscripcion);
    public void actualizar(Inscripcion inscripcion);
    public Inscripcion obtener(String nombreUsuario, String nombreCurso);
    public List<Inscripcion> obtenerPorAlumno(String nombreUsuario);
    public List<Inscripcion> obtenerPorCurso(String nombreCurso);
    public List<Inscripcion> obtenerTodos();
    public void actualizarEstadoInscripcion(int id, String estado);
}
