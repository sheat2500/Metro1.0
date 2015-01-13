package me.blueland.metro.model;

/**
 * Created by Te on 1/13/15.
 */
public class Entrance {



    String Description;
    String ID;
    String Lat;
    String Lon;
    String Name;
    String StationCode1;
    String StationCode2;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStationCode1() {
        return StationCode1;
    }

    public void setStationCode1(String stationCode1) {
        StationCode1 = stationCode1;
    }

    public String getStationCode2() {
        return StationCode2;
    }

    public void setStationCode2(String stationCode2) {
        StationCode2 = stationCode2;
    }




    public Entrance(String description, String ID, String lat, String lon, String name, String stationCode1, String stationCode2) {
        Description = description;
        this.ID = ID;
        Lat = lat;
        Lon = lon;
        Name = name;
        StationCode1 = stationCode1;
        StationCode2 = stationCode2;
    }




}
