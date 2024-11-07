package Entidades;

import java.util.List;
import java.util.Map;

public class Curso {
    private int id;
    private String nombreCurso;
    private String descripcionCurso;
    private int cupo;
    private double precioCurso;
    private int anio;
    private double notaAprobacion;
    private Promocion promocionCurso;
    private Semestre semestre;
    private Map<Alumno, Double> calificaciones;

    public Curso(String nombreCurso, String descripcionCurso, int cupo, double precioCurso, Semestre semestre, int anio) {
        this.nombreCurso = nombreCurso;
        this.descripcionCurso = descripcionCurso;
        this.cupo = cupo;
        this.precioCurso = precioCurso;
        this.semestre = semestre;
        this.anio = anio;
        this.promocionCurso = null;
    }

    public int getId() {
        return id;
    }

    public Curso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getDescripcionCurso() {
        return descripcionCurso;
    }

    public void setDescripcionCurso(String descripcionCurso) {
        this.descripcionCurso = descripcionCurso;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public double getPrecioCurso() {
        return precioCurso;
    }

    public void setPrecioCurso(double precioCurso) {
        this.precioCurso = precioCurso;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getNotaAprobacion() {
        return notaAprobacion;
    }

    public void setNotaAprobacion(double notaAprobacion) {
        this.notaAprobacion = notaAprobacion;
    }

    public Promocion getPromocionCurso() {
        return promocionCurso;
    }

    public void setPromocionCurso(Promocion promocionCurso) {
        this.promocionCurso = promocionCurso;
    }

    public Map<Alumno, Double> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(Map<Alumno, Double> calificaciones) {
        this.calificaciones = calificaciones;
    }

    @Override
    public String toString() {
        return this.nombreCurso ;
    }
}
