package com.example.videotophoto_grabphotosfromvideo.select_video.model;

import android.net.Uri;

public class ModelVideo {
    long id;
    String title;
    int duration;
    Uri uri;

    public ModelVideo(long id, String title, int duration, Uri uri) {
        this.id = id;
        this.title = title;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
