package Entidades;

public class UsuarioSesion extends Usuario {
    public static UsuarioSesion usuarioSesionActual;

    private TipoUsuario tipo;

    public UsuarioSesion(String nombre, String passw, String nombreUsuario, String correo, TipoUsuario tipo) {
        super(nombre, passw, nombreUsuario, correo);
        this.tipo = tipo;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }
    public void setTipoPorString(String tipoStr) {
        try {
            this.tipo = TipoUsuario.valueOf(tipoStr);  // Convierte el String al enum TipoUsuario
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Tipo de usuario inválido");
            this.tipo = null;  // O asigna un valor predeterminado o lanza una excepción
        }
    }
    public static void setUsuarioSesion(UsuarioSesion usuario) {
        usuarioSesionActual = usuario;
    }

    public static UsuarioSesion getUsuarioSesion() {
        return usuarioSesionActual;
    }
}
