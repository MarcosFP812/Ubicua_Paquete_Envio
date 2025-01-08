package com.example.smart_packet.data;

public class Receptor {
    private int id;
    private String user;
    private String pw;
    private Ubicacion ubi;


    public Receptor(int id, String user, String pw, Ubicacion ubi) {
        this.id = id;
        this.user = user;
        this.pw = pw;
        this.ubi = ubi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public Ubicacion getUbi() {
        return ubi;
    }

    public void setUbi(Ubicacion ubi) {
        this.ubi = ubi;
    }
}
