package DAO.interfaces;

import DAO.DAOException;
import Entidades.Promocion;

import java.util.List;

public interface IPromocionDAO {

    public void guardarPromocion(Promocion promocion) throws DAOException;
    public List<Promocion> obtenerTodasLasPromociones() throws DAOException;
    public void actualizarPromocion(int promocion_id, String nombre, String descripcion, double descuento);
    public Promocion obtenerPromocionPorId(int id_promocion) throws DAOException;
}
