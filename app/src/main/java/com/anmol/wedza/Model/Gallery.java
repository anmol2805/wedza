package com.anmol.wedza.Model;

import android.net.Uri;

/**
 * Created by anmol on 12/28/2017.
 */

public class Gallery {
    String url;

    public Gallery() {
    }

    public Gallery(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

