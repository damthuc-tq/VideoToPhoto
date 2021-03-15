package com.example.videotophoto_grabphotosfromvideo.select_video.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.gallery.fragment.FragmentImage;
import com.example.videotophoto_grabphotosfromvideo.select_video.model.ModelPhoto;

import java.util.ArrayList;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.ViewHolder> {
    Context context;
    ArrayList<ModelPhoto> arrayList;
    LayoutInflater inflater;
    public AdapterPhoto(Context context, ArrayList<ModelPhoto> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.item_photos, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelPhoto modelPhoto = arrayList.get(position);
        Glide.with(context).load(Uri.parse(modelPhoto.getPhotoPath())).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FragmentImage.class));
            }
        });
    }
    @Override
    public int getItemCount() {
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_photo);
        }
    }
}
