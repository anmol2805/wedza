package com.anmol.wedza.Model;

/**
 * Created by anmol on 12/27/2017.
 */

public class Guest {
    String name,profilepicturepath,team;
    Boolean keypeople,admin;

    public Guest() {
    }

    public Guest(String name, String profilepicturepath, String team, Boolean keypeople, Boolean admin) {
        this.name = name;
        this.profilepicturepath = profilepicturepath;
        this.team = team;
        this.keypeople = keypeople;
        this.admin = admin;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Boolean getKeypeople() {
        return keypeople;
    }

    public void setKeypeople(Boolean keypeople) {
        this.keypeople = keypeople;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getProfilepicturepath() {
        return profilepicturepath;
    }

    public void setProfilepicturepath(String profilepicturepath) {
        this.profilepicturepath = profilepicturepath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
