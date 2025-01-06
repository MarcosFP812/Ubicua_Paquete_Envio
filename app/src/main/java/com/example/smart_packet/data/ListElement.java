package com.example.smart_packet.data;

import android.widget.ImageView;

public class ListElement {
    public int id;
    public String color;
    public ImageView img;

    public ListElement(String color, int id) {
        this.color = color;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
