package com.example.smxcc.reserves;

import java.util.ArrayList;

public class Reserva {
    Integer id;
    String inici;
    String fi;
    String ubicacio;
    int aprovada;
    String nomObjecte;
    String descObjecte;
    ArrayList<Recurs> recursos = new ArrayList<Recurs>();

    Reserva(Integer id,String inici, String fi, String ubicacio, int aprovada, String nomObjecte, String descObjecte){
        this.id = id;
        this.inici = inici;
        this.fi = fi;
        this.ubicacio = ubicacio;
        this.aprovada = aprovada;
        this.nomObjecte = nomObjecte;
        this.descObjecte = descObjecte;
    }

    public Integer getId() {
        return id;
    }

    public String getInici() {
        return inici;
    }

    public String getFi() {
        return fi;
    }

    public String getUbicacio() {
        return ubicacio;
    }

    public int getAprovada() {
        return aprovada;
    }

    public String getNomObjecte() {
        return nomObjecte;
    }

    public String getDescObjecte() {
        return descObjecte;
    }

    public ArrayList<Recurs> getRecursos(){return recursos;}

    public void afegirRecursos(Recurs r){
        recursos.add(r);
    }

}
