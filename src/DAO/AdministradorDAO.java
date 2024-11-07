package DAO;

import DB.DBConfig;
import Entidades.Administrador;
import Entidades.TipoUsuario;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO implements IAdministradorDAO {
    @Override
    public void guardar(Administrador administrador) throws DAOException {
        String sql = "INSERT INTO administrador (nombre, passw, nombreUsuario, correo, tipoUsuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, administrador.getNombre());
            ps.setString(2, administrador.getPassw());
            ps.setString(3, administrador.getNombreUsuario());
            ps.setString(4, administrador.getCorreo());
            ps.setString(5, administrador.getTipoUsuario().name());

            ps.executeUpdate();
            System.out.println("Administrador guardado correctamente");

        } catch (SQLException e) {
            throw new DAOException("Error al guardar el admin", e);
        }
    }

    @Override
    public void eliminar(String nombreUsuario) throws DAOException {
        String sql = "DELETE FROM administrador WHERE nombreUsuario = ?";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Administrador eliminado correctamente");
            } else {
                System.out.println("No se encontró un administrador con el nombre de usuario especificado");
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el administrador", e);
        }
    }

    @Override
    public void modificar(Administrador administrador) throws DAOException {
        String sql = "UPDATE administrador SET nombre = ?, passw = ?,tipoUsuario = ?, correo = ? WHERE nombreUsuario = ?";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, administrador.getNombre());
            ps.setString(2, administrador.getPassw());
            ps.setString(3, administrador.getNombreUsuario());
            ps.setString(4, administrador.getCorreo());
            ps.setString(5, administrador.getTipoUsuario().name());
            ps.executeUpdate();
            System.out.println("Admin modificado correctamente");

        } catch (SQLException e) {
            throw new DAOException("Error al modificar el alumno", e);
        }
    }

    @Override
    public Administrador recuperar(String nombreUsuario) throws DAOException {
        String sql = "SELECT * FROM administrador WHERE nombreUsuario = ?";
        Administrador admin = null;

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                admin = new Administrador(
                        rs.getString("nombre"),
                        rs.getString("passw"),
                        rs.getString("nombreUsuario"),
                        rs.getString("correo"),
                        TipoUsuario.valueOf(rs.getString("tipoUsuario")));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar el alumno", e);
        }

        return admin;
    }

    @Override
    public List<Administrador> recuperarTodos() throws DAOException {
        String sql = "SELECT * FROM administrador";
        List<Administrador> administradores = new ArrayList<>();

        try (Connection connection = DBConfig.getConexion();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Administrador admin = new Administrador(
                        rs.getString("nombre"),
                        rs.getString("passw"),
                        rs.getString("nombreUsuario"),
                        rs.getString("correo"),
                        TipoUsuario.valueOf(rs.getString("tipoUsuario")));
                administradores.add(admin);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar los alumnos", e);
        }

        return administradores;
    }
}
