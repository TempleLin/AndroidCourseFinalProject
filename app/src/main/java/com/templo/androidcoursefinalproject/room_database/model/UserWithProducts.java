package com.templo.androidcoursefinalproject.room_database.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

//TODO: Relational design (ROOM Database's relational design note is saved at ImportantNotes repository.)
public class UserWithProducts {
    @Embedded
    @Getter @Setter
    User user;

    @Relation(entity = Product.class, parentColumn = "id", entityColumn = "sellerUserID")
    @Getter @Setter
    List<Product> productsList;
}
