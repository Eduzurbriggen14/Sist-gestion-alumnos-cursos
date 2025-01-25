package Service;

import DAO.DAOException;

import DAO.interfaces.IProfesor;
import DAO.ProfesorDAO;
import Entidades.Profesor;

import java.util.List;

public class ProfesorService {

    private IProfesor profesorDAO;

    public ProfesorService() {
        profesorDAO = new ProfesorDAO();
    }

    public void guardar(Profesor profesor) throws ServiceException {
        try {
            profesorDAO.guardar(profesor);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.Guardar: " + e.getMessage());
        }
    }

    public void eliminar(String nombreUsuario) throws ServiceException {
        try {
            profesorDAO.eliminar(nombreUsuario);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.Eliminar: " + e.getMessage());
        }
    }

    public void modificar(Profesor profesor) throws ServiceException {
        try {
            profesorDAO.modificar(profesor);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.Modificar: " + e.getMessage());
        }
    }

    public Profesor recuperar(String nombreUsuario) throws ServiceException {
        try {
            return profesorDAO.recuperar(nombreUsuario);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.Recuperar: " + e.getMessage());
        }
    }

    public List<Profesor> recuperarTodos() throws ServiceException {
        try {
            return profesorDAO.recuperarTodos();
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.RecuperarTodos: " + e.getMessage());
        }
    }
}
