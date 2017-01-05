package com.example.dina.someapp;

import android.graphics.Bitmap;

/**
 * Created by dina on 13.11.2016.
 */
public class Kind {

    public String Name;
    public Bitmap img;
    public int ID;
    public Kind(String name, Bitmap i, int id){
        Name = name;
        img = i;
        ID = id;
    }
}

