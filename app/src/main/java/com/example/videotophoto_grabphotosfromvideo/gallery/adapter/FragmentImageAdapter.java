package com.example.videotophoto_grabphotosfromvideo.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.gallery.activity.ViewPhotoActivity;
import com.example.videotophoto_grabphotosfromvideo.gallery.model.ImageModel;

import java.util.ArrayList;

public class FragmentImageAdapter extends RecyclerView.Adapter<FragmentImageAdapter.ViewHolder> {
    Context context;
    ArrayList<ImageModel> arrayList;
    int resource;
    LayoutInflater inflater;

    public FragmentImageAdapter(Context context, ArrayList<ImageModel> arrayList, int resource) {
        this.context = context;
        this.arrayList = arrayList;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageModel imageModel = arrayList.get(position);
        holder.imageView.setImageURI(Uri.parse(imageModel.getPathName()));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPhotoActivity.class);
                intent.putExtra("index",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageOfVD);
        }
    }
}
