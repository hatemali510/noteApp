package com.example.hatem.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hatem on 11/02/2018.
 */

public class noteFragment extends Activity {

   private String noteBody="";
   private String note_name="";
   private String lastupdated="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        final DBHelper db=new DBHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoglayout);
        TextView textView=findViewById(R.id.textView5);
        Intent intent = getIntent();
        final int id=intent.getIntExtra("id",-1);
        final EditText editText=findViewById(R.id.editText4);

        Cursor note=db.getData(id);
        if (note.moveToFirst()){
            do{
                noteBody=note.getString(note.getColumnIndex("note_body"));
                note_name=note.getString(note.getColumnIndex("name"));
                lastupdated=note.getString(note.getColumnIndex("lastupdated"));
            }while(note.moveToNext());
        }
        note.close();
        TextView textView1=findViewById(R.id.last);
        textView1.setText("updated at :  "+lastupdated);
        editText.setText(noteBody);
        textView.setText(note_name);
        FloatingActionButton save=findViewById(R.id.floatingActionButton2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               String note_body= editText.getText().toString();
                Date date=new Date();
                String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
                if(id>0 && !note_body.isEmpty() && !note_name.isEmpty()) {
                    db.updatenote(id, note_name, modifiedDate, note_body);
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    Snackbar snackbar = Snackbar
                            .make(view, "note saved successfully", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    Toast.makeText(noteFragment.this, "can't save ", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}

// in the next version will make note head which open when press back of the note to can see the note content out side the note body
