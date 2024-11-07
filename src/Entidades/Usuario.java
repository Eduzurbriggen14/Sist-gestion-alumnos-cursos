package Entidades;

public abstract class Usuario {

    private String nombre;
    private String passw;
    private String nombreUsuario;
    private String correo;


    public Usuario(String nombre, String passw, String nombreUsuario, String correo) {
        this.nombre = nombre;
        this.passw = passw;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Alumno = "+ this.nombre;
    }
}
