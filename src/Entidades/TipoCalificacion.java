package Entidades;

public enum TipoCalificacion {
    NOTA1("nota1"),
    NOTA2("nota2"),
    RECUPERATORIO("recuperatorio");

    private final String tipo;

    TipoCalificacion(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return tipo;
    }
}
