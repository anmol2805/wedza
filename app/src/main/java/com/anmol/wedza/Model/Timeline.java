package com.anmol.wedza.Model;


import java.util.Date;

/**
 * Created by anmol on 12/26/2017.
 */

public class Timeline {
    String medialink,event,mediatype,des,username,userid,postid,userpic;
    public Timeline() {
    }

    public Timeline(String medialink, String event, String mediatype, String des, String username, String userid, String postid,String userpic) {
        this.medialink = medialink;
        this.event = event;
        this.mediatype = mediatype;
        this.des = des;
        this.username = username;
        this.userid = userid;
        this.postid = postid;
        this.userpic = userpic;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getMedialink() {
        return medialink;
    }

    public void setMedialink(String medialink) {
        this.medialink = medialink;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }
}
