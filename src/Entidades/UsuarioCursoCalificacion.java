package Entidades;

import java.util.ArrayList;
import java.util.List;

public class UsuarioCursoCalificacion {
    private Alumno alumno;
    private Curso curso;
    private List<Calificacion> calificaciones;
    private Condicion condicion;

    public UsuarioCursoCalificacion(Alumno alumno, Curso curso) {
        this.alumno = alumno;
        this.curso = curso;
        this.calificaciones = new ArrayList<>();
        this.condicion = determinarCondicion();
    }

    public void agregarCalificacion(Calificacion calificacion) {
        calificaciones.add(calificacion);
        condicion = determinarCondicion();
    }

    public Condicion determinarCondicion() {
        double nota1 = 0;
        double nota2 = 0;
        double recuperatorio = 0;

        for (Calificacion calificacion : calificaciones) {
            switch (calificacion.getTipo()) {
                case "nota1":
                    nota1 = calificacion.getCalificacion();
                    break;
                case "nota2":
                    nota2 = calificacion.getCalificacion();
                    break;
                case "recuperatorio":
                    recuperatorio = calificacion.getCalificacion();
                    break;
            }
        }

        if (nota1 == 0 || nota2 == 0) {
            return Condicion.CURSANDO;
        }

        if (nota1 >= 6 && nota2 >= 6) {
            return Condicion.APROBADO;
        } else if (nota1 < 6 || nota2 < 6) {
            if (recuperatorio >= 6) {
                return Condicion.APROBADO;
            } else {
                return Condicion.DESAPROBADO;
            }
        }
        return Condicion.DESAPROBADO;
    }

    public Alumno getAlumno () {
        return alumno;
    }

    public void setAlumno (Alumno alumno){
        this.alumno = alumno;
    }

    public Curso getCurso () {
        return curso;
    }

    public void setCurso (Curso curso){
        this.curso = curso;
    }

    public List<Calificacion> getCalificaciones () {
        return calificaciones;
    }

    public void setCalificaciones (List < Calificacion > calificaciones) {
        this.calificaciones = calificaciones;
    }

    public Condicion getCondicion () {
        return condicion;
    }

    public void setCondicion (Condicion condicion){
        this.condicion = condicion;
    }
}


