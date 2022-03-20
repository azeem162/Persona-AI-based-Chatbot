package com.example.persona.Notepad;

public class NoteModel  {

    private  String noteTitle;
    private  String noteTime;
    private String content;
    public  NoteModel (String noteTitle, String noteTime, String content ){
        this.noteTitle = noteTitle;
        this.noteTime = noteTime;
        this.content=content;
    }

    public NoteModel() {

    }


    public  String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String Title) {
        this.noteTitle = Title;
    }

    public  String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
