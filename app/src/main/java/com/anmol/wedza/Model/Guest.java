package com.anmol.wedza.Model;

/**
 * Created by anmol on 12/27/2017.
 */

public class Guest {
    String name,profilepicturepath;

    public Guest() {
    }

    public Guest(String name,String profilepicturepath) {
        this.name = name;
        this.profilepicturepath = profilepicturepath;
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
