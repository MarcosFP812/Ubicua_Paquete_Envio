/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

/**
 *
 * @author socra
 */
public class Ubicacion {
    
    private double Longitud;

    private double Latitud;

    public Ubicacion(double Longitud, double Latitud) {
        this.Longitud = Longitud;
        this.Latitud = Latitud;
    }

    @Override
    public String toString() {
        return "Ubicacion{" + "Longitud=" + Longitud + ", Latitud=" + Latitud + '}';
    }

    /**
     * Get the value of Latitud
     *
     * @return the value of Latitud
     */
    public double getLatitud() {
        return Latitud;
    }

    /**
     * Set the value of Latitud
     *
     * @param Latitud new value of Latitud
     */
    public void setLatitud(double Latitud) {
        this.Latitud = Latitud;
    }

    /**
     * Get the value of Longitud
     *
     * @return the value of Longitud
     */
    public double getLongitud() {
        return Longitud;
    }

    /**
     * Set the value of Longitud
     *
     * @param Longitud new value of Longitud
     */
    public void setLongitud(double Longitud) {
        this.Longitud = Longitud;
    }

}
