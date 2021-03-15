package com.example.videotophoto_grabphotosfromvideo.gallery.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.videotophoto_grabphotosfromvideo.R;
import com.example.videotophoto_grabphotosfromvideo.gallery.fragment.FragmentImage;
import com.example.videotophoto_grabphotosfromvideo.gallery.fragment.FragmentVideo;
import com.example.videotophoto_grabphotosfromvideo.gallery.model.ImageModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String listTab[] = {"Video", "Image"};
    private FragmentVideo fragmentVideo;
    private FragmentImage fragmentImage;
    ArrayList<ImageModel> itemImages;
    ImageModel imageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTitle("Gallery");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            fragmentImage = new FragmentImage();
            fragmentVideo = new FragmentVideo();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return fragmentVideo;
            } else if (position == 1) {
                return fragmentImage;
            }
            return null;
        }

        @Override
        public int getCount() {
            return listTab.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return listTab[position];
        }
    }
}