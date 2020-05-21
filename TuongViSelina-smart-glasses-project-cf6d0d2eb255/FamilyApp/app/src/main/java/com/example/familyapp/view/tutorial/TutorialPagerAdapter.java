package com.example.familyapp.view.tutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familyapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

public class TutorialPagerAdapter extends PagerAdapter {

    private ArrayList<Integer> images = new ArrayList<Integer>() {{
        add(R.drawable.img_location);
        add(R.drawable.img_danger);
        add(R.drawable.img_welcome);
    }};

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        View view = layoutInflater.inflate(R.layout.item_tutorial, container, false);
        AppCompatImageView img = view.findViewById(R.id.img_tutorial);
        img.setImageDrawable(ContextCompat.getDrawable(container.getContext(), images.get(position)));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
