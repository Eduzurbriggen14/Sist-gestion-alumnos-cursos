package Service;

import DAO.CursoDAO;
import DAO.DAOException;
import Entidades.Curso;

import java.util.List;

public class CursoService {


    private CursoDAO cursoDAO;

    public CursoService() {
        this.cursoDAO = new CursoDAO();
    }


    public void guardarCurso(Curso curso) {
        try {
            cursoDAO.guardar(curso);
        } catch (Exception e) {
            System.err.println("Error al guardar el curso: " + e.getMessage());
        }
    }

    public void eliminarCurso(String nombreCurso) {
        try {
            cursoDAO.eliminar(nombreCurso);
        } catch (Exception e) {
            System.err.println("Error al eliminar el curso: " + e.getMessage());
        }
    }

    public void modificarCurso(Curso curso) {
        try {
            cursoDAO.modificar(curso);
        } catch (Exception e) {
            System.err.println("Error al modificar el curso: " + e.getMessage());
        }
    }

    public Curso recuperarCurso(String nombreCurso) {
        try {
            return cursoDAO.recuperar(nombreCurso);
        } catch (Exception e) {
            System.err.println("Error al recuperar el curso: " + e.getMessage());
            return null;
        }
    }

    public List<Curso> recuperarTodosLosCursos() {
        try {
            return cursoDAO.recuperarTodos();
        } catch (Exception e) {
            System.err.println("Error al recuperar todos los cursos: " + e.getMessage());
            return null;
        }
    }

    public int recuperCursoID(String nombreCurso) throws ServiceException {
        try {
            return cursoDAO.recuperarIdCurso(nombreCurso);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.RecuperarPorID: " + e.getMessage());
        }
    }

    public Curso recuperarPorId(int idCurso) throws ServiceException {
        try {
            return cursoDAO.recuperarPorId(idCurso);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.RecuperarPorID: " + e.getMessage());
        }
    }
}

