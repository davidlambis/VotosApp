package com.votosapp.Models;

public class Sector {

    public long Sector_Id_Local;
    public long Sector_Id;
    public long Sector_Type_id;
    public long Zone_Id;
    public String Name;
    public String Description;
    public String Latitude;
    public String Longitude;
    public int City_Id;

    public Sector(){
        this.Sector_Id_Local = Sector_Id_Local;
        this.Sector_Id = Sector_Id;
        this.Sector_Type_id = Sector_Type_id;
        this.Zone_Id = Zone_Id;
        this.Name = Name;
        this.Description = Description;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.City_Id = City_Id;
    }

    public long getSector_Id_Local() {
        return Sector_Id_Local;
    }

    public void setSector_Id_Local(long sector_Id_Local) {
        Sector_Id_Local = sector_Id_Local;
    }

    public long getSector_Id() {
        return Sector_Id;
    }

    public void setSector_Id(long sector_Id) {
        Sector_Id = sector_Id;
    }

    public long getSector_Type_id() {
        return Sector_Type_id;
    }

    public void setSector_Type_id(long sector_Type_id) {
        Sector_Type_id = sector_Type_id;
    }

    public long getZone_Id() {
        return Zone_Id;
    }

    public void setZone_Id(long zone_Id) {
        Zone_Id = zone_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public int getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(int city_Id) {
        City_Id = city_Id;
    }
}

