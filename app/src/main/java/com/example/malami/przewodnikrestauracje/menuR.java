package com.example.malami.przewodnikrestauracje;

public class menuR {
    String nazwa;
    String cena;
    String opis;


    public menuR() {
    }

    public menuR(String opis, String cena, String nazwa) {
        this.opis = opis;
        this.cena=cena;
        this.nazwa=nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getCena() {
        return cena;
    }

    public String getOpis() {
        return opis;
    }
}