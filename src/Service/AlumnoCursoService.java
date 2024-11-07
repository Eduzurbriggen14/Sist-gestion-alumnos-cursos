package Service;

import DAO.AlumnoCursoDAO;
import DAO.DAOException;
import Entidades.Alumno;
import Entidades.Curso;
import Entidades.AlumnoCurso;

import java.util.List;

public class AlumnoCursoService {

    private final AlumnoCursoDAO alumnoCursoDAO;

    public AlumnoCursoService() {
        this.alumnoCursoDAO = new AlumnoCursoDAO();
    }

    public void inscribirAlumnoEnCurso(Alumno alumno, Curso curso) throws DAOException {
        alumnoCursoDAO.inscribirAlumno(alumno, curso);
    }
    public void eliminarInscripcion(Alumno alumno, Curso curso) throws DAOException {
        alumnoCursoDAO.eliminarInscripcion(alumno, curso);
    }
    public List<AlumnoCurso> obtenerCursosPorAlumno(String nombreUsuario) throws DAOException {
        return alumnoCursoDAO.obtenerCursosPorAlumno(nombreUsuario);
    }
}