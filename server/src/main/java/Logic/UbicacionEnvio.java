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
public class UbicacionEnvio {
    
    private int idDato;

    private double longitud;

    private double latitud;

    private double velocidad;

    private double velocidadVia;
    
    private Timestamp fecha;

    public UbicacionEnvio(int idDato, double longitud, double latitud, double velocidad, double velocidadVia, Timestamp fecha) {
        this.idDato = idDato;
        this.longitud = longitud;
        this.latitud = latitud;
        this.velocidad = velocidad;
        this.velocidadVia = velocidadVia;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "UbicacionEnvio{" + "idDato=" + idDato + ", longitud=" + longitud + ", latitud=" + latitud + ", velocidad=" + velocidad + ", velocidadVia=" + velocidadVia + ", fecha=" + fecha + '}';
    }

    /**
     * Get the value of velocidadVia
     *
     * @return the value of velocidadVia
     */
    public double getVelocidadVia() {
        return velocidadVia;
    }

    /**
     * Set the value of velocidadVia
     *
     * @param velocidadVia new value of velocidadVia
     */
    public void setVelocidadVia(double velocidadVia) {
        this.velocidadVia = velocidadVia;
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
     * Get the value of velocidad
     *
     * @return the value of velocidad
     */
    public double getVelocidad() {
        return velocidad;
    }

    /**
     * Set the value of velocidad
     *
     * @param velocidad new value of velocidad
     */
    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * Get the value of latitud
     *
     * @return the value of latitud
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * Set the value of latitud
     *
     * @param latitud new value of latitud
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /**
     * Get the value of longitud
     *
     * @return the value of longitud
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * Set the value of longitud
     *
     * @param longitud new value of longitud
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
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
