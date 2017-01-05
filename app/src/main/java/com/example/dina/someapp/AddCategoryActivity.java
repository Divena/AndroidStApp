package com.example.dina.someapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
    }

    public void SaveEntity(View view) {
        int k = getIntent().getExtras().getInt("Kind");
        TextView t = (TextView)findViewById(R.id.entity_name);
        String s = t.getText().toString();
        if (s != null && !s.equals("")) {
            Category cat = new Category(k,s);
            MyDb db = new MyDb(getApplication());
            db.open();
            db.SaveCategory(cat);
            db.close();
            finish();
        }
    }
}
