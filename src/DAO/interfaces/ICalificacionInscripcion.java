package DAO.interfaces;

import Entidades.CalificacionInscripcion;
import Entidades.Inscripcion;

import java.sql.Date;
import java.util.List;

public interface ICalificacionInscripcion {

    public boolean agregarCalificacion(int inscripcion_id, double nota, Date fecha, String tipoNota);

    public List<CalificacionInscripcion> obtenerCalificacionesPorInscripcion(int idInscripcion);
}
