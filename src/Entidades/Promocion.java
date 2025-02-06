package Entidades;

import java.time.LocalDate;

public class Promocion {

    private int id;
    private String nombrePromocion;
    private String descripcionPromocion;
    private double descuentoPorPromocion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Promocion() {}

    public Promocion(String nombrePromocion, String descripcionPromocion, double descuentoPorPromocion, LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombrePromocion = nombrePromocion;
        this.descripcionPromocion = descripcionPromocion;
        this.descuentoPorPromocion = descuentoPorPromocion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return (fechaInicio != null && fechaFin != null) && (hoy.isAfter(fechaInicio) || hoy.isEqual(fechaInicio)) && (hoy.isBefore(fechaFin) || hoy.isEqual(fechaFin));
    }

    @Override
    public String toString() {
        return this.nombrePromocion + " - Desc %" + this.descuentoPorPromocion +
                " (Válido del " + fechaInicio + " al " + fechaFin + ")";
    }
}
