package com.example.videotophoto_grabphotosfromvideo.select_video.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.select_video.adapter.AdapterVideoFolder;
import com.example.videotophoto_grabphotosfromvideo.select_video.model.ModelFolder;

import java.util.ArrayList;

public class SelectVideoActivity extends AppCompatActivity {

    public AdapterVideoFolder adapterVideoList;
    protected RecyclerView recyclerView;
    ArrayList<ModelFolder> videoFolders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);

        recyclerView = findViewById(R.id.recyclerView_folder);
        videoFolders = new ArrayList<>();
        // loadVideo();
        selectFolder();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapterVideoList = new AdapterVideoFolder(this, videoFolders);
        recyclerView.setAdapter(adapterVideoList);

    }

    protected void selectFolder() {
        ArrayList<String> videoPaths = new ArrayList<>();
        Uri allVideosuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(allVideosuri, projection, null, null, null);
        try {
            cursor.moveToFirst();
            do {
                ModelFolder fold = new ModelFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String folderpaths = datapath.replace(name, "");
                if (!videoPaths.contains(folderpaths)) {
                    videoPaths.add(folderpaths);
                    fold.setPath(folderpaths);
                    fold.setName(folder);
                    videoFolders.add(fold);
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}