package com.powerfour;

import android.app.Activity;
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

        //PAUL: inserisco il testo, ottenendolo dalla listOfContents
        txtTitle.setText(listOfContents.get(position));
        txtTitle.setAllCaps(true);

        //PAUL: cambio di icona per tipoloia evento, automaticamente
        //in base al contenuto del test dell'evento

        if(listOfContents.get(position).contains("BF4")){
            imageView.setImageResource(R.drawable.bf4);
        }
        if(listOfContents.get(position).contains("BF3")){
            imageView.setImageResource(R.drawable.bf3);
        }
        if(listOfContents.get(position).contains("RAD")){
            imageView.setImageResource(R.drawable.rad);
        }

        //PAUL: set della Tag in caso necessitassimo di usare la riga più avanti
        rowView.setTag("Event"+position);
        return rowView;
    }
}

