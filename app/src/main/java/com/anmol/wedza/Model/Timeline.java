package com.anmol.wedza.Model;

/**
 * Created by anmol on 12/26/2017.
 */

public class Timeline {
    String medialink,event,mediatype;

    public Timeline() {
    }

    public Timeline(String medialink, String event,String mediatype) {
        this.medialink = medialink;
        this.event = event;
        this.mediatype = mediatype;
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
