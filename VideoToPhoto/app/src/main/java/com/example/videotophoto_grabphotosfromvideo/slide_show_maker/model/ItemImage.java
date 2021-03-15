package com.example.videotophoto_grabphotosfromvideo.slide_show_maker.model;

import android.net.Uri;

public class ItemImage {
    long id;
    String title;
    double size;
    Uri uri;

    public ItemImage(long id, String title, double size, Uri uri) {
        this.id = id;
        this.title = title;
        this.size = size;
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
