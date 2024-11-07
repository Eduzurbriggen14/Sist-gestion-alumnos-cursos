package Service;

import DAO.IProfesorCursoDao;
import DAO.ProfesorCursoDao;
import Entidades.ProfesorCurso;
import java.sql.Connection;
import java.util.List;

public class ProfesorCursoService {

    private IProfesorCursoDao profesorCursoDao;

    // Constructor con la conexión
    public ProfesorCursoService(Connection connection) {
        this.profesorCursoDao = new ProfesorCursoDao();
    }

    // Método para asignar un curso a un profesor
    public void asignarCursoAProfesor(ProfesorCurso profesorCurso) throws Exception {
        profesorCursoDao.asignarCursoAProfesor(profesorCurso);
    }

    // Método para obtener los cursos asignados a un profesor
    public List<ProfesorCurso> obtenerCursosPorProfesor(String nombreUsuarioProfesor) throws Exception {
        return profesorCursoDao.obtenerCursosPorProfesor(nombreUsuarioProfesor);
    }

    // Método para eliminar un curso asignado a un profesor
    public void eliminarCursoDeProfesor(String nombreUsuarioProfesor, String nombreCurso) throws Exception {
        profesorCursoDao.eliminarCursoDeProfesor(nombreUsuarioProfesor, nombreCurso);
    }

    // Método para obtener la asignación de un curso de un profesor específico
    public ProfesorCurso obtenerProfesorCursoPorUsuarioYCurso(String nombreUsuarioProfesor, String nombreCurso) throws Exception {
        return profesorCursoDao.obtenerProfesorCursoPorUsuarioYCurso(nombreUsuarioProfesor, nombreCurso);
    }
}