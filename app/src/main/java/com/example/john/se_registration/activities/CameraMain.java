package com.example.john.se_registration.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.john.se_registration.R;
import com.example.john.se_registration.dbOperations.Registration;
import com.example.john.se_registration.utils.DateTime;
import com.example.john.se_registration.utils.Directory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
/**
 * Created by john on 1/3/18.
 */

public class CameraMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private Button btn_submit;
    private EditText input_regno,input_sem,input_course;
    private LinearLayout pic_layout;
    private Context context = this;
    DrawerLayout drawer;
    private String picture_url = "";
    private static final String TAG = "Camera";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try{
            btn_submit = (Button) findViewById(R.id.btn_submit);
            input_regno = (EditText) findViewById(R.id.input_regno);
            input_sem = (EditText) findViewById(R.id.input_sem);
            input_course = (EditText) findViewById(R.id.input_course);
            pic_layout = (LinearLayout) findViewById(R.id.pic_layout);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            //staticPaymets();
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openCamera();
                }
            });
            fab.setVisibility(View.GONE);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (drawer.isDrawerOpen(Gravity.START)) {
                        drawer.closeDrawer(Gravity.START);
                    } else {
                        drawer.openDrawer(Gravity.START);
                    }
                }
            });

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reg_number = input_regno.getText().toString();
                    String semester = input_sem.getText().toString();
                    String course = input_course.getText().toString();

                    if (reg_number.equals("")){
                        input_regno.setError("Invalid Regnumber");
                    }
                    if (semester.equals("")){
                        input_sem.setError("Invalid Semester");
                    }
                    if (course.equals("")){
                        input_course.setError("Invalid Course");
                    }
                    if (pic_layout.equals("")){
                        Toast.makeText(context,"No pictures captured..!", Toast.LENGTH_SHORT).show();
                    }
                    if (!reg_number.equals("") && !semester.equals("") && !course.equals("") && !picture_url.equals("")){
                        String message = new Registration(context).save(DateTime.getCurrentDate(),DateTime.getCurrentTime(),reg_number,semester,course,picture_url,1);
                        Toast.makeText(context, message,Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                input_regno.setText("");
                                input_sem.setText("");
                                input_course.setText("");
                                pic_layout.removeAllViews();
                            }
                        });

                    }else {
                        Toast.makeText(context,"No pictures captured..!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //toggleDrawer(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(photo);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,10,10,10);
            imageView.setLayoutParams(layoutParams);
            pic_layout.addView(imageView);
            //capturedImage.setImageBitmap(photo);
            try{
                saveImage(photo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void saveImage(Bitmap finalBitmap) {
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String iname = "Image-" + n + ".jpg";
        File file = new File(Directory.create(), iname);
        picture_url = picture_url.concat(file.getAbsolutePath()+"/");
        //Log.e(TAG, "PATH::: "+path);
        if (file.exists())
        file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
        new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });
        File[] files = Directory.create().listFiles();
        int numberOfImages = files.length;
        System.out.println("Total images in Folder "+numberOfImages);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pic_layout = null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item != null && item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(Gravity.START)) {
                drawer.closeDrawer(Gravity.START);
            }
            else {
                drawer.openDrawer(Gravity.START);
            }
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_capture) {
            openCamera();
            return true;
        }
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_details) {
            // proceed(date,ACTION_ASSESSEMENT);
            startActivity(new Intent(context,HistoryActivity.class));

        } else if (id == R.id.nav_exit) {
            finish();
        }
        return true;
    }
}
