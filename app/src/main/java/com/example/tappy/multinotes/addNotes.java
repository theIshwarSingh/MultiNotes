package com.example.tappy.multinotes;

public class addNotes {
    private String title, text, lastUpdate;


    public addNotes(){
    }

    public addNotes(String title, String text, String lastUpdate){
        this.title=title;
        this.text=text;
        this.lastUpdate=lastUpdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "addNotes{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }
}
