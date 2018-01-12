package com.anmol.wedza.Model;

/**
 * Created by anmol on 1/9/2018.
 */

public class Alert {
    String username,team,content,uid;

    public Alert() {
    }

    public Alert(String username, String team, String content, String uid) {
        this.username = username;
        this.team = team;
        this.content = content;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
