package Entidades;

public class AlumnoCurso {

    private Alumno alumno;
    private Curso curso;
    private Double calificacionFinal;

    public AlumnoCurso(Alumno alumno, Curso curso) {
        this.alumno = alumno;
        this.curso = curso;
        this.calificacionFinal = null;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Double getCalificacionFinal() {
        return calificacionFinal;
    }

    public void setCalificacionFinal(Double calificacionFinal) {
        this.calificacionFinal = calificacionFinal;
    }

    @Override
    public String toString() {
        return "AlumnoCurso{" +
                "alumno=" + alumno.getNombreUsuario() +
                ", curso=" + curso.getNombreCurso() +
                ", calificacionFinal=" + (calificacionFinal != null ? calificacionFinal : "No asignada") +
                '}';
    }
}
