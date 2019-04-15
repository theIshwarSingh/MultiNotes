package com.example.tappy.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<addNotes> notesList = new ArrayList<>();
    private static final int NEW_NOTE = 1, EXISTING_NOTE = 0;
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private notesAdapter nAdapter;
    Intent intent;
    ArrayList<addNotes> enteredData = new ArrayList<addNotes>();
    int pos, counter = -1;
    JsonReader reader;
    JsonWriter writer;
    String title, date, content;
    static String label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        nAdapter = new notesAdapter(this, notesList);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadJson();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: MainActivity");
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: MainActivity");
        pos = recyclerView.getChildLayoutPosition(v);

        intent = new Intent(v.getContext(), EditActivity.class);
        intent.putExtra("Title", enteredData.get(pos).getTitle().toString());
        intent.putExtra("Content", enteredData.get(pos).getText().toString());
        intent.putExtra("lastUpdate", enteredData.get(pos).getLastUpdate().toString());
        startActivityForResult(intent, EXISTING_NOTE);
    }
    public boolean onLongClick(View view) {
        pos = this.getRecyclerView().getChildLayoutPosition(view);
        addNotes an = notesList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem(pos);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setTitle("Action Required!");
        builder.setMessage("Delete note '"+an.getTitle()+"'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: MainActivity");
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: MainActivity");
        switch (item.getItemId()) {
            case R.id.info:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.create:
                //notesList.add(new addNotes());
                intent = new Intent(this, EditActivity.class);
                startActivityForResult(intent, NEW_NOTE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Log.d(TAG, "onActivityResult: MainActivity");
            addNotes temp;
            if (requestCode == NEW_NOTE) {
                if (resultCode == RESULT_OK) {
                    title = data.getStringExtra("Title");
                    content = data.getStringExtra("Content");
                    date = data.getStringExtra("lastUpdate");

                    addNotes note = new addNotes(title, content, date);
                    enteredData.add(0, note);
                    content=trimContent(content);
                    notesList.add(0, new addNotes(title, content, date));
                    nAdapter.notifyDataSetChanged();
                } else {
                }
            } else if (requestCode == EXISTING_NOTE) {
                if (resultCode == RESULT_OK) {
                    addNotes a;
                    title = data.getStringExtra("Title");
                    content = data.getStringExtra("Content");
                    date = data.getStringExtra("lastUpdate");
                    a = enteredData.get(pos);
                    a = new addNotes(title, content, date);
                    enteredData.remove(pos);
                    notesList.remove(pos);
                    nAdapter.notifyDataSetChanged();
                    enteredData.add(0, a);
                    content=trimContent(content);
                    notesList.add(0, new addNotes(title, content, date));
                    nAdapter.notifyDataSetChanged();
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String trimContent(String content) {
        if (content.length() > 80)
            content = content.substring(0, 81) + "...";
        return content;
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: MainActivity");
        saveJson();
    }
    public void saveJson() {
        try {
            addNotes temp;
            File f = new File("Multi-notes.json");
            f.delete();
            writer = new JsonWriter(new OutputStreamWriter(openFileOutput("Multi-notes.json", Context.MODE_PRIVATE), "UTF-8"));
            writer.beginObject();
            Iterator<addNotes> iterator = enteredData.iterator();
            while (iterator.hasNext()) {
                temp = iterator.next();
                writer.name("jTitle").value(temp.getTitle().toString());
                writer.name("jDate").value(temp.getLastUpdate().toString());
                writer.name("jContent").value(temp.getText().toString());
            }
            writer.endObject();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadJson() {
        notesList.clear();
        enteredData.clear();

        try {
            reader = new JsonReader(new InputStreamReader(openFileInput("Multi-notes.json"), "UTF-8"));
            reader.beginObject();
            int temp = 0;
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("jTitle")) {
                    title = reader.nextString();
                    temp++;
                } else if (name.equals("jDate")) {
                    date = reader.nextString();
                    temp++;
                } else if (name.equals("jContent")) {
                    content = reader.nextString();
                    temp++;
                } else {
                    reader.skipValue();
                }
                if (temp == 3) {
                    counter++;
                    addNotes note = new addNotes(title, content, date);
                    enteredData.add(note);
                    content=trimContent(content);
                    /*if (content.length() > 80)
                        content = content.substring(0, 81) + "...";*/
                    notesList.add(counter, new addNotes(title, content, date));
                    nAdapter.notifyDataSetChanged();
                    temp = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class MyAsyncTask extends AsyncTask<Context, Integer, String> { //  <Parameter, Progress, Result>
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Context... contexts) {

            loadJson();
            return enteredData.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    public void deleteItem(int index) {
        enteredData.remove(index);
        notesList.remove(index);
        nAdapter.notifyDataSetChanged();
    }
}


