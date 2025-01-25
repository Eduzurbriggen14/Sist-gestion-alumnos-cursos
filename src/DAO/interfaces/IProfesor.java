package DAO.interfaces;

import DAO.DAOException;
import Entidades.Profesor;

import java.util.List;

public interface IProfesor {
    public void guardar(Profesor profe) throws DAOException;
    public void eliminar(String nombreUsuario)throws DAOException;
    public void modificar(Profesor profe)throws DAOException;
    public Profesor recuperar(String nombreUsuario)throws DAOException;
    public List<Profesor> recuperarTodos()throws DAOException;
}
