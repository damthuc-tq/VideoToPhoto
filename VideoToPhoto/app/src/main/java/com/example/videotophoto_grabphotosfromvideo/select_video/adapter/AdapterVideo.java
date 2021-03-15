package com.example.videotophoto_grabphotosfromvideo.select_video.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.select_video.activity.VideoToPhotoActivity;
import com.example.videotophoto_grabphotosfromvideo.select_video.model.ModelVideo;

import java.util.ArrayList;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.ViewHolder>{
    ArrayList<ModelVideo> videoArrayList = new ArrayList<>();
    Context context;

    public AdapterVideo(ArrayList<ModelVideo> videoArrayList, Context context) {
        this.videoArrayList = videoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        AdapterVideo.ViewHolder viewHolder = new AdapterVideo.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelVideo modelVideo = videoArrayList.get(position);
        holder.textView.setText(modelVideo.getTitle());
        Glide.with(context).load(modelVideo.getUri()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelVideo modelVideo = videoArrayList.get(position);
                String  strings = String.valueOf(modelVideo.getUri());
                Intent intent = new Intent(context, VideoToPhotoActivity.class);
                intent.putExtra("id",modelVideo.getId());
                intent.putExtra("title",modelVideo.getTitle());
                intent.putExtra("path_video",strings);
                intent.putExtra("duration",modelVideo.getDuration());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title_video);
            imageView = itemView.findViewById(R.id.image_video);
        }
    }
}
