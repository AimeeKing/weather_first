package com.example.aimee.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Aimee on 2015/12/3.
 */
public class weather_fragment extends Fragment {
    public String city;
    public weather_fragment(String city)
    {
        this.city=city;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.content_fragment,container,false);
        TextView textView=(TextView)v.findViewById(R.id.textView);
        textView.setText(city);
        return v;
    }
}
