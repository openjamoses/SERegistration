package com.example.john.se_registration.dbOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.john.se_registration.core.DBHelper;
import com.example.john.se_registration.utils.Constants;

import static com.example.john.se_registration.utils.Constants.config.COURSE;
import static com.example.john.se_registration.utils.Constants.config.PICTURE_NAME;
import static com.example.john.se_registration.utils.Constants.config.REGISTRATION_DATE;
import static com.example.john.se_registration.utils.Constants.config.REGISTRATION_ID;
import static com.example.john.se_registration.utils.Constants.config.REGISTRATION_NUMBER;
import static com.example.john.se_registration.utils.Constants.config.REGISTRATION_STATUS;
import static com.example.john.se_registration.utils.Constants.config.REGISTRATION_TIME;
import static com.example.john.se_registration.utils.Constants.config.SEMESTER;

/**
 * Created by john on 10/17/17.
 */
public class Registration {
    Context context;
    private static final String TAG = "Registration";
    public Registration(Context context){
        this.context = context;
    }

    public String save(String registration_date, String registration_time, String registration_number, String semester, String course, String pictures, int registration_status) {
        SQLiteDatabase database = DBHelper.getHelper(context).getWritableDatabase();
        String message = null;
        try{
            //database.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(REGISTRATION_DATE,registration_date);
            contentValues.put(REGISTRATION_TIME,registration_time);
            contentValues.put(REGISTRATION_NUMBER,registration_number);
            contentValues.put(SEMESTER,semester);
            contentValues.put(COURSE,course);
            contentValues.put(PICTURE_NAME,pictures);
            contentValues.put(REGISTRATION_STATUS,registration_status);

            database.insert(Constants.config.TABLE_REGISTRATION, null, contentValues);
            //database.setTransactionSuccessful();
            message = "registration Details saved!";

        }catch (Exception e){
            e.printStackTrace();
            message = "Sorry, error: "+e;
        }finally {
            //database.close();
            // database.endTransaction();
        }
        return message;
    }

    public  int selectLast(){
        int last_id = 0;
        SQLiteDatabase db = DBHelper.getHelper(context).getReadableDatabase();
        Cursor cursor = null;
        try{
            db.beginTransactionNonExclusive();
            String query = "SELECT "+Constants.config.REGISTRATION_ID+" FROM" +
                    " "+ Constants.config.TABLE_REGISTRATION+"  ORDER BY "+Constants.config.REGISTRATION_ID+" DESC LIMIT 1 ";
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    last_id = cursor.getInt(cursor.getColumnIndex(Constants.config.REGISTRATION_ID));
                }while (cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return  last_id;
    }

    public String edit(int registration_id,String registration_date, String registration_time, String registration_number, String semester, String course, int registration_status) {
        SQLiteDatabase database = DBHelper.getHelper(context).getWritableDatabase();
        String message = null;

        try{
            //database.beginTransaction();
            ContentValues contentValues = new ContentValues();

            contentValues.put(REGISTRATION_DATE,registration_date);
            contentValues.put(REGISTRATION_TIME,registration_time);
            contentValues.put(REGISTRATION_NUMBER,registration_number);
            contentValues.put(SEMESTER,semester);
            contentValues.put(COURSE,course);
            contentValues.put(REGISTRATION_STATUS,registration_status);

            database.update(Constants.config.TABLE_REGISTRATION
                    ,contentValues,REGISTRATION_ID+"="+registration_id, null);
            message = "personel details updated!";

        }catch (Exception e){
            e.printStackTrace();
            message = "Sorry, error: "+e;
        }finally {
            //database.close();
            // database.endTransaction();
        }
        return message;
    }
}
