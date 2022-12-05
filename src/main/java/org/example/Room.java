package org.example;

import java.util.Date;

public class Room {
    private final int Room_Number;
    private int Visited_Times;
    private String Last_Visit;

    public Room(int Room_Number, int Visited_Times, String Last_Visit) {
        this.Room_Number = Room_Number;
        this.Visited_Times = Visited_Times;
        this.Last_Visit = Last_Visit;

    }

    public int getRoom_Number() {
        return Room_Number;
    }

    public int getVisited_Times() {
        return Visited_Times;
    }

    public String getLast_Visit() {
        return Last_Visit;
    }

    public void newVisit() {
        Date Date = new Date();
        Visited_Times += 1;
        Last_Visit = Date.toString().split(" ")[2] + " " +
                Date.toString().split(" ")[1] + " " +
                Date.toString().split(" ")[3];
    }
}
