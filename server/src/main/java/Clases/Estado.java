/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.sql.Timestamp;

/**
 *
 * @author socra
 */
public class Estado {
    
    private int idDato;

    private String estado;

    private Timestamp fecha;

    public Estado(int idDato, String estado, Timestamp fecha) {
        this.idDato = idDato;
        this.estado = estado;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
    return "{"
        + "\"idDato\": \"" + idDato + "\", "
        + "\"estado\": \"" + estado + "\", "
        + "\"fecha\": \"" + fecha + "\""
        + "}";
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
     * Get the value of estado
     *
     * @return the value of estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Set the value of estado
     *
     * @param estado new value of estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
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
