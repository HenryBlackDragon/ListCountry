package com.example.alex.listcountry.Help_class;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.listcountry.R;

import java.io.IOException;
import java.io.InputStream;

public class SomeAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] countryNamesCapitals;
    private final String[] countryNames;


    public SomeAdapter(Activity context, String[] countryNamesCapitals, String[] countryNames) {
        super(context, R.layout.activity_country, countryNamesCapitals);
        this.context = context;
        this.countryNamesCapitals = countryNamesCapitals;
        this.countryNames = countryNames;
    }

    private static class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.activity_country, null, true);
            holder = new ViewHolder();
            holder.textView = (TextView) rowView.findViewById(R.id.label);
            holder.imageView = (ImageView) rowView.findViewById(R.id.icon);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textView.setText(countryNamesCapitals[position]);
        String s = countryNames[position];
        try {
            for (String name : countryNames) {
                InputStream inputStream = getContext().getAssets().open(s + ".png");
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                holder.imageView.setImageDrawable(drawable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}
