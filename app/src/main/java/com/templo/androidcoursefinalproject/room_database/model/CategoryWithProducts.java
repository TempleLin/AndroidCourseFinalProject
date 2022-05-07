package com.templo.androidcoursefinalproject.room_database.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

//TODO: Relational design (ROOM Database's relational design note is saved at ImportantNotes repository.)
public class CategoryWithProducts {
    @Embedded
    Category category;

    @Relation(entity = Product.class, parentColumn = "id", entityColumn = "category")
    List<Product> productsList;
}
