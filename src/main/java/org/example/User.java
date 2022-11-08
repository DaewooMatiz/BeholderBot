package org.example;

public class User {
    private final Long User_ID;
    private final String User_Name;
    private final Boolean Is_SKIF;
    private final Boolean Is_Admin;


    public User(Long User_ID, String User_Name, Boolean Is_SKIF, Boolean Is_Admin ){
        this.User_ID = User_ID;
        this.User_Name = User_Name;
        this.Is_SKIF = Is_SKIF;
        this.Is_Admin = Is_Admin;

    }

    public Long getUserID() {
        return User_ID;
    }

    public String getUserName() {
        return User_Name;
    }

    public Boolean isSKIF() {
        return Is_SKIF;
    }

    public Boolean isAdmin() {
        return Is_Admin;
    }
}
