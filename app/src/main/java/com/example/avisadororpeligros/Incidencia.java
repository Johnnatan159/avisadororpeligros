package com.example.avisadororpeligros;

public class Incidencia {

    private String url;
    private String latitud;
    private String longitud;
    private String direccio;
    private String problema;
    private String tipoIncidencia;
    private String numeroTelefono;
    private String correoElectronico;
    private String liniaMetro;
    private String horaNotificacion;


    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setTipoIncidencia(String tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    public String getHoraNotificacion() {
        return horaNotificacion;
    }

    public void setHoraNotificacion(String horaNotificacion) {
        this.horaNotificacion = horaNotificacion;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getLiniaMetro() {
        return liniaMetro;
    }

    public void setLiniaMetro(String liniaMetro) {
        this.liniaMetro = liniaMetro;
    }

    public Incidencia(String latitud, String longitud, String direccio, String problema, String url) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccio = direccio;
        this.problema = problema;
        this.url = url;
    }

    public Incidencia() {
    }
}
