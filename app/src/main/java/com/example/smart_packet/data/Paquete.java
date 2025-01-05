package com.example.smart_packet.data;

public class Paquete {
    private int transportista;
    private String id;
    private int receptor;
    private String estado;
    private Ubicacion ubi;
    private double temperatura;
    private double humedad;

    public Paquete(int transportista, String id, int receptor, String estado) {
        this.transportista = transportista;
        this.id = id;
        this.receptor = receptor;
        this.estado = estado;
    }

    public int getTransportista() {
        return transportista;
    }

    public void setTransportista(int transportista) {
        this.transportista = transportista;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReceptor() {
        return receptor;
    }

    public void setReceptor(int receptor) {
        this.receptor = receptor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Ubicacion getUbi() {
        return ubi;
    }

    public void setUbi(Ubicacion ubi) {
        this.ubi = ubi;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(double humedad) {
        this.humedad = humedad;
    }
}
