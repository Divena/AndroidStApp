package com.example.dina.someapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EntityItemActivity extends ListFragment {

    Entity[] e;
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity().getApplicationContext(), "Был выбран пункт " + e[position].Name,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        MyDb dbhelper = new MyDb(getActivity().getApplicationContext());
        dbhelper.open();
        e = dbhelper.getEntities((getActivity()).getIntent().getIntExtra("Kind",0));
        dbhelper.close();
        setListAdapter(new EntityItemActivity.EntityAdapter(e));
    }
    public void SetEntities(int[] categories){
        MyDb dbhelper = new MyDb(getActivity().getApplicationContext());
        dbhelper.open();
        e = dbhelper.getEntities((getActivity()).getIntent().getIntExtra("Kind",0), categories);
        dbhelper.close();
        setListAdapter(new EntityItemActivity.EntityAdapter(e));
    }

    private Entity getModel(int position) {
        return(((EntityAdapter)getListAdapter()).getItem(position));
    }
    class EntityAdapter extends ArrayAdapter<Entity> {

        private LayoutInflater mInflater;

        EntityAdapter(Entity[] list) {
            super(getActivity(),R.layout.activity_entity_item,  list);
            mInflater = LayoutInflater.from(getActivity());
        }
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder;
            View row=convertView;
            if(row==null){

                row = mInflater.inflate(R.layout.activity_entity_item, parent, false);
                holder = new ViewHolder();
                holder.EntityImage = (ImageView) row.findViewById(R.id.EntityImageView);
                holder.Name = (TextView) row.findViewById(R.id.EntityName);
                row.setTag(holder);
            }
            else{

                holder = (ViewHolder)row.getTag();
            }

            Entity en = getModel(position);

            holder.EntityImage.setImageBitmap(en.Image);
            holder.Name.setText(en.Name);
            return row;
        }

        class ViewHolder {
            public ImageView EntityImage;
            public TextView Name;
        }
    }
}
