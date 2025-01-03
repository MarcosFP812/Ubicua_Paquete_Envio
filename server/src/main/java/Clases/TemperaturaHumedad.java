/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.sql.Timestamp;

public class TemperaturaHumedad {
    private int idDato;
    private double temperatura;
    private double humedad;
    private Timestamp fecha;

    public TemperaturaHumedad(int idDato, double temperatura, double humedad, Timestamp fecha) {
        this.idDato = idDato;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
    return "{"
        + "\"idDato\": \"" + idDato + "\", "
        + "\"temperatura\": \"" + temperatura + "\", "
        + "\"humedad\": \"" + humedad + "\", "
        + "\"fecha\": \"" + fecha + "\""
        + "}";
    }


    // Getters y Setters

    public int getIdDato() {
        return idDato;
    }

    public void setIdDato(int idDato) {
        this.idDato = idDato;
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

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
