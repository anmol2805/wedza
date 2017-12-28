package com.anmol.wedza.Model;

import android.net.Uri;

/**
 * Created by anmol on 12/28/2017.
 */

public class Gallery {
    Uri uri;

    public Gallery() {
    }

    public Gallery(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}

