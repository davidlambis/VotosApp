package com.votosapp.Models;

import java.util.Date;

public class User {

    public long User_Id_Local;
    public int User_Id;
    public long User_Type_Id;
    public long User_Type_Id_Remote;
    public String Name_User_Type;
    public long Referente_Id;
    public long Referente_Id_Local;
    public String Name_Referente;
    public long Sector_Id;
    public long Sector_Id_Remote;
    public String Name_Municipe;
    public String FirstName;
    public String LastName;
    public String Identification_Card;
    public String Profession;
    public String Birth_Date;
    public String Phone1;
    public String Phone2;
    public String Email;
    public String Address;
    public String Coords_Location;
    public String Have_Vehicle;
    public String Vehicle_Type;
    public String Vehicle_Plate;
    public String Password;
    public String Picture;
    public String Is_Leader;
    public int Department_Id;
    public int Id_Zone;

    public User(){
        this.User_Id_Local = User_Id_Local;
        this.User_Id = User_Id;
        this.User_Type_Id = User_Type_Id;
        this.User_Type_Id_Remote = User_Type_Id_Remote;
        this.Referente_Id = Referente_Id;
        this.Referente_Id_Local = Referente_Id_Local;
        this.Name_Referente = Name_Referente;
        this.Sector_Id = Sector_Id;
        this.Sector_Id_Remote = Sector_Id_Remote;
        this.Name_Municipe = Name_Municipe;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Identification_Card = Identification_Card;
        this.Profession = Profession;
        this.Birth_Date = Birth_Date;
        this.User_Id_Local = User_Id_Local;
        this.Email = Email;
        this.Address = Address;
        this.Coords_Location = Coords_Location;
        this.Have_Vehicle = Have_Vehicle;
        this.Vehicle_Type = Vehicle_Type;
        this.Vehicle_Plate = Vehicle_Plate;
        this.Password = Password;
        this.Picture = Picture;
        this.Is_Leader = Is_Leader;
        this.Name_User_Type = Name_User_Type;
        this.Department_Id = Department_Id;
        this.Id_Zone = Id_Zone;
    }

    public long getUser_Id_Local() {
        return User_Id_Local;
    }

    public void setUser_Id_Local(long user_Id_Local) {
        User_Id_Local = user_Id_Local;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public long getUser_Type_Id() {
        return User_Type_Id;
    }

    public void setUser_Type_Id(long user_Type_Id) {
        User_Type_Id = user_Type_Id;
    }

    public long getUser_Type_Id_Remote() {
        return User_Type_Id_Remote;
    }

    public void setUser_Type_Id_Remote(long user_Type_Id_Remote) {
        User_Type_Id_Remote = user_Type_Id_Remote;
    }

    public String getName_User_Type() {
        return Name_User_Type;
    }

    public void setName_User_Type(String name_User_Type) {
        Name_User_Type = name_User_Type;
    }

    public long getReferente_Id() {
        return Referente_Id;
    }

    public void setReferente_Id(long referente_Id) {
        Referente_Id = referente_Id;
    }

    public long getReferente_Id_Local() {
        return Referente_Id_Local;
    }

    public void setReferente_Id_Local(long referente_Id_Local) {
        Referente_Id_Local = referente_Id_Local;
    }

    public String getName_Referente() {
        return Name_Referente;
    }

    public void setName_Referente(String name_Referente) {
        Name_Referente = name_Referente;
    }

    public long getSector_Id() {
        return Sector_Id;
    }

    public void setSector_Id(long sector_Id) {
        Sector_Id = sector_Id;
    }

    public long getSector_Id_Remote() {
        return Sector_Id_Remote;
    }

    public void setSector_Id_Remote(long sector_Id_Remote) {
        Sector_Id_Remote = sector_Id_Remote;
    }

    public String getName_Municipe() {
        return Name_Municipe;
    }

    public void setName_Municipe(String name_Municipe) {
        Name_Municipe = name_Municipe;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getIdentification_Card() {
        return Identification_Card;
    }

    public void setIdentification_Card(String identification_Card) {
        Identification_Card = identification_Card;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }

    public String getBirth_Date() {
        return Birth_Date;
    }

    public void setBirth_Date(String birth_Date) {
        Birth_Date = birth_Date;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String phone1) {
        Phone1 = phone1;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String phone2) {
        Phone2 = phone2;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCoords_Location() {
        return Coords_Location;
    }

    public void setCoords_Location(String coords_Location) {
        Coords_Location = coords_Location;
    }

    public String getHave_Vehicle() {
        return Have_Vehicle;
    }

    public void setHave_Vehicle(String have_Vehicle) {
        Have_Vehicle = have_Vehicle;
    }

    public String getVehicle_Type() {
        return Vehicle_Type;
    }

    public void setVehicle_Type(String vehicle_Type) {
        Vehicle_Type = vehicle_Type;
    }

    public String getVehicle_Plate() {
        return Vehicle_Plate;
    }

    public void setVehicle_Plate(String vehicle_Plate) {
        Vehicle_Plate = vehicle_Plate;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getIs_Leader() {
        return Is_Leader;
    }

    public void setIs_Leader(String is_Leader) {
        Is_Leader = is_Leader;
    }

    public int getDepartment_Id() {
        return Department_Id;
    }

    public void setDepartment_Id(int department_Id) {
        Department_Id = department_Id;
    }

    public int getId_Zone() {
        return Id_Zone;
    }

    public void setId_Zone(int id_Zone) {
        Id_Zone = id_Zone;
    }
}
