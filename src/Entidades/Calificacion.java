package Entidades;

import java.util.Date;

public class Calificacion {
    private String tipo;
    private double calificacion;


    public Calificacion(String tipo, double calificacion) {
        this.tipo = tipo;
        this.calificacion = calificacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

}
