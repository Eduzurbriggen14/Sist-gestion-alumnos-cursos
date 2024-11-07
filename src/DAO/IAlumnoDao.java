package DAO;

import Entidades.Alumno;
import Entidades.Usuario;

import java.util.List;

public interface IAlumnoDao {
    public void guardar(Alumno alumno) throws DAOException;
    public void eliminar(String nombreUsuario)throws DAOException;
    void modificar(Alumno alumno) throws DAOException;
    public Alumno recuperar(String nombreUsuario)throws DAOException;
    public List<Alumno> recuperarTodos()throws DAOException;
    public int obtenerIdAlumnoPorNombreUsuario(String nombreUsuario) throws DAOException;
    public Alumno recuperarPorId(int id) throws DAOException;
}
