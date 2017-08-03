package com.votosapp.Models;

public class UserType {

    public long User_Type_Id_Local;
    public long User_Type_Id;
    public String Name;

    public UserType(){
        this.User_Type_Id_Local = User_Type_Id_Local;
        this.User_Type_Id = User_Type_Id;
        this.Name = Name;
    }

    public long getUser_Type_Id_Local() {
        return User_Type_Id_Local;
    }

    public void setUser_Type_Id_Local(long user_Type_Id_Local) {
        User_Type_Id_Local = user_Type_Id_Local;
    }

    public long getUser_Type_Id() {
        return User_Type_Id;
    }

    public void setUser_Type_Id(long user_Type_Id) {
        User_Type_Id = user_Type_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
