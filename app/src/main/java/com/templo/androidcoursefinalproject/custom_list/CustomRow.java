package com.templo.androidcoursefinalproject.custom_list;

public class CustomRow {
    private String id;
    private String textValue;
    private int image;

    public CustomRow(String id, String textValue, int image) {
        this.id = id;
        this.textValue = textValue;
        this.image = image;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
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
