package com.example.videotophoto_grabphotosfromvideo.select_video.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.select_video.adapter.AdapterVideo;
import com.example.videotophoto_grabphotosfromvideo.select_video.model.ModelVideo;

import java.util.ArrayList;
import java.util.Locale;

public class VideoListActivity extends AppCompatActivity {
    ArrayList<ModelVideo> videoArrayList;
    AdapterVideo adapterVideo;
    RecyclerView recyclerView;
    ModelVideo modelVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        recyclerView = findViewById(R.id.recyclerView_video);
        videoArrayList = new ArrayList<>();
        try {
            loadVideo();
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            adapterVideo = new AdapterVideo(videoArrayList, this);
            recyclerView.setAdapter(adapterVideo);
        }catch (Exception e){
            startActivity(new Intent(this,SelectVideoActivity.class));
        }

    }
    protected void loadVideo() {
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] parameters = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION};
        Intent intent = getIntent();
        String folderName = intent.getStringExtra("name");
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        Cursor cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                parameters, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
        cursor.moveToFirst();
        do {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            modelVideo = new ModelVideo(id,name,duration, uri);
            videoArrayList.add(modelVideo);
        } while (cursor.moveToNext());
        cursor.close();
    }
}