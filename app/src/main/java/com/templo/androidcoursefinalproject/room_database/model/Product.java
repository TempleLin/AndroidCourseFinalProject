package com.templo.androidcoursefinalproject.room_database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


//Entity mapping to the actual table in database.
@Entity(tableName = "product_table", foreignKeys = {
        //Set "sellerUserID" as foreign key from user_table.
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "sellerUserID", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "category", onDelete = ForeignKey.CASCADE)
})
@ToString
public class Product {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Getter@Setter
    private int id; //Primary key id column of the table.

    @ColumnInfo(name = "productName")
    @Getter@Setter
    private String productName;

    @ColumnInfo(name = "sellerUserID")
    @Getter@Setter
    private int sellerUserID;

    @ColumnInfo(name = "image1")
    @Getter@Setter
    private String image1;

    @ColumnInfo(name = "image2")
    @Getter@Setter
    private String image2;

    @ColumnInfo(name = "image3")
    @Getter@Setter
    private String image3;

    @ColumnInfo(name = "image4")
    @Getter@Setter
    private String image4;

    @ColumnInfo(name = "image5")
    @Getter@Setter
    private String image5;

    @ColumnInfo(name = "category")
    @Getter@Setter
    private int category; //Foreign key to category table.

    @ColumnInfo(name = "location")
    @Getter@Setter
    private String location;

    @ColumnInfo(name = "description")
    @Getter@Setter
    private String description;

    public Product(String productName, int sellerUserID, String image1, String image2, String image3, String image4,
                   String image5, int category, String location, String description) {
        this.productName = productName;
        this.sellerUserID = sellerUserID;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.category = category;
        this.location = location;
        this.description = description;
    }
}
