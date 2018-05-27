package com.example.smxcc.reserves;

public class Recurs {
    String nom;
    String descripcio;
    Double quantitat;
    String mesura;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Double getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(Double quantitat) {
        this.quantitat = quantitat;
    }

    public String getMesura() {
        return mesura;
    }

    public void setMesura(String mesura) {
        this.mesura = mesura;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    Double valor;

    Recurs(String nom, String descripcio, Double quantitat, String mesura, Double valor){
        this.nom = nom;
        this.descripcio = descripcio;
        this.quantitat = quantitat;
        this.mesura = mesura;
        this.valor = valor;
    }
}
