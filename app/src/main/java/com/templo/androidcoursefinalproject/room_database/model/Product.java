package com.templo.androidcoursefinalproject.room_database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entity mapping to the actual table in database.
@Entity(tableName = "product_table")
public class Product {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id; //Primary key id column of the table.

    @ColumnInfo(name = "productName")
    private String productName;

    @ColumnInfo(name = "image1")
    private String image1;

    @ColumnInfo(name = "image2")
    private String image2;

    @ColumnInfo(name = "image3")
    private String image3;

    @ColumnInfo(name = "image4")
    private String image4;

    @ColumnInfo(name = "image5")
    private String image5;

    @ColumnInfo(name = "category")
    private int category; //Foreign key to category table.

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "description")
    private String description;

    public Product(String productName, String image1, String image2, String image3, String image4,
                   String image5, int category, String location, String description) {
        this.productName = productName;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.category = category;
        this.location = location;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                ", image5='" + image5 + '\'' +
                ", category=" + category +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage1() {
        return image1;
    }
    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }
    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }
    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }
    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }
    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
