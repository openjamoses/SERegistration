package com.example.john.se_registration.core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by john on 4/7/18.
 */

public class ReturnCursor {
    public static Cursor getCursor(String query, Context context){
        SQLiteDatabase db = DBHelper.getHelper(context).getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  cursor;
    }
}
