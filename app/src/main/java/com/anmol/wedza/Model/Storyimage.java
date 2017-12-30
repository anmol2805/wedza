package com.anmol.wedza.Model;

/**
 * Created by anmol on 12/30/2017.
 */

public class Storyimage {
    String medialink,mediatype;

    public Storyimage() {
    }

    public Storyimage(String medialink, String mediatype) {
        this.medialink = medialink;
        this.mediatype = mediatype;
    }

    public String getMedialink() {
        return medialink;
    }

    public void setMedialink(String medialink) {
        this.medialink = medialink;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }
}
