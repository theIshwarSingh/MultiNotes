package com.example.tappy.multinotes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class notesViewHolder extends RecyclerView.ViewHolder{

    public TextView title;
    public TextView content;
    public TextView dateMod;

    public notesViewHolder(View view){

        super(view);

        title=(TextView) view.findViewById(R.id.noteTitle);
        content=(TextView) view.findViewById(R.id.noteContent);
        dateMod=(TextView) view.findViewById(R.id.dateModified);


    }
}
