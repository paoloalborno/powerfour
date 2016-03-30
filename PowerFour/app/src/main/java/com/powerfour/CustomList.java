package com.powerfour;

import android.app.Activity;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList <String> listOfContents;

    public CustomList(Activity context, ArrayList<String> list) {
        super(context, R.layout.list_single, list);
        this.context = context;
        this.listOfContents = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_single, parent, false);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtTitle.setText(listOfContents.get(position));
        txtTitle.setAllCaps(true);

        if(listOfContents.get(position).contains("BF4")){
            imageView.setImageResource(R.drawable.bf4);
        }
        if(listOfContents.get(position).contains("BF3")){
            imageView.setImageResource(R.drawable.bf3);
        }
        if(listOfContents.get(position).contains("RAD")){
            imageView.setImageResource(R.drawable.rad);
        }

        rowView.setTag("Event"+position);
        return rowView;
    }



    /*
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public CustomList(Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }*/

}

