package com.example.moodtracker.Model;


public class Mood {

    private String dateMood;
    private String commentMood;
    private int index;
    private int idMood;


    public Mood(int index,String commentMood,String dateMood) {
        this.index = index;
        this.commentMood=commentMood;
        this.dateMood=dateMood;

    }
    public Mood(int idMood,int index,String commentMood,String dateMood){
        this.idMood=idMood;
        this.index=index;
        this.commentMood=commentMood;
        this.dateMood=dateMood;
    }

    public String getDateMood() {
        return dateMood;
    }

    public String getCommentMood() {
        return commentMood;
    }

    public int getIndex() {
        return index;
    }

    public void setCommentMood(String commentMood) {
        this.commentMood = commentMood;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
