package com.conceptappsworld.bookaholics.model;

import java.util.ArrayList;

public class Book {

    private String title;
    private String description;
    private String publisher;
    private String infoLink;
    private ArrayList<String> authors;

    public Book(String _title, String _descripton, String _publisher, String _infoLink){
        title = _title;
        description = _descripton;
        publisher = _publisher;
        infoLink = _infoLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }
}
