package com.example.findroom;

public class DataRoom {
    private String nomor;
    private boolean isAvailable;


    public String getNomor() {
        return nomor;
    }

    public boolean isAvailable() {
        return isAvailable;
    }


    public DataRoom(String nomor, boolean isAvailable) {
        this.nomor = nomor;
        this.isAvailable = isAvailable;
    }



    public DataRoom() {
        // Default constructor required for Firebase
    }
}
