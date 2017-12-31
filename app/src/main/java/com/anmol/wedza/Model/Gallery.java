package com.anmol.wedza.Model;

import android.net.Uri;

/**
 * Created by anmol on 12/28/2017.
 */

public class Gallery {
    String url,mediatype,event;

    public Gallery() {
    }

    public Gallery(String url, String mediatype, String event) {
        this.url = url;
        this.mediatype = mediatype;
        this.event = event;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}

