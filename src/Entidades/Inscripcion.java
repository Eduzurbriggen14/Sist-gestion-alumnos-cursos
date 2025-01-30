package Entidades;

public class Inscripcion {
    private int id;
    private String nombreUsuario;
    private String nombreCurso;
    private int anio;
    private Condicion condicion;
    private boolean estadoInscripcion;

    // Constructor, getters y setters
    public Inscripcion(String nombreUsuario, String nombreCurso, int anio) {
        this.nombreUsuario = nombreUsuario;
        this.nombreCurso = nombreCurso;
        this.anio = anio;
    }

    public Inscripcion(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Condicion getCondicion() {
        return condicion;
    }

    public void setCondicion(Condicion condicion) {
        this.condicion = condicion;
    }

    public boolean isEstadoInscripcion() {
        return estadoInscripcion;
    }

    public void setEstadoInscripcion(boolean estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }
}
