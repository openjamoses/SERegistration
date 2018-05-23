/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.john.se_registration.ocr;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.se_registration.R;
import com.example.john.se_registration.activities.HistoryActivity;
import com.google.android.gms.common.api.CommonStatusCodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * recognizes text.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    // Use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue1,textValue2,total_value;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final int RC_OCR_CAPTURE_MARKS = 9001;
    private Button read_reg_btn,read_marks_btn,btn_submit;
    private Context context = this;
    DrawerLayout drawer;
    private String marks_string = "";
    private double total_marks = 0;
    private String REGNO = "";
    private  String ip = "";
    private Socket socket = null;
    private List<String> list = new ArrayList<>();

    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue1 = (TextView)findViewById(R.id.text_value);
        textValue2 = (TextView)findViewById(R.id.reg_value);
        total_value = (TextView)findViewById(R.id.total_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        read_reg_btn = (Button) findViewById(R.id.read_reg_btn);
        read_marks_btn = (Button) findViewById(R.id.read_marks_btn);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        try{

        }catch (Exception e){

        }
        try {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

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
        }catch (Exception e){
            e.printStackTrace();
        }
        read_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch Ocr capture activity.
                Intent intent = new Intent(context, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

        read_marks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch Ocr capture activity.
                Intent intent = new Intent(context, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE_MARKS);
            }
        });
        //findViewById(R.id.read_text).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */


    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success_reg);
                    textValue1.setText(text);
                    Log.d(TAG, "Text read: " + text);
                    REGNO = text;
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }else if(requestCode == RC_OCR_CAPTURE_MARKS) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success_marks);
                  // String str = //"qwerty-1qwerty-2 455 f0gfg 4";
                           text = text.replaceAll("[^0-9]+", " ");
                    String[] splits = text.trim().split(" ");
                    for (int i=0; i<splits.length; i++){
                        try {
                            if (!splits[i].equals("")) {
                                if (splits[i].length() > 0) {
                                    marks_string = marks_string.concat(splits[i] + " , ");
                                    total_marks += Double.parseDouble(splits[i]);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Arrays.asList(text.trim().split(" ")));
                    textValue2.setText(marks_string);
                    total_value.setText("Total: "+total_marks);
                    if (total_marks > 0){
                        if (!REGNO.equals("")){
                           // btn_submit.setVisibility(View.VISIBLE);
                        }
                    }else {
                       // btn_submit.setVisibility(View.GONE);
                    }

                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ocr_menu, menu);
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
        if (id == R.id.action_send) {
            if (total_marks > 0){
                if (!REGNO.equals("")){
                    list.add(REGNO+":"+total_marks);
                    //popSEND(REGNO,total_marks);

                    // btn_submit.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(context, "Marks Saved. Thanx...!", Toast.LENGTH_LONG);
                    View view2 = toast.getView();
                    view2.setBackgroundResource(R.drawable.button);
                    TextView text = (TextView) view2.findViewById(android.R.id.message);
                    toast.show();

                    textValue1.setText("");
                    textValue2.setText("");
                    statusMessage.setText("");
                    total_value.setText("");

                }
            }else {
                // btn_submit.setVisibility(View.GONE);
                Toast toast = Toast.makeText(context, "Invalid entry detected..!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundResource(R.drawable.button);
                TextView text = (TextView) view.findViewById(android.R.id.message);
                toast.show();
                //Toast.makeText(context, "Invalid entry detected..!", Toast.LENGTH_SHORT).show();
            }
        }

       // if (id == R.id.action_connection) {
           // popConnections();
       // }

        if (id == R.id.action_refresh) {
            textValue1.setText("");
            textValue2.setText("");
            statusMessage.setText("");
            total_value.setText("");
        }
        return super.onOptionsItemSelected(item);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_details) {
            // proceed(date,ACTION_ASSESSEMENT);
            startActivity(new Intent(context,HistoryActivity.class));

        } else if (id == R.id.nav_send) {
            //finish();
            if (list.size()> 0){
                String all_marks = "";
                for (int i=0; i<list.size(); i++){
                    all_marks = all_marks.concat(list.get(i)+"|");
                }
                popSEND(all_marks, list.size());
            }else {
                Toast toast = Toast.makeText(context, "No Data to be submited..!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundResource(R.drawable.button);
                TextView text = (TextView) view.findViewById(android.R.id.message);
                toast.show();
            }
        }else if (id == R.id.nav_exit) {
            finish();
        }
        return true;
    }
    private void popConnections(){
        AlertDialog.Builder alert;
        try {
            alert = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.connection_dialog, null);
            // this is set the view from XML inside AlertDialog
            alert.setView(view);
            Button connect_button = (Button) view.findViewById(R.id.connect_button);
            TextView title_text = (TextView) view.findViewById(R.id.title_text);
            TextView body_text = (TextView) view.findViewById(R.id.body_text);
            final EditText input_ip = (EditText) view.findViewById(R.id.input_ip);
            if (!ip.equals("")){
                input_ip.setText(ip);
                body_text.setText("Your current connection is: "+ip);
               // connect_button.setText("Close");
            }
            if (socket != null){
                connect_button.setEnabled(false);
                try{
                    input_ip.setText(socket.getInetAddress().getHostAddress());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                connect_button.setEnabled(true);
            }
            alert.setCancelable(true);

            final AlertDialog dialog = alert.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            connect_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ip = input_ip.getText().toString().trim();
                    try {
                        if (!ip.equals("")) {
                            new BSERVER(socket).execute(ip);
                            dialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void popSEND(final String details, int total){
        AlertDialog.Builder alert;
        try {
            alert = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.send_dialog, null);
            // this is set the view from XML inside AlertDialog
            alert.setView(view);
            final Button connect_button = (Button) view.findViewById(R.id.connect_button);
            TextView body_text = (TextView) view.findViewById(R.id.body_text);

            TextView regNo = (TextView) view.findViewById(R.id.regNo);
            final TextView marks = (TextView) view.findViewById(R.id.marks);

            regNo.setText(total+" - Records To be submitted to the server..!");
            //marks.setText(mks+"");

            final EditText input_ip = (EditText) view.findViewById(R.id.input_ip);
            if (ip.equals("")){
                input_ip.setText(ip);
                //body_text.setText("Sory you haven't establisd the connection yet..!");
                //connect_button.setText("Close");
            }
            if (socket != null){
                //connect_button.setEnabled(false);
                try{
                  //  input_ip.setText(socket.getInetAddress().getHostAddress());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
               // connect_button.setEnabled(true);
            }
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            connect_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (connect_button.getText().toString().equals("Close")){
                        dialog.dismiss();
                    }else {
                        ip = input_ip.getText().toString().trim();
                        try {
                            if (!ip.equals("")) {
                                new BSERVER_SEND(socket).execute(ip, details);
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // btn_submit.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(context, "Marks Sent. Thanx...!", Toast.LENGTH_LONG);
                        View view2 = toast.getView();
                        view2.setBackgroundResource(R.drawable.button);
                        TextView text = (TextView) view2.findViewById(android.R.id.message);
                        toast.show();

                        textValue1.setText("");
                        textValue2.setText("");
                        statusMessage.setText("");
                        total_value.setText("");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private class BSERVER extends AsyncTask<String, Void, Socket> {
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        Socket socket;
        BSERVER(Socket socket){
            this.socket = socket;
        }
        @Override
        protected Socket doInBackground(String... strings) {
            try {
                if (socket == null) {
                    socket = new Socket(strings[0], 9701);
                }
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeUTF("2014BCS010/ps:36");
                Log.e(TAG, "InputStream: "+dataInputStream.readUTF());
                //textIn.setText(dataInputStream.readUTF());
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return socket;
        }
        @Override
        protected void onPostExecute(Socket socket) {
            super.onPostExecute(socket);
            try{
                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (dataOutputStream != null){
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null){
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private class BSERVER_SEND extends AsyncTask<String, Void, Socket> {
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        Socket socket;
        BSERVER_SEND(Socket socket){
            this.socket = socket;
        }
        @Override
        protected Socket doInBackground(String... strings) {

            try {
                if (socket == null) {
                    socket = new Socket(strings[0], 9701);
                }
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeUTF(strings[1]);
                Log.e(TAG, "InputStream: "+dataInputStream.readUTF());
                //textIn.setText(dataInputStream.readUTF());
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally{

            }
            return socket;
        }
        @Override
        protected void onPostExecute(Socket socket) {
            super.onPostExecute(socket);
            try{
                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (dataOutputStream != null){
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (dataInputStream != null){
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
