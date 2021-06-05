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
import com.example.videotophoto_grabphotosfromvideo.select_video.activity.VideoListActivity;
import com.example.videotophoto_grabphotosfromvideo.select_video.model.ModelFolder;
import com.example.videotophoto_grabphotosfromvideo.select_video.model.ModelVideo;

import java.util.ArrayList;

public class AdapterVideoFolder extends RecyclerView.Adapter<AdapterVideoFolder.ViewHolder> {
    ArrayList<ModelFolder> videoArrayFolder = new ArrayList<>();
    ArrayList<ModelVideo> videoArrayList = new ArrayList<>();
    AdapterVideo adapterVideo;
    Context context;

    public AdapterVideoFolder(Context context, ArrayList<ModelFolder> videoArrayList) {
        this.videoArrayFolder = videoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelFolder modelFolder = videoArrayFolder.get(position);
        holder.tv_title.setText(modelFolder.getName());
        Glide.with(context).load(R.drawable.icon_folder).into(holder.imageView_thumbnail);
        holder.imageView_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoListActivity.class);
                intent.putExtra("name",modelFolder.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoArrayFolder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_thumbnail;
        TextView tv_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            imageView_thumbnail = itemView.findViewById(R.id.icon_thumbnail);
        }
    }
}
