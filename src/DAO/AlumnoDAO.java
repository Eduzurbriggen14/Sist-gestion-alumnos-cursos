package DAO;

import Entidades.Alumno;
import Entidades.TipoUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DB.DBConfig;

public class AlumnoDAO implements IAlumnoDao {

    // Método para guardar un alumno en la base de datos
    @Override
    public void guardar(Alumno alumno) throws DAOException {
        String sql = "INSERT INTO alumno (nombre, passw, nombreUsuario, correo, tipoUsuario, abonoAlumno) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getPassw());
            ps.setString(3, alumno.getNombreUsuario());
            ps.setString(4, alumno.getCorreo());
            ps.setString(5, alumno.getTipoUsuario().name());
            ps.setBoolean(6, alumno.getAbonoAlumno());

            ps.executeUpdate();
            System.out.println("Alumno guardado correctamente");

        } catch (SQLException e) {
            throw new DAOException("Error al guardar el alumno", e);
        }
    }

    // Método para eliminar un alumno por su nombre de usuario
    @Override
    public void eliminar(String nombreUsuario) throws DAOException {
        String sql = "DELETE FROM alumno WHERE nombreUsuario = ?";

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Alumno eliminado correctamente");
            } else {
                System.out.println("No se encontró un alumno con el nombre de usuario especificado");
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el alumno", e);
        }
    }

    // Método para modificar un alumno existente
    @Override
    public void modificar(Alumno alumno) throws DAOException {
        String sql = "UPDATE alumno SET nombre = ?, passw = ?, nombreUsuario = ?, correo = ? WHERE nombreUsuario = ?";

        System.out.println("usuario "+ alumno.getNombreUsuario()+ " "+ alumno.getNombre());
        try (Connection connection = DBConfig.getConexion();

             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getPassw());
            ps.setString(3, alumno.getNombreUsuario());
            ps.setString(4, alumno.getCorreo());
            ps.setString(5, alumno.getNombreUsuario());
            ps.executeUpdate();
            System.out.println("Alumno modificado correctamente");

        } catch (SQLException e) {
            throw new DAOException("Error al modificar el alumno", e);
        }
    }

    // Método para recuperar un alumno por su nombre de usuario
    @Override
    public Alumno recuperar(String nombreUsuario) throws DAOException {
        String sql = "SELECT * FROM alumno WHERE nombreUsuario = ?";
        Alumno alumno = null;

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                alumno = new Alumno(
                        rs.getString("nombre"),
                        rs.getString("passw"),
                        rs.getString("nombreUsuario"),
                        rs.getString("correo"),
                        TipoUsuario.valueOf(rs.getString("tipoUsuario")),
                        rs.getBoolean("abonoAlumno"));

            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar el alumno", e);
        }

        return alumno;
    }

    // Método para recuperar todos los alumnos
    @Override
    public List<Alumno> recuperarTodos() throws DAOException {
        String sql = "SELECT * FROM alumno";
        List<Alumno> alumnos = new ArrayList<>();

        try (Connection connection = DBConfig.getConexion();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getString("nombre"),
                        rs.getString("passw"),
                        rs.getString("nombreUsuario"),
                        rs.getString("correo"),
                        TipoUsuario.valueOf(rs.getString("tipoUsuario")),
                        rs.getBoolean("abonoAlumno"));
                alumnos.add(alumno);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar los alumnos", e);
        }

        return alumnos;
    }
    @Override
    public int obtenerIdAlumnoPorNombreUsuario(String nombreUsuario) throws DAOException {
        String sql = "SELECT id FROM alumno WHERE nombreUsuario = ?";

        try (Connection conn = DBConfig.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new DAOException("Alumno no encontrado con nombreUsuario: " + nombreUsuario);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al obtener el ID del alumno", e);
        }
    }

    @Override
    public Alumno recuperarPorId(int id) throws DAOException {
        String sql = "SELECT * FROM alumno WHERE id = ?";
        Alumno alumno = null;

        try (Connection connection = DBConfig.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                alumno = new Alumno(
                        rs.getString("nombre"),
                        rs.getString("passw"),
                        rs.getString("nombreUsuario"),
                        rs.getString("correo"),
                        TipoUsuario.valueOf(rs.getString("tipoUsuario")),
                        rs.getBoolean("abonoAlumno"));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar el alumno por ID", e);
        }

        return alumno;
    }

}