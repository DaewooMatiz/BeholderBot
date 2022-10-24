package org.example;

public class User {
    public Long User_ID;
    public String User_Name;
    public Boolean Is_SKIF;
    public Boolean Is_Admin;

    public User(Long User_IDt, String User_Namet, Boolean Is_SKIFt, Boolean Is_Admint ){
        User_ID = User_IDt;
        User_Name = User_Namet;
        Is_SKIF = Is_SKIFt;
        Is_Admin = Is_Admint;

    }
}
