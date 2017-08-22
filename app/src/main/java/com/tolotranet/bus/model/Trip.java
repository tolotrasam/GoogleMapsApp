package com.tolotranet.bus.model;

/**
 * Created by Tolotra Samuel on 21/08/2017.
 */

public class Trip {
    private String destination, departure, time, distance;
    private int timeInMinute;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setTimeInMinute(int timeInMinute) {
        this.timeInMinute = timeInMinute;
    }

    public Integer getTimeInMinute() {
        return timeInMinute;
    }
}
