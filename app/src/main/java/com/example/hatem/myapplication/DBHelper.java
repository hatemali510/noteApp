package com.example.hatem.myapplication;

/**
 * Created by hatem on 13/02/2018.
 */

import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notes.db";
    public static final String CONTACTS_TABLE_NAME = "notesinfo";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table notesinfo " +
                        "(id integer primary key AUTOINCREMENT NOT NULL, name text,lastupdated text, note_body text )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS notesinfo");
        onCreate(db);
    }

    public boolean insertnote (String name, String lastupdated,String text) {
        text="";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lastupdated", lastupdated);
        contentValues.put("note_body",text);
        db.insert("notesinfo", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notesinfo where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

   public boolean updatenote (Integer id, String name, String last, String note_body) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lastupdated", last);
        contentValues.put("note_body", note_body);
        db.update("notesinfo", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notesinfo",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<noteInfo> getAllnotes() {
        ArrayList<noteInfo> array_list = new ArrayList<noteInfo>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notesinfo", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            noteInfo note=new noteInfo();
            note.setName(res.getString(res.getColumnIndex("name")));
            note.setId(res.getInt(res.getColumnIndex("id")));
            note.setLast_updated(res.getString(res.getColumnIndex("lastupdated")));
            note.setNoteBody(res.getString(res.getColumnIndex("note_body")));
            array_list.add(note);
            res.moveToNext();
        }
        return array_list;
    }
    public boolean namefounded(String name){
        ArrayList<noteInfo> notes = getAllnotes();
        for (int i=0;i<notes.size();i++){
            if(name==notes.get(i).getName()){
                return true;
            }
        }
        return false;
    }

}

//in the next version delete note will implement