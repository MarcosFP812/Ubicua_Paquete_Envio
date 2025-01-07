package com.example.smart_packet.data;

import android.widget.ImageView;

public class ListElement {
    public String id;
    public String color;
    public ImageView img;

    public ListElement(String color, String id) {
        this.color = color;
        this.id = id;
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
