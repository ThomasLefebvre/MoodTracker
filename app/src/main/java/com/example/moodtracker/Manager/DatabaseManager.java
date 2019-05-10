package com.example.moodtracker.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.moodtracker.Model.Mood;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    static final String DATABASE_NAME ="Mood.db";
    static final int DATABASE_VERSION=1;
       private DateManager dateManager= new DateManager();


    public DatabaseManager(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSql="create table T_Mood("
                +"idMood Integer primary key autoincrement,"
                +"index_ Integer not null,"
                +"comment String ,"
                +"when_ String not null"
                +")";
        db.execSQL(strSql);
        Log.i("DATABASE","onCreate invoked");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSql="drop table T_Mood";
        db.execSQL(strSql);
        this.onCreate(db);
        Log.i("DATABASE","onUpgradde invoked");

    }
    public void insertMood(int index_,String comment,String date){
        comment=comment.replace("'","''");
        date=date.replace("'","''");
        String strSql="insert into T_Mood(index_,comment,when_)"
                +"  values ("+index_+",'"+comment+"','"+date+"')";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","InsertMood");
    }
    public void updateIndexMood(int index){
        String date=dateManager.getCurrentDate();
        String strSql="update T_Mood"
                +"  set index_ ="+index+""
                +" where when_ = '"+date+"'";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","Update indexMood invoked");

    }public void updateCommentMood(String comment){
        String date=dateManager.getCurrentDate();
        comment= comment.replace("'","''");
        String strSql="update T_Mood"
                +" set comment = '"+comment+"'"
                +" where when_ = '"+date+"'";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("DATABASE","Update commentMood invoked");
    }

    public Mood readLastMood(){

        Mood mood=null;
        Cursor cursor= this.getReadableDatabase().query("T_Mood",new String[]{"idMood","index_","comment","when_"},null,null,null,null,"idMood asc",null);
        cursor.moveToLast();
        if(cursor.isLast()){
            mood = new Mood(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3));
        }
        return mood;
    }
    public List<Mood> readSevenLastMoodSave(){
        List<Mood> sevenLastMood= new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().query("T_Mood",new String[]{"idMood","index_","comment","when_"}, null,null,null,null,"idMood desc","8");
        cursor.moveToFirst();
        cursor.moveToNext();
        while(!cursor.isAfterLast()){
            Mood mood = new Mood(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3));
            sevenLastMood.add(mood);
            cursor.moveToNext();
        }
        cursor.close();
        return sevenLastMood;
    }

}
