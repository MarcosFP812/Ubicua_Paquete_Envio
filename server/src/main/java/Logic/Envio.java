/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

/**
 *
 * @author socra
 */
public class Envio {
    private int idEnvio;
    private int transportistaId;
    private int paqueteId;
    private int receptorId;
    private int remitenteId;
    private boolean finalizado;

    public Envio(int idEnvio, int transportistaId, int paqueteId, int receptorId, int remitenteId, boolean finalizado) {
        this.idEnvio = idEnvio;
        this.transportistaId = transportistaId;
        this.paqueteId = paqueteId;
        this.receptorId = receptorId;
        this.remitenteId = remitenteId;
        this.finalizado = finalizado;
    }

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public int getTransportistaId() {
        return transportistaId;
    }

    public void setTransportistaId(int transportistaId) {
        this.transportistaId = transportistaId;
    }

    public int getPaqueteId() {
        return paqueteId;
    }

    public void setPaqueteId(int paqueteId) {
        this.paqueteId = paqueteId;
    }

    public int getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(int receptorId) {
        this.receptorId = receptorId;
    }

    public int getRemitenteId() {
        return remitenteId;
    }

    public void setRemitenteId(int remitenteId) {
        this.remitenteId = remitenteId;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
}