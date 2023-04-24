package com.example.lab1;

public class ListForAdapter {

    private String nazwa;
    private int ocena;
    private boolean isChecked;

    public ListForAdapter(String nazwa, int ocena) {
        this.nazwa = nazwa;
        this.ocena = ocena;
        this.isChecked = false;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
