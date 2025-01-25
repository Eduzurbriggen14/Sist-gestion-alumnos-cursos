package Service;

import DAO.AlumnoDAO;
import DAO.DAOException;
import DAO.interfaces.IAlumnoDao;
import Entidades.Alumno;

import java.util.List;

public class AlumnoService {

    private IAlumnoDao alumnoDAO;

    public AlumnoService() {
        alumnoDAO = new AlumnoDAO();
    }

    public void guardar(Alumno alumno) throws ServiceException {
        try {
            alumnoDAO.guardar(alumno);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.Guardar: " + e.getMessage());
        }
    }

    public void eliminar(String nombreUsuario) throws ServiceException {
        try {
            alumnoDAO.eliminar(nombreUsuario);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.Eliminar: " + e.getMessage());
        }
    }

    public void modificar(Alumno alumno) throws ServiceException {
        try {
            alumnoDAO.modificar(alumno);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.Modificar: " + e.getMessage());
        }
    }

    public Alumno recuperarPorNombreUsuario(String nombreUsuario) throws ServiceException {
        try {
            return alumnoDAO.recuperar(nombreUsuario);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.Recuperar: " + e.getMessage());
        }
    }

    public List<Alumno> recuperarTodos() throws ServiceException {
        try {
            return alumnoDAO.recuperarTodos();
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.RecuperarTodos: " + e.getMessage());
        }
    }

    public int recuperIdAlumno(String nombreUsuario) throws ServiceException{
        try {
            return alumnoDAO.obtenerIdAlumnoPorNombreUsuario(nombreUsuario);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.RecuperarPorID: " + e.getMessage());
        }
    }

    public Alumno recuperarPorId(String nombreUsuario) throws ServiceException {
        try {
            return alumnoDAO.recuperar(nombreUsuario);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.RecuperarPorID: " + e.getMessage());
        }
    }
}
