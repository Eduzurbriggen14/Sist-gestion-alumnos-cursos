package DAO.interfaces;

import DAO.DAOException;
import Entidades.Curso;

import java.util.List;

public interface ICursoDAO {

    public void guardar(Curso curso) throws DAOException;
    public void eliminar(String nombreCurso)throws DAOException;
    public void modificar(Curso curso)throws DAOException;
    public Curso recuperar(String nombreCurso)throws DAOException;
    public List<Curso> recuperarTodos()throws DAOException;
    public int recuperarIdCurso(String nombreCurso) throws DAOException;
    public Curso recuperarCursoPorId(int id) throws DAOException;
    public void agregarPromocionACurso(int id_curso, int id_promocion);
    public boolean recuperarPromocionCurso(int id_curso) throws DAOException;
}
