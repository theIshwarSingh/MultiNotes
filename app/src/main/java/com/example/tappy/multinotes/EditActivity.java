package com.example.tappy.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    private EditText input, title;
    Intent intent;
    String date = "",temp_text,temp_title;
    Date objDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        input = (EditText) findViewById(R.id.userText);
        title = (EditText) findViewById(R.id.noteTitle);
        input.setMovementMethod(new ScrollingMovementMethod());

        //Pre fetch already existing Data
        Intent intent = getIntent();
        temp_title=intent.getStringExtra("Title");
        temp_text=intent.getStringExtra("Content");

        //Set value to existing fields
        title.setText(temp_title);
        input.setText(temp_text);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                try {
                    if (input.getText().toString().equals(temp_text) && title.getText().toString().equals(temp_title)) {
                        //Do no save data if there is no change in text
                        finish();
                    }else if (title.getText().toString().trim().equals("")) {
                        Toast.makeText(this, "Note not saved. You must provide a title to your note for saving.", Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        saveData();
                        finish();
                    }
                }catch (Exception e){

                    Log.d(TAG, "onOptionsItemSelected: EditActivity"+ e );
                    finish();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);


        }
        return super.onOptionsItemSelected(item);
    }
    public void saveData() {

        Intent data = new Intent();
        data.putExtra("Title", title.getText().toString());
        data.putExtra("Content", input.getText().toString());
        objDate = new Date();
        String strDateFormat = "E MMM dd, hh:mm a";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        date = (objSDF.format(objDate));
        data.putExtra("lastUpdate", date);
        setResult(RESULT_OK, data);
    }

    public void onBackPressed() {
        try {
            if (input.getText().toString().equals(temp_text) && title.getText().toString().equals(temp_title)) {
                //Do no save data if there is no change in text
                finish();
            } else if (title.getText().toString().equals("")) {
                Toast.makeText(this, "Note not saved. You must provide a title to your note for saving.", Toast.LENGTH_LONG).show();
                finish();
            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveData();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setTitle("Action Required!");
                builder.setMessage("Note not saved. Would to like to save '"+title.getText().toString()+"' ?");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }catch (Exception e){

            Log.d(TAG, "onOptionsItemSelected: EditActivity"+ e );
            finish();
        }


    }

}


