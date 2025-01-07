/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

/**
 *
 * @author socra
 */
public class Transportista {
    
    private String nombre;
    
    private double TiempoPerdida;

    private double TiempoEnvio;

    /**
     * Get the value of TiempoEnvio
     *
     * @return the value of TiempoEnvio
     */
    public double getTiempoEnvio() {
        return TiempoEnvio;
    }

    /**
     * Set the value of TiempoEnvio
     *
     * @param TiempoEnvio new value of TiempoEnvio
     */
    public void setTiempoEnvio(double TiempoEnvio) {
        this.TiempoEnvio = TiempoEnvio;
    }

    /**
     * Get the value of TiempoPerdida
     *
     * @return the value of TiempoPerdida
     */
    public double getTiempoPerdida() {
        return TiempoPerdida;
    }

    /**
     * Set the value of TiempoPerdida
     *
     * @param TiempoPerdida new value of TiempoPerdida
     */
    public void setTiempoPerdida(double TiempoPerdida) {
        this.TiempoPerdida = TiempoPerdida;
    }


    /**
     * Get the value of nombre
     *
     * @return the value of nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Set the value of nombre
     *
     * @param nombre new value of nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private int id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{"
            + "\"nombre\": \"" + nombre + "\", "
            + "\"id\": \"" + id + "\", "
            + "\"TiempoEnvio\": \"" + TiempoEnvio + "\", "
            + "\"TiempoPerdida\": \"" + TiempoPerdida + "\""
            + "}";
    }


}
