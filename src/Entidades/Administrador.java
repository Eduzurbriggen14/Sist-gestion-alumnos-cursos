package Entidades;

public class Administrador extends Usuario{

    private TipoUsuario tipoUsuario;

    public Administrador(String nombre, String passw, String nombreUsuario, String correo, TipoUsuario tipoUsuario) {
        super(nombre, passw, nombreUsuario, correo);
        this.tipoUsuario = tipoUsuario;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }


    public void inscribirUsuario(Usuario usuario, Curso curso){

    }

    public void crearCurso(Curso curso){

    }
}
