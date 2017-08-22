package com.votosapp.Models;

public class Sesion {

    private int Id_Sesion;
    private int Estado_Sesion;
    private int User_Id_Sesion;
    private String Name_User_Type_Sesion;
    private String Firstname_Sesion;
    private String Lastname_Sesion;

    public Sesion() {
        this.Id_Sesion = Id_Sesion;
        this.Estado_Sesion = Estado_Sesion;
        this.User_Id_Sesion = User_Id_Sesion;
        this.Name_User_Type_Sesion = Name_User_Type_Sesion;
        this.Firstname_Sesion = Firstname_Sesion;
        this.Lastname_Sesion = Lastname_Sesion;
    }

    public int getId_Sesion() {
        return Id_Sesion;
    }

    public void setId_Sesion(int id_Sesion) {
        Id_Sesion = id_Sesion;
    }

    public int getEstado_Sesion() {
        return Estado_Sesion;
    }

    public void setEstado_Sesion(int estado_Sesion) {
        Estado_Sesion = estado_Sesion;
    }

    public int getUser_Id_Sesion() {
        return User_Id_Sesion;
    }

    public void setUser_Id_Sesion(int user_Id_Sesion) {
        User_Id_Sesion = user_Id_Sesion;
    }

    public String getName_User_Type_Sesion() {
        return Name_User_Type_Sesion;
    }

    public void setName_User_Type_Sesion(String name_User_Type_Sesion) {
        Name_User_Type_Sesion = name_User_Type_Sesion;
    }

    public String getFirstname_Sesion() {
        return Firstname_Sesion;
    }

    public void setFirstname_Sesion(String firstname_Sesion) {
        Firstname_Sesion = firstname_Sesion;
    }

    public String getLastname_Sesion() {
        return Lastname_Sesion;
    }

    public void setLastname_Sesion(String lastname_Sesion) {
        Lastname_Sesion = lastname_Sesion;
    }
}
