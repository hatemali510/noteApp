package com.example.hatem.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private  String name="";
    private ArrayList<noteInfo> list;
    private static CustomAdapter adapter;
    private String id;
    Context context;
    private DBHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DBHelper(getApplicationContext());
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_note_name();
            }
        });
        final ListView listview = (ListView) findViewById(R.id.listView);
        list=db.getAllnotes();
        adapter= new CustomAdapter(list,getApplicationContext());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                noteInfo selectednote=list.get(position);
                int note_id=0;
                Cursor res=db.getData(selectednote.getId());
                if (res.moveToFirst()){
                    do{
                        note_id = res.getInt(res.getColumnIndex("id"));

                        // do what ever you want here
                    }while(res.moveToNext());
                }
                res.close();
                Intent intent = new Intent(getApplicationContext(),noteFragment.class);
                intent.putExtra("id",note_id);
                Log.d(getClass().getName(), "value = " + note_id);

                intent.putExtra("note_name",name);
                startActivity(intent);
            }

        });

    }


    private void request_note_name() {
        final noteInfo note=new noteInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter note  name:");
        final EditText input_field = new EditText(this);
        builder.setView(input_field);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!db.namefounded(input_field.getText().toString())) {
                    note.setName(input_field.getText().toString());
                    note.setNoteBody("");
                    Date date = new Date();
                    String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    note.setLast_updated(modifiedDate);
                    db.insertnote(note.getName(), note.getLast_updated(), "");
                    list.add(note);
                    list = db.getAllnotes();
                    // add the object to the array list .
                    // don't forget
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}




