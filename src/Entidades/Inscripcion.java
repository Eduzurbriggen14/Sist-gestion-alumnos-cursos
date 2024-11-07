package Entidades;

public class Inscripcion {
    private String nombreUsuario;
    private String nombreCurso;
    private double calificacionFinal;

    // Constructor, getters y setters
    public Inscripcion(String nombreUsuario, String nombreCurso, double calificacionFinal) {
        this.nombreUsuario = nombreUsuario;
        this.nombreCurso = nombreCurso;
        this.calificacionFinal = calificacionFinal;
    }

    // Getters y setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public double getCalificacionFinal() {
        return calificacionFinal;
    }


    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public void setCalificacionFinal(double calificacionFinal) {
        this.calificacionFinal = calificacionFinal;
    }

    public boolean isAprobado() {
        if(calificacionFinal >=6){
            return true;
        }else{
            return false;
        }
    }
}
