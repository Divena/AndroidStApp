package com.example.dina.someapp;

import android.graphics.Bitmap;


/**
 * Created by dina on 13.11.2016.
 */
public class Entity {
    public int ID;
    public int Kind_ID;
    public String Name;
    public Bitmap Image;

    public Entity(int k, String n, Bitmap i){
        Kind_ID = k;
        Name = n;
        Image = i;
    }
}
