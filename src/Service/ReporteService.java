package Service;

import DAO.ReporteDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteService {

    private final ReporteDAO reporteDAO;

    public ReporteService() {
        this.reporteDAO = new ReporteDAO();
    }

    public List<Map<String, Object>> generarReporte() throws ServiceException {
        try {
            return reporteDAO.obtenerReporteCursos();
        } catch (Exception e) {
            throw new ServiceException("Error al generar el reporte", e);
        }
    }

}
