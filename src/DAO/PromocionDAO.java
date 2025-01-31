package DAO;

import DAO.interfaces.IPromocionDAO;
import DB.DBConfig;
import Entidades.Promocion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromocionDAO implements IPromocionDAO {
    private Connection connection;

    public PromocionDAO() {
        try {
            String url = "jdbc:mysql://localhost:3306/tp_java";
            String user = "root";
            String password = "Zurdo123";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void guardarPromocion(Promocion promocion) throws DAOException {
        String sql = "INSERT INTO promocion (nombre_promocion, descripcion, descuento) VALUES (?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, promocion.getNombrePromocion());
            ps.setString(2, promocion.getDescripcionPromocion());
            ps.setDouble(3, promocion.getDescuentoPorPromocion());

            ps.executeUpdate();
            System.out.println("Promoción guardada correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error al guardar la promoción", e);
        }
    }

    @Override
    public List<Promocion> obtenerTodasLasPromociones() throws DAOException {
        String sql = "SELECT * FROM promocion;";
        List<Promocion> promociones = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Promocion promocion = new Promocion(
                        rs.getString("nombre_promocion"),
                        rs.getString("descripcion"),
                        rs.getDouble("descuento")
                );
                promocion.setId(rs.getInt("id"));
                promociones.add(promocion);
                System.out.println("Promoción: " + promocion);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al recuperar las promociones", e);
        }

        return promociones;
    }

    @Override
    public void actualizarPromocion(int promocion_id, String nombre, String descripcion, double descuento) {
        String sql = "UPDATE promocion SET nombre_promocion = ?, descripcion =?, descuento =? WHERE id = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, descuento);
            ps.setInt(4, promocion_id);

            ps.executeUpdate();
            System.out.println("Promoción actualizada correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Promocion obtenerPromocionPorId(int id) throws DAOException {
        String sql = "SELECT * FROM promocion WHERE id = ?";
        Promocion promocion = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    promocion = new Promocion();
                    promocion.setId(rs.getInt("id"));
                    promocion.setNombrePromocion(rs.getString("nombre_promocion"));
                    promocion.setDescripcionPromocion(rs.getString("descripcion"));
                    promocion.setDescuentoPorPromocion(rs.getDouble("descuento"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error SQL al obtener la promoción: " + e.getMessage(), e);
        }

        return promocion;
    }



}
