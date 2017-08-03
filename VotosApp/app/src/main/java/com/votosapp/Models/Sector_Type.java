package com.votosapp.Models;

public class Sector_Type {

    public long Sector_Type_Id_Local;
    public long Sector_Type_id;
    public String Name;

    public Sector_Type(){
        this.Sector_Type_Id_Local = Sector_Type_Id_Local;
        this.Sector_Type_id = Sector_Type_id;
        this.Name = Name;
    }

    public long getSector_Type_Id_Local() {
        return Sector_Type_Id_Local;
    }

    public void setSector_Type_Id_Local(long sector_Type_Id_Local) {
        Sector_Type_Id_Local = sector_Type_Id_Local;
    }

    public long getSector_Type_id() {
        return Sector_Type_id;
    }

    public void setSector_Type_id(long sector_Type_id) {
        Sector_Type_id = sector_Type_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
