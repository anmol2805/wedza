package com.anmol.wedza.Model;

import android.net.Uri;

/**
 * Created by anmol on 12/28/2017.
 */

public class Gallery {
    String url,mediatype;

    public Gallery() {
    }

    public Gallery(String url,String mediatype) {
        this.url = url;
        this.mediatype = mediatype;
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
}

