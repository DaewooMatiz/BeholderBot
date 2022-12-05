package org.example;
import java.util.Date;

public class Violator {
    private final int Violator_ID;
    private final String Violator_FullName;
    private final int Room;
    private int Violations;
    private String Last_Violation;


    public Violator(int Violator_ID, String Violator_FullName, int Room, int Violations, String Last_Violation) {
        this.Violator_ID = Violator_ID;
        this.Violator_FullName = Violator_FullName;
        this.Room = Room;
        this.Violations = Violations;
        this.Last_Violation = Last_Violation;

    }

    public int getViolatorID() {
        return Violator_ID;
    }

    public String getViolatorFullName() {
        return Violator_FullName;
    }

    public int getViolatorRoom() {
        return Room;
    }

    public int getViolations() {
        return Violations;
    }

    public String getLast_Violation() {
        return Last_Violation;
    }

    public void newViolation(){
        Date Date = new Date();
        Violations += 1;
        Last_Violation = Date.toString().split(" ")[2] + " " +
                Date.toString().split(" ")[1] + " " +
                Date.toString().split(" ")[3];
    }
}