package Entidades;



public class Profesor extends Usuario{

    private TipoUsuario tipoUsuario;

    public Profesor(String nombre, String passw, String nombreUsuario, String correo, TipoUsuario tipoUsuario) {
        super(nombre, passw, nombreUsuario, correo);
        this.tipoUsuario = tipoUsuario;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
