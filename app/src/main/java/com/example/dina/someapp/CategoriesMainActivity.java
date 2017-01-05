package com.example.dina.someapp;

import android.app.ListFragment;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class CategoriesMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_main);
    }

    public void FindByCategories(View view) {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null && !allFragments.isEmpty()){
            boolean[] chakcat = ((CategoriesActivity)allFragments.get(0)).categories;
            Category[] categories = ((CategoriesActivity)allFragments.get(0)).cat;

            int count = 0;
            for(int i = 0; i < chakcat.length; i++){
                if(chakcat[i]){
                    count++;
                }
            }
            int[] answer = new int[count];
            count = 0;
            for(int i = 0; i < chakcat.length; i++){
                if(chakcat[i]){
                    answer[count] = categories[i].ID;
                    count++;
                }
            }
            Intent intent = new Intent(this,EntityActivity.class);
            intent.putExtra("Categories", answer);
            setResult(RESULT_OK, intent);
            finish();

        }
    }
}
