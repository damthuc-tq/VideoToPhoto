package com.example.videotophoto_grabphotosfromvideo.gallery.fragment;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.gallery.activity.GalleryActivity;
import com.example.videotophoto_grabphotosfromvideo.gallery.adapter.FragmentImageAdapter;
import com.example.videotophoto_grabphotosfromvideo.gallery.model.ImageModel;
import com.example.videotophoto_grabphotosfromvideo.select_video.activity.VideoToPhotoActivity;
import com.example.videotophoto_grabphotosfromvideo.slide_show_maker.model.ItemImage;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;

public class FragmentImage extends Fragment {
    Context context;
    RecyclerView rcView;
    FragmentImageAdapter adapter;
    ArrayList<ImageModel> arrayList;
    ImageModel imageModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        rcView = view.findViewById(R.id.rcImage);
        loadImage();
        return view;
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
            adapter = new FragmentImageAdapter(getContext(), arrayList, R.layout.item_image_of_photo);
            rcView.setLayoutManager(new GridLayoutManager(context,4,RecyclerView.VERTICAL, false));
            rcView.setAdapter(adapter);
        }
    }

}