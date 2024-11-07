package Service;

import DAO.*;
import Entidades.Administrador;


import java.util.List;

public class AdministradorService {

    private IAdministradorDAO administradorDAO;

    public AdministradorService() {
        administradorDAO = new AdministradorDAO(); //instancia de adm DAO
    }

    public void guardar(Administrador administrador) throws ServiceException {
        try {
            administradorDAO.guardar(administrador);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AdministradorService.Guardar: " + e.getMessage());
        }
    }

    public void eliminar(String nombreUsuario) throws ServiceException {
        try {
            administradorDAO.eliminar(nombreUsuario);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AdministradorService.Eliminar: " + e.getMessage());
        }
    }

    public void modificar(Administrador administrador) throws ServiceException {
        try {
            administradorDAO.modificar(administrador);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AdministradorService.Modificar: " + e.getMessage());
        }
    }

    public Administrador recuperar(String nombreUsuario) throws ServiceException {
        try {
            return administradorDAO.recuperar(nombreUsuario);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AdministradorService.Recuperar: " + e.getMessage());
        }
    }

    public List<Administrador> recuperarTodos() throws ServiceException {
        try {
            return administradorDAO.recuperarTodos();
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AdministradorService.RecuperarTodos: " + e.getMessage());
        }
    }
}
