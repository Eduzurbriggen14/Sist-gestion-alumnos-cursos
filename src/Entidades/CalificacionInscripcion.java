package Entidades;

import java.util.Date;

public class CalificacionInscripcion {
    private int id;
    private Inscripcion inscripcion;
    private double valorNota;
    private Date fecha;
    private TipoCalificacion tipo;

    public CalificacionInscripcion() {}

    public CalificacionInscripcion(Inscripcion inscripcion, double valorNota, Date fecha, TipoCalificacion tipo) {
        this.inscripcion = inscripcion;
        this.valorNota = valorNota;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public double getValorNota() {
        return valorNota;
    }

    public void setValorNota(double valorNota) {
        this.valorNota = valorNota;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public TipoCalificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoCalificacion tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "CalificacionInscripcion{" +
                "id=" + id +
                ", inscripcion=" + inscripcion +
                ", valorNota=" + valorNota +
                ", fecha=" + fecha +
                ", tipo=" + tipo +
                '}';
    }
}
