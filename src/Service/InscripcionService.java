package Service;

import DAO.*;
import Entidades.Alumno;
import Entidades.Inscripcion;

import java.util.List;

public class InscripcionService {
    private InscripcionDAO inscripcionDAO;
    private AlumnoDAO alumnoDAO;
    private CursoDAO cursoDAO;

    // Constructor con validación para asegurarse de que los DAOs no sean null
    public InscripcionService(InscripcionDAO inscripcionDAO, AlumnoDAO alumnoDAO, CursoDAO cursoDAO) {
        if (inscripcionDAO == null || alumnoDAO == null || cursoDAO == null) {
            throw new IllegalArgumentException("Los DAOs no pueden ser null");
        }
        this.inscripcionDAO = inscripcionDAO;
        this.alumnoDAO = alumnoDAO;
        this.cursoDAO = cursoDAO;
    }

    // Método para inscribir a un alumno en un curso
    public boolean inscribirAlumnoEnCurso(String nombreUsuario, String nombreCurso, double calificacionFinal) throws DAOException {
        try {
            if (!puedeInscribirseEnCurso(nombreUsuario, nombreCurso)) {
                return false;
            }
        } catch (DAOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Inscripcion inscripcion = new Inscripcion(nombreUsuario, nombreCurso, calificacionFinal);
        System.out.println(inscripcion.getNombreCurso());
        inscripcionDAO.insertar(inscripcion);
        return true;
    }

    // Método para verificar si el alumno puede inscribirse en el curso
    public boolean puedeInscribirseEnCurso(String nombreUsuario, String nombreCurso) throws DAOException {
        // Obtener el alumno
        Alumno alumno = alumnoDAO.recuperar(nombreUsuario);
        System.out.println("alumno en puede inscribirse???"+alumno);

        // Si el alumno no existe, no puede inscribirse
        if (alumno == null) {
            System.out.println("El alumno no existe.");
            return false;
        }


        List<Inscripcion> inscripciones = inscripcionDAO.obtenerPorAlumno(nombreUsuario);
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getNombreCurso().equalsIgnoreCase(nombreCurso)) {
                System.out.println("El alumno ya está inscrito en este curso.");
                return false;
            }
        }

        if (inscripciones.size() >= alumno.getLimiteCursos()) {
            System.out.println("El alumno ha alcanzado el límite de cursos.");
            return false;
        }
        System.out.println("El alumno no cumple con los requisitos de inscripción.");
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

    // Método para generar reporte de inscripciones
    public void generarReporteInscripciones() {
        List<Inscripcion> inscripciones = obtenerTodasLasInscripciones();
        System.out.println("Reporte de Inscripciones:");
        for (Inscripcion inscripcion : inscripciones) {
            System.out.println("Alumno: " + inscripcion.getNombreUsuario() + ", Curso: " + inscripcion.getNombreCurso() + ", Calificación Final: " + inscripcion.getCalificacionFinal() + ", Aprobado: " + (inscripcion.isAprobado() ? "Sí" : "No"));
        }
    }
}
