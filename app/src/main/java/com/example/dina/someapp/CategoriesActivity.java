package com.example.dina.someapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.prompt;

public class CategoriesActivity extends ListFragment {

    ArrayAdapter mAdapter;
    public boolean[] categories;
    Category[] cat;

    @Override
   public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CheckedTextView checkedTextView = ((CheckedTextView)v);
        checkedTextView.setChecked(!checkedTextView.isChecked());
        categories[position] = !categories[position];
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        int id_kind= getActivity().getIntent().getIntExtra("Kind",0);

        MyDb db = new MyDb(getActivity().getApplication());
        db.open();
        cat = db.getCategories(id_kind);
        String[] str = new String[cat.length];

        int count = cat.length;
        categories = new boolean[count];
        for(int i = 0; i < count; i++){
            categories[i] = false;
        }
        for(int i = 0; i < cat.length; i++){
            str[i] = cat[i].Name;
        }

        mAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, str);
        setListAdapter(mAdapter);
        db.close();
    }



}
