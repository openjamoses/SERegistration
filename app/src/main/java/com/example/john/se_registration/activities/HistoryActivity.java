package com.example.john.se_registration.activities;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.john.se_registration.R;
import com.example.john.se_registration.adapters.Other_Adapters;
import com.example.john.se_registration.core.ReturnCursor;
import com.example.john.se_registration.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.example.john.se_registration.utils.Constants.config.REGISTRATION_ID;
import static com.example.john.se_registration.utils.Constants.config.TABLE_REGISTRATION;

/**
 * Created by john on 4/27/18.
 */

public class HistoryActivity extends AppCompatActivity {
    private ListView listView;
    private TextView noText;
    private Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        listView = (ListView) findViewById(R.id.listView);
        noText = (TextView) findViewById(R.id.noText);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setView();
    }

    private void setView(){
        List<String> regno = new ArrayList<>();
        List<String> semester = new ArrayList<>();
        List<String> courses = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<String> pictures = new ArrayList<>();


        String query = "SELECT * FROM "+TABLE_REGISTRATION+" ORDER BY "+REGISTRATION_ID+" DESC";
        try{
            Cursor cursor = ReturnCursor.getCursor(query,context);
            int count = 0;
            if (cursor.moveToFirst()){
                do {
                    count ++;
                    regno.add(cursor.getString(cursor.getColumnIndex(Constants.config.REGISTRATION_NUMBER)));
                    semester.add(cursor.getString(cursor.getColumnIndex(Constants.config.SEMESTER)));
                    courses.add(cursor.getString(cursor.getColumnIndex(Constants.config.COURSE)));
                    ids.add(cursor.getInt(cursor.getColumnIndex(Constants.config.REGISTRATION_ID)));
                    pictures.add(cursor.getString(cursor.getColumnIndex(Constants.config.PICTURE_NAME)));

                }while (cursor.moveToNext());
            }
            if (count > 0){
                noText.setVisibility(View.GONE);
            }
            Other_Adapters adapters = new Other_Adapters(context,regno,semester,courses,pictures,ids);
            listView.setAdapter(adapters);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
