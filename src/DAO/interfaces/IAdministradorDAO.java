package DAO.interfaces;

import DAO.DAOException;
import Entidades.Administrador;

import java.util.List;

public interface IAdministradorDAO {
    public void guardar(Administrador administrador) throws DAOException;
    public void eliminar(String nombreUsuario)throws DAOException;
    public void modificar(Administrador administrador)throws DAOException;
    public Administrador recuperar(String nombreUsuario)throws DAOException;
    public List<Administrador> recuperarTodos()throws DAOException;
}
