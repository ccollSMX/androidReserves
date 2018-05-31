package com.example.smxcc.reserves;

public class Recurs {
    private int id;
    private String nom;
    private String descripcio;
    private Double quantitat;
    private String mesura;
    private int stock;
    private int posicio;

    public String getNom() {
        return nom;
    }

    public int getId(){return id;}

    public void setId(int id){
        this.id = id;
    }

    public int getStock(){return stock;}

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

    Recurs(int id,String nom, String descripcio, Double quantitat, String mesura, Double valor){
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.quantitat = quantitat;
        this.mesura = mesura;
        this.valor = valor;
    }

    Recurs(int id,String nom, String descripcio, int stock){
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.stock = stock;
    }

    Recurs(String nom){
        this.nom =nom;
    }

    public String toString(){
        return (nom.toUpperCase());
    }

    public void posicio(int posicio){
        this.posicio = posicio;
    }

    public int getPosicio(){
        return posicio;
    }
}
