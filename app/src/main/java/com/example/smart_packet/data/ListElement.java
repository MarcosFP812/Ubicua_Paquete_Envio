package com.example.smart_packet.data;

import android.widget.ImageView;

public class ListElement {
    public String id;
    public String color;
    public String estado;
    public ImageView img;
    public String idCliente;

    public ListElement(String color, String id, String estado, String idCliente) {
        this.color = color;
        this.id = id;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
