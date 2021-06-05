package com.example.videotophoto_grabphotosfromvideo.slide_show_maker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.slide_show_maker.model.ItemFolder;
import com.example.videotophoto_grabphotosfromvideo.slide_show_maker.model.ItemImage;
import com.example.videotophoto_grabphotosfromvideo.slide_show_maker.model.ItemImageSelected;

import java.util.ArrayList;

public class SelectImageToSlideActivity extends AppCompatActivity {
    RecyclerView recyclerView_folder, recyclerView_image, recyclerView_image_selected;
    ArrayList<ItemFolder> itemFolders;
    ArrayList<ItemImage> itemImages;
    AdapterFolderImage adapterFolderImage;
    AdapterImage adapterImage;
    public ItemImage itemImage;
    public AdapterImageSelected adapterImageSelected;
    ArrayList<ItemImageSelected> imageSelecteds = new ArrayList<>();
    ItemImageSelected itemImageSelected;
    Uri uri;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_to_slide);
        recyclerView_folder = findViewById(R.id.recycler_folder_image);
        recyclerView_image = findViewById(R.id.recyclerView_image_inSlide);
        recyclerView_image_selected = findViewById(R.id.image_selected);
        selectFolder();
        adapterFolderImage = new AdapterFolderImage(this, itemFolders);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false);
        recyclerView_folder.setLayoutManager(gridLayoutManager);
        recyclerView_folder.setAdapter(adapterFolderImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done_image_to_slide, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                Intent intent = new Intent(getApplicationContext(), SlideShowMakerActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    protected void selectFolder() {
        itemFolders = new ArrayList<>();
        ArrayList<String> folderPaths = new ArrayList<>();
        Uri allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(allVideosuri, projection, null, null, null);
        try {
            cursor.moveToFirst();
            do {
                ItemFolder fold = new ItemFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                String folderpaths = datapath.replace(name, "");
                if (!folderPaths.contains(folderpaths)) {
                    folderPaths.add(folderpaths);
                    fold.setPath(folderpaths);
                    fold.setName(folder);
                    itemFolders.add(fold);
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Clear(View view) {
        imageSelecteds.clear();
        adapterImageSelected.notifyDataSetChanged();
    }

    public void delete(){
        adapterImageSelected.notifyDataSetChanged();
    }

    // adapter hiển thị folder chứa ảnh
    public class AdapterFolderImage extends RecyclerView.Adapter<AdapterFolderImage.ViewHolder> {
        Context context;
        ArrayList<ItemFolder> itemFolders = new ArrayList<>();

        public AdapterFolderImage(Context context, ArrayList<ItemFolder> itemFolders) {
            this.context = context;
            this.itemFolders = itemFolders;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_folder_image, null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItemFolder itemFolder = itemFolders.get(position);
            holder.textView.setText(itemFolder.getName());
            Glide.with(context).load(R.drawable.icon_folder).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemFolder folder = itemFolders.get(position);
                    loadImage(folder.getName());
                    showImage();
                }
            });
        }

        // Load ảnh lên recyclerView.
        public void showImage() {
            adapterImage = new AdapterImage(context, itemImages);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false);
            recyclerView_image.setLayoutManager(gridLayoutManager);
            recyclerView_image.setAdapter(adapterImage);
        }

        //Tìm kiếm ảnh theo tập tin.
        public void loadImage(String folder) {
            itemImages = new ArrayList<>();
            String selection = MediaStore.Images.Media.DATA + " like?";
            String[] parameters = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE};
            String[] selectionArgs = new String[]{"%" + folder + "%"};
            Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    parameters, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
            cursor.moveToFirst();
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                double size = cursor.getDouble(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                itemImage = new ItemImage(id, name, size, uri);
                itemImages.add(itemImage);
            } while (cursor.moveToNext());
            cursor.close();
        }

        @Override
        public int getItemCount() {
            return itemFolders.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.icon_folder);
                textView = itemView.findViewById(R.id.tv_title_image);
            }
        }
    }

    public class AdapterImage extends RecyclerView.Adapter<AdapterImage.ViewHolder> {
        Context context;
        ArrayList<ItemImage> itemImages;

        public AdapterImage(Context context, ArrayList<ItemImage> itemImages) {
            this.context = context;
            this.itemImages = itemImages;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItemImage itemImage = itemImages.get(position);
            holder.textView.setText(itemImage.getTitle());
            Glide.with(context).load(itemImage.getUri()).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemImage itemImage = itemImages.get(position);
                    uri = itemImage.getUri();
                    id = itemImage.getId();
                    showImageSelected();
                }
            });
        }

        // load ảnh đã chọn lên recyclerView===
        public void showImageSelected() {
            itemImageSelected = new ItemImageSelected(id, uri);
            imageSelecteds.add(itemImageSelected);
            adapterImageSelected = new AdapterImageSelected(context, imageSelecteds);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false);
            recyclerView_image_selected.setLayoutManager(gridLayoutManager);
            recyclerView_image_selected.setAdapter(adapterImageSelected);
            adapterImageSelected.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return itemImages.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image);
                textView = itemView.findViewById(R.id.tv_title_image);
            }
        }
    }

    public class AdapterImageSelected extends RecyclerView.Adapter<AdapterImageSelected.ViewHolder> {
        Context context;
        ArrayList<ItemImageSelected> imageSelecteds;

        public AdapterImageSelected(Context context, ArrayList<ItemImageSelected> imageSelecteds) {
            this.context = context;
            this.imageSelecteds = imageSelecteds;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image_selected, null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            itemImageSelected = imageSelecteds.get(position);
            Glide.with(context).load(itemImageSelected.getUri()).into(holder.imageView);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        imageSelecteds.remove(position);
                        delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return imageSelecteds.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageButton imageButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageButton = itemView.findViewById(R.id.btn_delete_image);
                imageView = itemView.findViewById(R.id.image_selected_1);
            }
        }
    }

}