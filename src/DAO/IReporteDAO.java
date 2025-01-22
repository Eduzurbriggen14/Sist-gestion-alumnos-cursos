package DAO;

import java.util.List;
import java.util.Map;

public interface IReporteDAO {

    public List<Map<String, Object>> obtenerReporteCursos();

    public List<Map<String, Object>> obtenerReporteGrafico();
}
