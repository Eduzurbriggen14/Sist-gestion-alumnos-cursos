package Entidades;

public class Promocion {

    private int id;
    private String nombrePromocion;
    private String descripcionPromocion;
    private double descuentoPorPromocion;

    public Promocion(){}

    public Promocion(String nombrePromocion,String descripcionPromocion, double descuentoPorPromocion) {
        this.nombrePromocion = nombrePromocion;
        this.descripcionPromocion = descripcionPromocion;
        this.descuentoPorPromocion = descuentoPorPromocion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombrePromocion() {
        return nombrePromocion;
    }

    public void setNombrePromocion(String nombrePromocion) {
        this.nombrePromocion = nombrePromocion;
    }

    public String getDescripcionPromocion() {
        return descripcionPromocion;
    }

    public void setDescripcionPromocion(String descripcionPromocion) {
        this.descripcionPromocion = descripcionPromocion;
    }

    public double getDescuentoPorPromocion() {
        return descuentoPorPromocion;
    }

    public void setDescuentoPorPromocion(double descuentoPorPromocion) {
        this.descuentoPorPromocion = descuentoPorPromocion;
    }

    @Override
    public String toString() {
        return this.nombrePromocion + "Desc %"+ this.descuentoPorPromocion;
    }
}
