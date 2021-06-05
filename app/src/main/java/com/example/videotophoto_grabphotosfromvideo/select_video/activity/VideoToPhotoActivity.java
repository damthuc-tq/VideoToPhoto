package com.example.videotophoto_grabphotosfromvideo.select_video.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.UnicodeSetSpanner;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.gallery.activity.GalleryActivity;
import com.example.videotophoto_grabphotosfromvideo.gallery.fragment.FragmentImage;
import com.example.videotophoto_grabphotosfromvideo.gallery.model.ImageModel;
import com.example.videotophoto_grabphotosfromvideo.select_video.adapter.AdapterPhoto;
import com.example.videotophoto_grabphotosfromvideo.select_video.model.ModelPhoto;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoToPhotoActivity extends AppCompatActivity {
    VideoView videoView;
    RecyclerView rcView;
    SeekBar seekBar;
    TextView time_up_capture, sum_time_capture;
    Button quick_capture, time_capture;
    String title_video;
    String path;
    int duration;
    long id;
    Uri uri;
    Runnable runnable;
    Handler handler;
    MediaController mediaController;
    TextView tvSeconds;
    FileOutputStream fileOutputStream;
    MediaMetadataRetriever mediaMetadataRetriever;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String file_format, quality, size;
    int qualityImage, currentPosition;
    ArrayList<ModelPhoto> arrayList;
    ModelPhoto createdPhotos;
    Button btnStop;
    List<ModelPhoto> list = null;
    AdapterPhoto adapter;
    int progressDialog;
    int position;
    public final static String SCREENSHOTS_LOCATIONS = Environment.getExternalStorageDirectory() + "/DCIM/VideoToImage/";
    Button btnCancel, btnOK;
    Dialog dialog;
    String s;
    View view;
    boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_to_photo);
        setTitle("Video to photo playing");
        thamChieuVaKhoiTao();
        sharedPreferences();
        getIntentVideo();
        setTime();
        UpdateTimeVideo();
        mediaMetadataRetriever();
        runVideo();
        seekBar();
        setOnclick();
    }

    private void setOnclick() {
        time_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeCapture(state);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state!=false){
                    state = false;
                }
            }
        });
        quick_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new quickCap().execute();
                if (list != null) {
                    Toast.makeText(getApplicationContext(), "Đã chụp : " + arrayList.size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi : " + arrayList.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSeconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(VideoToPhotoActivity.this);
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_dialog_time_cupture, null);
                dialog.setContentView(view);
                EditText editText = dialog.findViewById(R.id.edit_second);
                btnOK = dialog.findViewById(R.id.btn_ok);
                btnCancel = dialog.findViewById(R.id.btn_cancel);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s = editText.getText().toString();
                        tvSeconds.setText(s);
                        dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    private void setTimeCapture(boolean state) {
        long l = Long.parseLong(tvSeconds.getText().toString());
        int delayMilis = (int) (l * 1000);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (videoView.getCurrentPosition()/1000 < videoView.getDuration()/1000) {
                    if (state){
                        new quickCap().execute();
                        handler.postDelayed(this, delayMilis);
                        Toast.makeText(getApplicationContext(), "Ảnh đã chụp: " + arrayList.size(), Toast.LENGTH_SHORT).show();
                        Log.e("DDDDDĐ: ", "all: " + videoView.getDuration() + ":: index: " + videoView.getCurrentPosition() + "    ARRAYLIST: "+arrayList.size());
                    }else {
                        Toast.makeText(getApplicationContext(), "Đã dừng lại", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Đã chụp xong", Toast.LENGTH_SHORT).show();
                }
            }
        }, delayMilis);
    }

    private void getQuality(String quality) {
        if (quality.equals("Best")) {
            qualityImage = 100;
        } else if (quality.equals("Very High")) {
            qualityImage = 80;
        } else if (quality.equals("High")) {
            qualityImage = 60;
        } else if (quality.equals("Medium")) {
            qualityImage = 40;
        } else {
            qualityImage = 20;
        }
    }

    public void UpdateTimeVideo() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                time_up_capture.setText(dateFormat.format(videoView.getCurrentPosition()));
                handler.postDelayed(this, 500);
                //update seekBar
                seekBar.setProgress(videoView.getCurrentPosition());
            }
        }, 100);
    }

    public void setTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        sum_time_capture.setText(dateFormat.format(duration));
        //seekBar
        seekBar.setMax(videoView.getDuration());
    }

    public void getIntentVideo() {
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        title_video = intent.getStringExtra("title");
        duration = intent.getIntExtra("duration", 0);
        path = intent.getStringExtra("path_video");
    }

    private void runVideo() {
        videoView.setVideoURI(uri);
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }

    private void mediaMetadataRetriever() {
        uri = Uri.parse(path);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, uri);
    }

    private void sharedPreferences() {
        sharedPreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        file_format = sharedPreferences.getString("file_format", "JPG");
        quality = sharedPreferences.getString("quality", "High");
        size = sharedPreferences.getString("size", "1x");
        getQuality(quality);
    }

    private void seekBar() {
        //seekBar
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(videoView.getDuration());
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                    onResume();
                }
                progressDialog = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void thamChieuVaKhoiTao() {
        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.id_seekbar);
        time_up_capture = findViewById(R.id.id_timeUp);
        sum_time_capture = findViewById(R.id.id_timeSum);
        quick_capture = findViewById(R.id.id_quick_capture);
        time_capture = findViewById(R.id.id_time_capture);
        tvSeconds = findViewById(R.id.tv_second);
        btnStop = findViewById(R.id.btnStop);
        rcView = findViewById(R.id.rcView);
        arrayList = new ArrayList<>();
        handler = new Handler();
    }

    private void videoToPhoto(File file) {
        Bitmap imageBitmap;
        currentPosition = videoView.getCurrentPosition();
        position = currentPosition * 1000;
        imageBitmap = mediaMetadataRetriever.getFrameAtTime(position);
        try {
            if (file_format.equals("JPG")) {
                fileOutputStream = new FileOutputStream(file + File.separator + System.currentTimeMillis() + ".jpg");
                if (fileOutputStream != null) {
                    if (!imageBitmap.compress(Bitmap.CompressFormat.JPEG, qualityImage, fileOutputStream)) {
                        Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                fileOutputStream = new FileOutputStream(file + File.separator + System.currentTimeMillis() + ".png");
                if (fileOutputStream != null) {
                    if (!imageBitmap.compress(Bitmap.CompressFormat.PNG, qualityImage, fileOutputStream)) {
                        Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            createdPhotos = new ModelPhoto(imageBitmap, fileOutputStream + "", file.getAbsolutePath(), Integer.parseInt(file.length() + ""), false);
            arrayList.add(createdPhotos);
            list = new ArrayList<>();
            list.add(createdPhotos);
            Log.e("AAAAAAAAAAAAAA: ", "" + arrayList.size());
            adapter = new AdapterPhoto(VideoToPhotoActivity.this, arrayList);
        } catch (FileNotFoundException e) {
            Log.e("AAAAAAAAAAAAAA", "" + e);
        } catch (IOException e) {
            Log.e("bbbbbbbbbbbbbbbb", "" + e);
        }


    }

    public class quickCap extends AsyncTask<Void, ArrayList<ModelPhoto>, ArrayList<ModelPhoto>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            currentPosition = videoView.getCurrentPosition();
        }

        @Override
        protected void onPostExecute(ArrayList<ModelPhoto> createdPhotos) {
            super.onPostExecute(createdPhotos);
            rcView.setLayoutManager(new LinearLayoutManager(VideoToPhotoActivity.this, LinearLayoutManager.HORIZONTAL, false));
            rcView.setAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(ArrayList<ModelPhoto>... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<ModelPhoto> doInBackground(Void... voids) {
            File file = new File(SCREENSHOTS_LOCATIONS);
            if (!file.exists()) {
                file.mkdirs();
            } else {
                videoToPhoto(file);
            }
            return arrayList;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_video_cupture, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                startActivity(new Intent(this, GalleryActivity.class));
                break;
            case R.id.camera:
                new quickCap().execute();
                if (list != null) {
                    Toast.makeText(getApplicationContext(), "Đã chụp : " + arrayList.size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi : " + arrayList.size(), Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.pause();
        handler.removeCallbacks(runnable);
    }
}