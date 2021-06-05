package com.example.videotophoto_grabphotosfromvideo.slide_show_maker.model;

import android.net.Uri;

public class ItemImageSelected {
    long id;
    Uri uri;

    public ItemImageSelected(long id, Uri uri) {
        this.id = id;
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
