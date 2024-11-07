package Entidades;

public class Promocion {

    private String nombrePromocion;
    private Semestre semestre;
    private double descuentoPorPromocion;
    private int anio;

    public Promocion(String nombrePromocion, Semestre semestre, double descuentoPorPromocion, int anio) {
        this.nombrePromocion = nombrePromocion;
        this.semestre = semestre;
        this.descuentoPorPromocion = descuentoPorPromocion;
        this.anio = anio;
    }
}
