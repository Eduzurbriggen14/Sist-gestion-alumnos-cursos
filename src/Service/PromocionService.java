package Service;

import DAO.DAOException;
import DAO.PromocionDAO;
import DAO.interfaces.IAdministradorDAO;
import DAO.interfaces.IPromocionDAO;
import Entidades.Promocion;

import java.util.List;

public class PromocionService {
    private PromocionDAO promocionDAO;

    public PromocionService(){
        promocionDAO = new PromocionDAO();
    }


    public List<Promocion> obtenerTodasLasPromociones() throws DAOException {
        return promocionDAO.obtenerTodasLasPromociones();
    }

    public Promocion obtenerPromocionCurso(int curso_id){
        return null;
    }

    public void actualizarPromocion(int promocion_id){

    }

    public Promocion obtenerPromocionPorId(int id_promocion) throws DAOException {
        return promocionDAO.obtenerPromocionPorId(id_promocion);
    }

}
