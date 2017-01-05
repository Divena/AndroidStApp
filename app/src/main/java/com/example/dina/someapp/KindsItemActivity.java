package com.example.dina.someapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class KindsItemActivity extends ListFragment {


    Kind[] k;
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity().getApplicationContext(), "Был выбран пункт " + k[position].Name,
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),EntityActivity.class);
        intent.putExtra("Kind",k[position].ID);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        MyDb dbhelper = new MyDb(getActivity().getApplicationContext());
        dbhelper.open();
        k = dbhelper.getKinds();
        dbhelper.close();
        setListAdapter(new KindAdapter(getActivity(),k));
    }


    private Kind getModel(int position) {
        return(((KindAdapter)getListAdapter()).getItem(position));
    }
    class KindAdapter extends ArrayAdapter<Kind> {

        private LayoutInflater mInflater;

        KindAdapter(Context context, Kind[] list) {
            super(context, R.layout.activity_kinds_item, list);
            mInflater = LayoutInflater.from(context);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ImageView imv;
            View row = convertView;

            if (row == null) {

                row = mInflater.inflate(R.layout.activity_kinds_item, parent, false);

                imv = (ImageView) row.findViewById(R.id.KindImage);

                row.setTag(imv);
            } else {

                imv = (ImageView) row.getTag();
            }
            Kind state = getModel(position);
            imv.setImageBitmap(state.img);
            return row;
        }
    }

}