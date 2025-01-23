package Entidades;

public class Inscripcion {
    private String nombreUsuario;
    private String nombreCurso;
    private int anio;

    // Constructor, getters y setters
    public Inscripcion(String nombreUsuario, String nombreCurso, int anio) {
        this.nombreUsuario = nombreUsuario;
        this.nombreCurso = nombreCurso;
        this.anio = anio;
    }

    // Getters y setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
}
