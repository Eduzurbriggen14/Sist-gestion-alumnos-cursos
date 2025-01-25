package Entidades;

public class Profesor extends Usuario{
    private int id;
    private TipoUsuario tipoUsuario;

    public Profesor(String nombre, String passw, String nombreUsuario, String correo, TipoUsuario tipoUsuario) {
        super(nombre, passw, nombreUsuario, correo);
        this.tipoUsuario = tipoUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
