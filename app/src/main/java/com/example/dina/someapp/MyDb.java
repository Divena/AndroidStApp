package com.example.dina.someapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDb {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "somefortest.db";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_Kind = "Kind";
    static final String TABLE_Entity = "Entity";
    static final String TABLE_Record = "Record";
    static final String TABLE_Category = "Category";
    public static final String Kind_ID = "Kind_ID";
    public static final String Entity_ID = "Entity_ID";
    public static final String Record_ID = "Record_ID";
    public static final String Category_ID = "Category_ID";
    public static final String NAME = "Name";
    public static final String IMAGE = "Image";
    public static final String Entity_Name = "Entity_Name";
    public static final String Category_Name = "Category_Name";


    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_Kind +" (" + Kind_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
                    + " TEXT, " + IMAGE + " BLOB);");


            db.execSQL("CREATE TABLE " + TABLE_Entity +" (" + Entity_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
                    + " TEXT, " + IMAGE + " BLOB, " + Kind_ID +" INTEGER);" );

            db.execSQL("CREATE TABLE " + TABLE_Category +" (" + Category_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
                    + " TEXT, " + Kind_ID +" INTEGER);" );


            db.execSQL("CREATE TABLE " + TABLE_Record +" (" + Record_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +  Entity_ID + " INTEGER, " + Category_ID + " INTEGER);" );
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_Category);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_Kind);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_Entity);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_Record);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public MyDb(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }

    public MyDb open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public void SaveKind(Kind k){
        ContentValues cv = new ContentValues();
        cv.put(NAME, k.Name);
        cv.put(IMAGE, Utility.getBytes(k.img));
        mDb.insert(TABLE_Kind, null, cv);
    }
    public void SaveCategory(Category c){
        ContentValues cv = new ContentValues();
        cv.put(NAME, c.Name);
        cv.put(Kind_ID, c.Kind_ID);
        mDb.insert(TABLE_Category, null, cv);
    }
    public void SaveEntity(Entity e, int[] Categories){
        ContentValues cv = new ContentValues();
        cv.put(NAME, e.Name);
        cv.put(IMAGE, Utility.getBytes(e.Image));
        cv.put(Kind_ID, e.Kind_ID);
        mDb.insert(TABLE_Entity, null, cv);
        Cursor cur = mDb.rawQuery("select * from "+ MyDb.TABLE_Entity + " where " + MyDb.Kind_ID
                +" = ?",new String[]{ "" + e.Kind_ID});

        if (cur.moveToFirst()) {
            do {
                if(e.Name.equals(cur.getString(cur.getColumnIndex(NAME)))){
                    e.ID = cur.getInt(cur.getColumnIndex(Entity_ID));
                    break;
                }
            } while (cur.moveToNext());
        }
        for(int c:Categories){
            ContentValues cv1 = new ContentValues();
            cv1.put(Entity_ID,e.ID);
            cv1.put(Category_ID, c);
            mDb.insert(TABLE_Record, null, cv1);
        }
    }

    public Kind[] getKinds(){

        Cursor cur = mDb.rawQuery("select * from "+ MyDb.TABLE_Kind, null);
        Kind[] ar = new Kind[cur.getCount()];
        int i = 0;
        if (cur.moveToFirst()) {
            do {
                byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
                String name = cur.getString(cur.getColumnIndex(NAME));
                int id = cur.getInt(cur.getColumnIndex(Kind_ID));
                ar[i] = new Kind(name, Utility.getPhoto(blob),id);
                i++;
            } while (cur.moveToNext());
        }
        return ar;
    }

    public Entity[] getEntities(int k_id){
        Cursor cur = mDb.rawQuery("select * from "+ MyDb.TABLE_Entity + " where " + MyDb.Kind_ID
                +" = ?",new String[]{ "" + k_id});
        Entity[] ar = new Entity[cur.getCount()];
        int i = 0;
        if (cur.moveToFirst()) {
            do {
                byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
                String name = cur.getString(cur.getColumnIndex(NAME));
                int id = cur.getInt(cur.getColumnIndex(Entity_ID));
                ar[i] = new Entity(k_id, name, Utility.getPhoto(blob));
                ar[i].ID = id;
                i++;
            } while (cur.moveToNext());
        }
        return ar;
    }
    public Entity[] getEntities(int k_id, int[] categories){


        String query = "select * from "+ MyDb.TABLE_Record + " where " + MyDb.Category_ID + " = ";

        for (int i: categories){
            query += i + " OR "+ MyDb.Category_ID + " = ";
        }
        query += -1;
        Cursor cur = mDb.rawQuery(query,null);
        int[] ar = new int[cur.getCount()];
        int i = 0;
        if (cur.moveToFirst()) {
            do {
                int id = cur.getInt(cur.getColumnIndex(Entity_ID));
                ar[i] = id;
                i++;
            } while (cur.moveToNext());
        }
        cur.close();
        i = 0;
        for(int j = 0; j < ar.length; j++){
            if (ar[j] != -1){
                int count = 1;
                for (int k = j+1; k < ar.length; k++){
                    if(ar[k] == ar[j]){
                        count++;
                        ar[k] = -1;
                    }
                }
                if (count != categories.length){
                    ar[j] = -1;
                }else{
                    i++;
                }
            }
        }
        Entity[] mas = new Entity[i];
        int[] a = new int[i];
        int j = 0;
        for (int k: ar){
            if(k != -1) {
                a[j] = k;
                j++;
            }
        }
        j = 0;
        Entity[] m  = getEntities(k_id);
        for(Entity en: m){
            for(int k: a){
                if(en.ID == k){
                    mas[j] = en;
                    j++;
                    break;
                }
            }
        }
        return mas;
    }

    public Category[] getCategories(int kind_id){

        Cursor cur = mDb.rawQuery("select * from "+ MyDb.TABLE_Category+ " where " + MyDb.Kind_ID
                +" = ?",new String[]{ "" + kind_id});
        Category[] ar = new Category[cur.getCount()];
        int i = 0;
        if (cur.moveToFirst()) {
            do {

                    String name = cur.getString(cur.getColumnIndex(NAME));
                    int k = cur.getInt(cur.getColumnIndex(Kind_ID));
                    Category cat = new Category(k, name);
                    cat.ID = cur.getInt(cur.getColumnIndex(Category_ID));
                    ar[i] = cat;

                i++;
            } while (cur.moveToNext());
        }
        return ar;
    }

}