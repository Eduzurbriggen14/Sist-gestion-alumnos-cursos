package Service;

import DAO.CalificacionInscripcionDAO;
import Entidades.CalificacionInscripcion;
import Entidades.Inscripcion;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class CalificacionInscripcionService {

    private CalificacionInscripcionDAO calificacionInscripcionDAO;

    public CalificacionInscripcionService(){
        this.calificacionInscripcionDAO = new CalificacionInscripcionDAO();
    }

    public boolean agregarCalificacion(int inscripcion_id, double nota, Date fecha, String tipoNota) {
        if (inscripcion_id < 1 || fecha == null || tipoNota == null || tipoNota.isEmpty()) {
            throw new IllegalArgumentException("No se proveen todos los parametros");
        }
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10.");
        }
        return calificacionInscripcionDAO.agregarCalificacion(inscripcion_id, nota, fecha, tipoNota);
    }

    public List<CalificacionInscripcion> obtenerCalificacionesPorInscripcion(int idInscripcion){

        if (idInscripcion == 0) {
            throw new RuntimeException("El id de inscripción es 0, lo que indica que la inscripción no se ha encontrado correctamente.");
        }
        return calificacionInscripcionDAO.obtenerCalificacionesPorInscripcion(idInscripcion);
    }
}
