package com.example.addhotelandlocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewpageAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int[] layout={R.layout.layout_1,
    R.layout.layout_2,
    R.layout.layout_3};

    public ViewpageAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return layout.length;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(layout[position],container,false);
        container.addView(view);
        return view;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view=(View) object;
        container.removeView(view);
    }


}
