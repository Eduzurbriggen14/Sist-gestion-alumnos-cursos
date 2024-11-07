package Entidades;

public class Sesion {

    private static UsuarioSesion usuarioSesion;

    // Método para iniciar la sesión (almacena el usuario autenticado)
    public static void iniciarSesion(UsuarioSesion usuario) {
        usuarioSesion = usuario;
    }

    // Método para obtener el usuario de la sesión
    public static UsuarioSesion obtenerUsuarioSesion() {
        return usuarioSesion;
    }

    // Método para obtener el usuario de la sesión (el que te falta)
    public static UsuarioSesion getUsuarioSesion() {
        return usuarioSesion;
    }

    // Método para cerrar sesión
    public static void cerrarSesion() {
        usuarioSesion = null;
    }

    // Método para verificar si hay un usuario autenticado
    public static boolean estaAutenticado() {
        return usuarioSesion != null;
    }
}