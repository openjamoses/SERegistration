package com.example.john.se_registration.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.john.se_registration.R;

import java.util.List;

/**
 * Created by john on 11/6/17.
 */

public class Other_Adapters extends BaseAdapter {
    Context context;
    List<String> regno,semester,courses,picture;

    List<Integer> ids;

    LayoutInflater inflter;
    public Other_Adapters(Context applicationContext, List<String> regno, List<String> semester, List<String> courses,List<String> picture, List<Integer>  ids) {
        this.context = applicationContext;
        this.regno = regno;
        this.semester = semester;
        this.ids = ids;
        this.courses = courses;
        this.picture = picture;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return regno.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list, null);
        try {
            TextView regText = (TextView) view.findViewById(R.id.regText);
            TextView semText = (TextView) view.findViewById(R.id.semText);
            TextView courseText = (TextView) view.findViewById(R.id.courseText);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.pic_layout) ;

            regText.setText(regno.get(i));
            semText.setText(semester.get(i));
            courseText.setText(courses.get(i));

            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,10,10,10);
            imageView.setLayoutParams(layoutParams);
            String[] pics = picture.get(i).split("/");

            for (int a=0; a<pics.length; a++){
                try {
                    if (!pics[a].equals("")) {
                        Glide.with(context)
                                .load(Uri.parse(pics[a]))
                                .override(612, 816)
                                .into(imageView);
                        layout.addView(imageView);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }
}
