/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

import java.sql.Timestamp;

/**
 *
 * @author socra
 */
public class Ventilador {
    
    private int idDato;

    private boolean activo;

    private Timestamp fecha;

    public Ventilador(int idDato, boolean activo, Timestamp fecha) {
        this.idDato = idDato;
        this.activo = activo;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Ventilador{" + "idDato=" + idDato + ", activo=" + activo + ", fecha=" + fecha + '}';
    }

    /**
     * Get the value of fecha
     *
     * @return the value of fecha
     */
    public Timestamp getFecha() {
        return fecha;
    }

    /**
     * Set the value of fecha
     *
     * @param fecha new value of fecha
     */
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    /**
     * Get the value of activo
     *
     * @return the value of activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Set the value of activo
     *
     * @param activo new value of activo
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Get the value of idDato
     *
     * @return the value of idDato
     */
    public int getIdDato() {
        return idDato;
    }

    /**
     * Set the value of idDato
     *
     * @param idDato new value of idDato
     */
    public void setIdDato(int idDato) {
        this.idDato = idDato;
    }

}
