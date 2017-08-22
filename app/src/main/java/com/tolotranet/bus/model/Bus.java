package com.tolotranet.bus.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Admin 2 on 21/08/2017.
 */

public class Bus {
    private double lon, lat, speed, alt;
    private String details, id, name, origin, destination;
    private String path;
    private List<String> bus_stop_ids;
    private List<Bus_Stop> bus_stops;

    private List <LatLng> pathHistory;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
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


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public List<LatLng> getPathHistory() {
        return pathHistory;
    }

    public void setPathHistory(List<LatLng> pathHistory) {
        this.pathHistory = pathHistory;
    }

    public LatLng getPosition() {
        if (this.pathHistory != null && this.pathHistory.size()!= 0) {
            this.lat = pathHistory.get(pathHistory.size()-1).latitude;
            this.lon = pathHistory.get(pathHistory.size()-1).longitude;

        }
        return new LatLng(this.getLat(), this.getLon());
    }
}
