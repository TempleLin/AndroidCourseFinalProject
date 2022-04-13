package com.templo.androidcoursefinalproject.custom_list;

public class CustomRow {
    private int id;
    private String textValue;
    private int image;

    public CustomRow(int id, String textValue, int image) {
        this.id = id;
        this.textValue = textValue;
        this.image = image;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTextValue() {
        return textValue;
    }
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
}
