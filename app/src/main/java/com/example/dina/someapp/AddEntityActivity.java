package com.example.dina.someapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class AddEntityActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entity);
    }

    public void LoadImageLind(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap img = null;
        ImageView image = (ImageView) findViewById(R.id.image_for_entity);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(img);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void SaveEntity(View view) {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null && !allFragments.isEmpty()) {
            boolean[] chakcat = ((CategoriesActivity) allFragments.get(0)).categories;
            Category[] categories = ((CategoriesActivity) allFragments.get(0)).cat;
            int c = 0;
            for (boolean b: chakcat){
                if(b)
                    c++;
            }

            int[] cat = new int[c];
            c = 0;
            for (int i = 0; i< categories.length; i++){
                if (chakcat[i]) {
                    cat[c] = categories[i].ID;
                    c++;
                }
            }
            String name = ((TextView)findViewById(R.id.entity_name)).getText().toString();
            Bitmap im = ((BitmapDrawable)(((ImageView)findViewById(R.id.image_for_entity)).getDrawable())).getBitmap();
            if (!name.equals("") && im != null){
                int k = getIntent().getIntExtra("Kind",0);
                Entity ent = new Entity(k,name,im);
                MyDb dbhelper = new MyDb(getApplication());
                dbhelper.open();
                dbhelper.SaveEntity(ent,cat);
                dbhelper.close();
                Intent intent = new Intent(this, EntityActivity.class);
                intent.putExtra("Kind",k);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }

    }
}
