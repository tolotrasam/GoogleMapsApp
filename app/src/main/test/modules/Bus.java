package ca.joel.mapapp.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Admin 2 on 21/08/2017.
 */

public class Bus {
    private double lon, lat, speed;
    private String details, id, name;
    private List<LatLng> path;
    private List<String> bus_stop_ids;
    private List<Bus_Stop> bus_stops;

    public Bus_Stop getLastBusStop() {
        return lastBusStop;
    }

    private Bus_Stop lastBusStop;
    public Bus( ){

    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LatLng> getPath() {
        return path;
    }

    public void setPath(List<LatLng> path) {
        this.path = path;
    }

    public List<String> getBus_stop_ids() {
        return bus_stop_ids;
    }

    public void setBus_stop_ids(List<String> bus_stop_ids) {
        this.bus_stop_ids = bus_stop_ids;
    }

    public List<Bus_Stop> getBus_stops() {
        return bus_stops;
    }

    public void setBus_stops(List<Bus_Stop> bus_stops) {
        this.bus_stops = bus_stops;
    }
}
