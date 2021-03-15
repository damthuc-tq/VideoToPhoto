package com.example.videotophoto_grabphotosfromvideo.gallery.model;

public class ImageModel {
    String name;
    String pathName;

    public ImageModel( String name, String pathName) {
        this.name = name;
        this.pathName = pathName;
    }

    public ImageModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
