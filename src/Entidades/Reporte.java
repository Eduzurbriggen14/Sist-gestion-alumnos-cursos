package Entidades;

public class Reporte {

    private String nombreReporte;
    private String descripcionReporte;

    public Reporte(String nombreReporte, String descripcionReporte) {
        this.nombreReporte = nombreReporte;
        this.descripcionReporte = descripcionReporte;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getDescripcionReporte() {
        return descripcionReporte;
    }

    public void setDescripcionReporte(String descripcionReporte) {
        this.descripcionReporte = descripcionReporte;
    }
}
