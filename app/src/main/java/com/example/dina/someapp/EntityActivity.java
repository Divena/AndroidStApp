package com.example.dina.someapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.util.List;

public class EntityActivity extends AppCompatActivity {

    private int k_id;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_entity);
        k_id = getIntent().getIntExtra("Kind",0);
    }


    public void FindByCategories(View view) {
        Intent intent = new Intent(this, CategoriesMainActivity.class);
        intent.putExtra("Kind", k_id);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_add_category:
                intent = new Intent(this, AddCategoryActivity.class);
                intent.putExtra("Kind", k_id);
                startActivity(intent);
                return true;
            case R.id.action_add_entity:
                intent = new Intent(this, AddEntityActivity.class);
                intent.putExtra("Kind", k_id);
                startActivity(intent);
                return true;
            case R.id.action_back:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                int[] cat = data.getIntArrayExtra("Categories");
                List<android.support.v4.app.Fragment> allFragments = getSupportFragmentManager().getFragments();
                if (allFragments != null && !allFragments.isEmpty()) {
                    ((EntityItemActivity) allFragments.get(0)).SetEntities(cat);
                } else {

                }
            }
        }
    }
}
