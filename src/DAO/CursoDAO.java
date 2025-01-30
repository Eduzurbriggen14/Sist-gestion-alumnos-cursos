package DAO;

import DAO.interfaces.ICursoDAO;
import Entidades.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DB.DBConfig;

public class CursoDAO implements ICursoDAO {

    // Método para guardar un curso en la base de datos
    @Override
    public void guardar(Curso curso) throws DAOException {
        String sql = "INSERT INTO curso (nombreCurso, descripcionCurso, cupo, precioCurso) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, curso.getNombreCurso());
            ps.setString(2, curso.getDescripcionCurso());
            ps.setInt(3, curso.getCupo());
            ps.setDouble(4, curso.getPrecioCurso());



            ps.executeUpdate();
            System.out.println("Curso guardado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al guardar el curso", e);
        }
    }
    @Override
    public void eliminar(String nombreCurso) throws DAOException {
        String sql = "DELETE FROM curso WHERE nombreCurso = ?";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreCurso);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Curso eliminado correctamente");
            } else {
                System.out.println("No se encontró un curso con el nombre especificado");
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el curso", e);
        }
    }

    // Método para modificar un curso existente
    @Override
    public void modificar(Curso curso) throws DAOException {
        String sql = "UPDATE curso SET descripcionCurso = ?, cupo = ?, precioCurso = ? WHERE nombreCurso = ?";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, curso.getDescripcionCurso());
            ps.setInt(2, curso.getCupo());
            ps.setDouble(3, curso.getPrecioCurso());
            ps.setString(4, curso.getNombreCurso());

            ps.executeUpdate();
            System.out.println("Curso modificado correctamente");

        } catch (SQLException e) {
            throw new DAOException("Error al modificar el curso", e);
        }
    }

    // Método para recuperar un curso por su nombre
    @Override
    public Curso recuperar(String nombreCurso) throws DAOException {
        String sql = "SELECT * FROM curso WHERE nombreCurso = ?";
        Curso curso = null;

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreCurso);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                curso = new Curso(
                        rs.getString("nombreCurso"),
                        rs.getString("descripcionCurso"),
                        rs.getInt("cupo"),
                        rs.getDouble("precioCurso")
                );
                curso.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar el curso", e);
        }

        return curso;
    }

    // Método para recuperar todos los cursos
    @Override
    public List<Curso> recuperarTodos() throws DAOException {
        String sql = "SELECT * FROM curso";
        List<Curso> cursos = new ArrayList<>();

        try (Connection connection = DBConfig.getConexion();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getString("nombreCurso"),
                        rs.getString("descripcionCurso"),
                        rs.getInt("cupo"),
                        rs.getDouble("precioCurso")
                );
                curso.setId(rs.getInt("id"));
                cursos.add(curso);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar los cursos", e);
        }

        return cursos;
    }

    @Override
    public int recuperarIdCurso(String nombreCurso) throws DAOException {
        String sql = "SELECT id FROM curso WHERE nombreCurso = ?";
        int idCurso = -1; // Valor por defecto para indicar que no se encontró el curso

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreCurso);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idCurso = rs.getInt("id");  // Recuperar el ID del curso
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar el ID del curso", e);
        }

        return idCurso;
    }

    @Override
    public Curso recuperarCursoPorId(int id) throws DAOException {
        String sql = "SELECT * FROM curso WHERE id = ?";
        Curso curso = null;

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                curso = new Curso(
                        rs.getString("nombreCurso"),
                        rs.getString("descripcionCurso"),
                        rs.getInt("cupo"),
                        rs.getDouble("precioCurso")
                );
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar el curso por ID", e);
        }

        return curso;
    }

    @Override
    public void agregarPromocionACurso(int id_curso, int id_promocion) {
        String sql = "UPDATE curso SET promocion_id = ? WHERE id =? ";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1,id_promocion);
            ps.setInt(2,id_curso);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean recuperarPromocionCurso(int id_curso) throws DAOException {
        String sql = "SELECT p.nombre_promocion, p.descuento " +
                "FROM curso c " +
                "INNER JOIN promocion p ON c.promocion_id = p.id " +
                "WHERE c.id = ?";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id_curso);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error al verificar si el curso tiene una promoción", e);
        }

        return false;  // Si no se encuentra, el curso no tiene una promoción
    }
}