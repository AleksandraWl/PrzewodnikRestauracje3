package com.example.malami.przewodnikrestauracje;

public class restauracje {
    String nazwa;
    String dlugosc;
    String szerokosc;
    String adres;

    restauracje()
    {

    }
    restauracje(String nazwa, String dlugosc, String szerokosc, String adres)
    {
        this.nazwa = nazwa;
        this.dlugosc =dlugosc;
        this.szerokosc=szerokosc;
        this.adres=adres;
    }

    public String getSzerokosc() {
        return szerokosc;
    }

    public String getAdres() {
        return adres;
    }

    public String getDlugosc() {
        return dlugosc;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }


}