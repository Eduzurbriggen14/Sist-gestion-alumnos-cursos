package Entidades;


import java.util.List;

public class AlumnoConCalificacion {
    private String nombreUsuario;
    private String nombreCurso;
    private List<CalificacionInscripcion> calificaciones;

    public AlumnoConCalificacion(String nombreUsuario, String nombreCurso ,List<CalificacionInscripcion> calificaciones) {
        this.nombreUsuario = nombreUsuario;
        this.nombreCurso = nombreCurso;
        this.calificaciones = calificaciones;
    }

    // Getters y setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public List<CalificacionInscripcion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<CalificacionInscripcion> calificaciones) {
        this.calificaciones = calificaciones;
    }
}
