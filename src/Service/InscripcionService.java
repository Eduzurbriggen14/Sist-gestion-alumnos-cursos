package Service;

import DAO.*;
import Entidades.Alumno;
import Entidades.Curso;
import Entidades.Inscripcion;

import java.util.List;

public class InscripcionService {
    private InscripcionDAO inscripcionDAO;
    private AlumnoDAO alumnoDAO;
    private CursoDAO cursoDAO;

    public InscripcionService(InscripcionDAO inscripcionDAO, AlumnoDAO alumnoDAO, CursoDAO cursoDAO) {
        if (inscripcionDAO == null || alumnoDAO == null || cursoDAO == null) {
            throw new IllegalArgumentException("Los DAOs no pueden ser null");
        }
        this.inscripcionDAO = inscripcionDAO;
        this.alumnoDAO = alumnoDAO;
        this.cursoDAO = cursoDAO;
    }

    // Método para inscribir a un alumno en un curso
    public boolean inscribirAlumnoEnCurso(String nombreUsuario, String nombreCurso, int anio) throws DAOException {
        try {
            if (!puedeInscribirseEnCurso(nombreUsuario, nombreCurso, anio)) {
                return false;
            }
        } catch (DAOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Inscripcion inscripcion = new Inscripcion(nombreUsuario, nombreCurso, anio);
        System.out.println(inscripcion.getNombreCurso());
        inscripcionDAO.insertar(inscripcion);
        return true;
    }

    // Método para verificar si el alumno puede inscribirse en el curso
    public boolean puedeInscribirseEnCurso(String nombreUsuario, String nombreCurso, int anio) throws DAOException {
        // Obtener el alumno
        Alumno alumno = alumnoDAO.recuperar(nombreUsuario);
        Curso curso = cursoDAO.recuperar(nombreCurso);
        System.out.println("cupo en el curso "+nombreCurso+ curso.getCupo());


        // Si el alumno no existe, no puede inscribirse
        if (alumno == null || curso == null) {
            System.out.println("El alumno/curso no existe.");
            return false;
        }
        if (curso.getCupo() < 1){
            System.out.println("No hay cupos");
            return false;
        }
        List<Inscripcion> inscripciones = inscripcionDAO.obtenerPorAlumno(nombreUsuario);
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getNombreCurso().equalsIgnoreCase(nombreCurso) && inscripcion.getAnio()== anio) {
                System.out.println("El alumno ya está inscrito en este curso en el anio: "+ anio);
                return false;
            }

        }

        if (inscripciones.size() >= alumno.getLimiteCursos()) {
            System.out.println("El alumno ha alcanzado el límite de cursos.");
            return false;
        }


        System.out.println("El alumno cumple con los requisitos de inscripción.");
        curso.setCupo(curso.getCupo()- 1);
        cursoDAO.modificar(curso);

        return true;
    }


    public List<Inscripcion> obtenerInscripcionesPorAlumno(String nombreUsuario) {
        return inscripcionDAO.obtenerPorAlumno(nombreUsuario);
    }

    public List<Inscripcion> obtenerInscripcionesPorCurso(String nombreCurso) {
        return inscripcionDAO.obtenerPorCurso(nombreCurso);
    }

    public List<Inscripcion> obtenerTodasLasInscripciones() {
        return inscripcionDAO.obtenerTodos();
    }

    public Inscripcion obtener(String nombreUsuario, String nombreCurso){
        return inscripcionDAO.obtener(nombreUsuario, nombreCurso);
    }

    public void actualizarEstadoInscripcion(int id, String estado){
        inscripcionDAO.actualizarEstadoInscripcion(id, estado);
    }

}
