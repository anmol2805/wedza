package com.anmol.wedza.Model;

import java.sql.Timestamp;

/**
 * Created by anmol on 1/8/2018.
 */

public class Event {
    String eventname,eventdes,eventimg,eventlocation,team;
    Timestamp time;

    public Event() {
    }

    public Event(String eventname, String eventdes, String eventimg, String eventlocation, String team, Timestamp time) {
        this.eventname = eventname;
        this.eventdes = eventdes;
        this.eventimg = eventimg;
        this.eventlocation = eventlocation;
        this.team = team;
        this.time = time;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventdes() {
        return eventdes;
    }

    public void setEventdes(String eventdes) {
        this.eventdes = eventdes;
    }

    public String getEventimg() {
        return eventimg;
    }

    public void setEventimg(String eventimg) {
        this.eventimg = eventimg;
    }

    public String getEventlocation() {
        return eventlocation;
    }

    public void setEventlocation(String eventlocation) {
        this.eventlocation = eventlocation;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
