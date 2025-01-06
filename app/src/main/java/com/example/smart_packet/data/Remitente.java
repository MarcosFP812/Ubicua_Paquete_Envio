package com.example.smart_packet.data;

public class Remitente {
    private int idRemitente;
    private String user;
    private String pw;
    private Ubicacion ubi;

    public Remitente(int idRemitente, String user, String pw, Ubicacion ubi) {
        this.idRemitente = idRemitente;
        this.user = user;
        this.pw = pw;
        this.ubi = ubi;
    }

    public int getIdRemitente() {
        return idRemitente;
    }

    public void setIdRemitente(int idRemitente) {
        this.idRemitente = idRemitente;
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
