package com.example.videotophoto_grabphotosfromvideo.gallery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.gallery.model.ImageModel;
import com.example.videotophoto_grabphotosfromvideo.select_video.activity.VideoToPhotoActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ViewPhotoActivity extends AppCompatActivity {
    ImageView imageView;
    ScaleGestureDetector scaleGestureDetector;
    float scaleFactor = 1f;
    ArrayList<ImageModel> arrayList;
    ImageModel imageModel;
    //
    int position = 0;
    GestureDetector gestureDetector;
    int SWIPE_THRESHOLD = 100;
    int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_photo);
        imageView = findViewById(R.id.showImage);
        Intent intent = getIntent();
        position = intent.getIntExtra("index", 0);
        loadImage();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListiener());
        Glide.with(getApplicationContext()).load(arrayList.get(position).getPathName()).into(imageView);
        gestureDetector = new GestureDetector(this, new MyGesture());
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    class MyGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                position--;
                if (position < 0) {
                    position = 0;
                }
                Glide.with(getApplicationContext()).load(arrayList.get(position).getPathName()).into(imageView);
            }
            if (e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                position++;
                if (position > arrayList.size() - 1) {
                    position = arrayList.size() - 1;
                }
                Glide.with(getApplicationContext()).load(arrayList.get(position).getPathName()).into(imageView);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void loadImage() {
        arrayList = new ArrayList<>();
        File photoFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM/VideoToImage/");
        if (photoFolder.exists()) {
            File[] allFiles = photoFolder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                }
            });
            for (int i = 0; i < allFiles.length; i++) {
                String imageName = allFiles[i].getName();
                String pathName = allFiles[i].getAbsolutePath();
                imageModel = new ImageModel(imageName, pathName);
                arrayList.add(imageModel);
                Log.e("get image", String.valueOf(arrayList.get(i).getName()) + pathName);
            }
            Log.e("Size file image: ", String.valueOf(arrayList.size()));
        }
    }

    class ScaleListiener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            if (scaleFactor < 10f) {
                if (scaleFactor>10f){
                    scaleFactor=10f;
                }else if (scaleFactor<0.8f){
                    scaleFactor = 1f;
                }
                imageView.setScaleX(scaleFactor);
                imageView.setScaleY(scaleFactor);
                imageView.animate();
            }
            return true;
        }
    }
}