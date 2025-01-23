package Service;

import DAO.IProfesorCursoDao;
import DAO.ProfesorCursoDao;
import Entidades.ProfesorCurso;
import java.sql.Connection;
import java.util.List;

public class ProfesorCursoService {

    private IProfesorCursoDao profesorCursoDao;

    public ProfesorCursoService( ) {
        this.profesorCursoDao = new ProfesorCursoDao();
    }

    public void asignarCursoAProfesor(String nombreUsuarioProfesor, String nombreCurso, int anio)throws Exception {
        try {
            profesorCursoDao.asignarCursoAProfesor(nombreUsuarioProfesor, nombreCurso, anio);
        } catch (Exception e) {
            throw new Exception("Error al asignar el curso al profesor: " + e.getMessage(), e);
        }    }

    public List<ProfesorCurso> obtenerCursosPorProfesor(String nombreUsuarioProfesor) throws Exception {
        return profesorCursoDao.obtenerCursosPorProfesor(nombreUsuarioProfesor);
    }

    public void eliminarCursoDeProfesor(String nombreUsuarioProfesor, String nombreCurso) throws Exception {
        profesorCursoDao.eliminarCursoDeProfesor(nombreUsuarioProfesor, nombreCurso);
    }

    public ProfesorCurso obtenerProfesorCursoPorUsuarioYCurso(String nombreUsuarioProfesor, String nombreCurso) throws Exception {
        return profesorCursoDao.obtenerProfesorCursoPorUsuarioYCurso(nombreUsuarioProfesor, nombreCurso);
    }
}