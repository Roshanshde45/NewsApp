package com.example.recyclerviewpractice;

public class Model {
    private String imageURL, title, description;


    public Model() {
    }

    public Model(String imageURL, String title, String description) {
        this.imageURL = imageURL;
        this.title = title;
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
