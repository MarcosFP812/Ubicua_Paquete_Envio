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
public class Dato {
    private int idDato;
    private int envioId;
    private Timestamp fecha;

    public Dato(int idDato, int envioId, Timestamp fecha) {
        this.idDato = idDato;
        this.envioId = envioId;
        this.fecha = fecha;
    }

    public int getIdDato() {
        return idDato;
    }

    public void setIdDato(int idDato) {
        this.idDato = idDato;
    }

    public int getEnvioId() {
        return envioId;
    }

    public void setEnvioId(int envioId) {
        this.envioId = envioId;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
