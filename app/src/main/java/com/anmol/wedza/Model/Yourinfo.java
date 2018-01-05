package com.anmol.wedza.Model;

/**
 * Created by anmol on 1/5/2018.
 */

public class Yourinfo {
    String username,fbpagelink,relation,team,status,profilepicturepath,currentwedding;

    public Yourinfo() {
    }

    public Yourinfo(String username, String fbpagelink, String relation, String team, String status, String profilepicturepath,String currentwedding) {
        this.username = username;
        this.fbpagelink = fbpagelink;
        this.relation = relation;
        this.team = team;
        this.status = status;
        this.profilepicturepath = profilepicturepath;
        this.currentwedding = currentwedding;
    }

    public String getCurrentwedding() {
        return currentwedding;
    }

    public void setCurrentwedding(String currentwedding) {
        this.currentwedding = currentwedding;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFbpagelink() {
        return fbpagelink;
    }

    public void setFbpagelink(String fbpagelink) {
        this.fbpagelink = fbpagelink;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilepicturepath() {
        return profilepicturepath;
    }

    public void setProfilepicturepath(String profilepicturepath) {
        this.profilepicturepath = profilepicturepath;
    }
}

