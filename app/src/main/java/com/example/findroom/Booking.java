package com.example.findroom;

public class Booking {
    private String roomName;
    private String date;
    private String time;
    private boolean available;

    public Booking(String roomName, String date, String time, boolean available) {
        this.roomName = roomName;
        this.date = date;
        this.time = time;
        this.available = available;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isAvailable() {
        return available;
    }


}
