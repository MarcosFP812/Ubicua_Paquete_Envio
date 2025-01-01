package com.example.wheatherstation_uah.data;

public class ListElement {
    public String id;
    public String color;

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