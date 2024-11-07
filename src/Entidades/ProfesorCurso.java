package Entidades;

public class ProfesorCurso {

    private Profesor profesor;
    private Curso curso;
    private Semestre semestre;
    private int anio;

    public ProfesorCurso(Profesor profesor, Curso curso, Semestre semestre, int anio) {
        this.profesor = profesor;
        this.curso = curso;
        this.semestre = semestre;
        this.anio = anio;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
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
}
