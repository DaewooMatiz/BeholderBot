package org.example;

public class User {
    public Long User_ID;
    public String User_Name;
    public Boolean Is_SKIF;
    public Boolean Is_Admin;

    public User(Long User_ID, String User_Name, Boolean Is_SKIF, Boolean Is_Admin ){
        this.User_ID = User_ID;
        this.User_Name = User_Name;
        this.Is_SKIF = Is_SKIF;
        this.Is_Admin = Is_Admin;

    }
}
