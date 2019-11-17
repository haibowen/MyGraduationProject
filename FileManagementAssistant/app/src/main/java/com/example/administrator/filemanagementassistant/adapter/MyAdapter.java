package com.example.administrator.filemanagementassistant.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyAdapter extends PagerAdapter {
    private ArrayList<View> pageviews;
    public MyAdapter(ArrayList<View> pageviews) {
        this.pageviews=pageviews;
    }
    @Override
    public int getCount() {
        return pageviews.size();
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o==view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        if (pageviews.get(position)!=null){
            container.removeView(pageviews.get(position));
        }
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(pageviews.get(position));
        return pageviews.get(position);
    }
}
