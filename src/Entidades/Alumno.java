package Entidades;

import java.util.ArrayList;
import java.util.List;

public class Alumno extends Usuario{
    private int id;
    private boolean abonoAlumno;
    private TipoUsuario tipoUsuario;
    private int limiteCursos = 3; // Límite de cursos
    private List<Curso> cursosInscritos = new ArrayList<>();

    public Alumno(String nombre, String passw, String nombreUsuario, String correo, TipoUsuario tipoUsuario, boolean abonoAlumno) {
        super(nombre, passw, nombreUsuario, correo);
        this.abonoAlumno = abonoAlumno;
        this.tipoUsuario = tipoUsuario;
    }

    public Alumno(String nombre, String passw, String nombreUsuario, String correo) {
        super(nombre, passw, nombreUsuario, correo);
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean getAbonoAlumno() {
        return abonoAlumno;
    }

    public void setAbonoAlumno(boolean abonoAlumno) {
        this.abonoAlumno = abonoAlumno;
    }

    public boolean isAbonoAlumno() {
        return abonoAlumno;
    }

    public int getLimiteCursos() {
        return limiteCursos;
    }

    public void setLimiteCursos(int limiteCursos) {
        this.limiteCursos = limiteCursos;
    }

    public List<Curso> getCursosInscritos() {
        return cursosInscritos;
    }

    public void setCursosInscritos(List<Curso> cursosInscritos) {
        this.cursosInscritos = cursosInscritos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
