package com.example.tappy.multinotes;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class notesAdapter extends RecyclerView.Adapter<notesViewHolder> {

    private ArrayList<addNotes> notesList;
    private MainActivity mainActivity;
    private int pos;

    public notesAdapter(MainActivity ma, ArrayList<addNotes> n) {
        mainActivity = ma;
        notesList = n;
    }

    @Override
    public notesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_view, parent, false);

        itemView.setOnClickListener(mainActivity);


        itemView.setOnLongClickListener(mainActivity);


        return new notesViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(notesViewHolder holder, int position) {
        addNotes an=notesList.get(position);
        holder.title.setText(an.getTitle());
        holder.content.setText(an.getText());
        holder.dateMod.setText(an.getLastUpdate());

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
