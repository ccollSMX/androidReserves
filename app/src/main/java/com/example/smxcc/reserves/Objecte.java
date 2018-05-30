package com.example.smxcc.reserves;

public class Objecte {
    private int id;
    private String nom;
    private String descripcio;

    Objecte(String nom, String descripcio){
        this.nom = nom;
        this.descripcio = descripcio;
    }

    Objecte(int id,String nom, String descripcio){
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
    }

    public String toString(){
        return (nom.toUpperCase());
    }
}
