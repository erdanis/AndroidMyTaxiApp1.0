package com.example.mytaxiapp10;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class drivers {
    public double latitud;
    public double longitud;
    public double speed;
    public String name;
    public int ranking;
    public int status;

    public drivers() {
    }

    public drivers(String name, double latitud, double longitud, int ranking, int status, double speed) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.name = name;
        this.ranking = ranking;
        this.status = status;
        this.speed = speed;
    }
}
