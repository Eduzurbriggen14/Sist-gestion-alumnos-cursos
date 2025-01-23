package Entidades;

public class ProfesorCurso {

    private Profesor profesor;
    private Curso curso;
    private int anio;

    public ProfesorCurso(Profesor profesor, Curso curso, int anio) {
        this.profesor = profesor;
        this.curso = curso;
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

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
}
