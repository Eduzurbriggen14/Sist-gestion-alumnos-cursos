package DAO;

import DB.DBConfig;
import Entidades.Profesor;
import Entidades.TipoUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO implements IProfesor{


    @Override
    public void guardar(Profesor profesor) throws DAOException {
        String sql = "INSERT INTO profesor (nombre, passw, nombreUsuario, correo, tipoUsuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getPassw());
            ps.setString(3, profesor.getNombreUsuario());
            ps.setString(4, profesor.getCorreo());
            ps.setString(5, profesor.getTipoUsuario().name());

            ps.executeUpdate();
            System.out.println("profesor guardado correctamente");

        } catch (SQLException e) {
            throw new DAOException("Error al guardar el profesor", e);
        }
    }

    @Override
    public void eliminar(String nombreUsuario) throws DAOException {
        String sql = "DELETE FROM profesor WHERE nombreUsuario = ?";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("profesor eliminado correctamente");
            } else {
                System.out.println("No se encontró un profesor con el nombre de usuario especificado");
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el profesor", e);
        }
    }

    @Override
    public void modificar(Profesor profesor) throws DAOException {
        String sql = "UPDATE profesor SET nombre = ?, passw = ?, nombreUsuario = ?, correo = ? WHERE nombreUsuario = ?";
        System.out.println("usuario " + profesor.getNombreUsuario());

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getPassw());
            ps.setString(3, profesor.getNombreUsuario());
            ps.setString(4, profesor.getCorreo());
            ps.setString(5, profesor.getNombreUsuario());

            int rowsAffected = ps.executeUpdate();
            System.out.println("Filas afectadas: " + rowsAffected);

            if (rowsAffected > 0) {
                System.out.println("Profesor modificado correctamente");
            } else {
                System.out.println("No se encontraron filas para actualizar");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al modificar el profesor", e);
        }
    }

    @Override
    public Profesor recuperar(String nombreUsuario) throws DAOException {
        String sql = "SELECT * FROM profesor WHERE nombreUsuario = ?";
        Profesor profesor = null;

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                profesor = new Profesor(
                        rs.getString("nombre"),
                        rs.getString("passw"),
                        rs.getString("nombreUsuario"),
                        rs.getString("correo"),
                        TipoUsuario.valueOf(rs.getString("tipoUsuario")));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar el profesor", e);
        }

        return profesor;
    }

    @Override
    public List<Profesor> recuperarTodos() throws DAOException {
        String sql = "SELECT * FROM profesor";
        List<Profesor> profesores = new ArrayList<>();

        try (Connection connection = DBConfig.getConexion();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Profesor profesor = new Profesor(
                        rs.getString("nombre"),
                        rs.getString("passw"),
                        rs.getString("nombreUsuario"),
                        rs.getString("correo"),
                        TipoUsuario.valueOf(rs.getString("tipoUsuario")));
                profesores.add(profesor);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar los profesores", e);
        }

        return profesores;
    }
}
