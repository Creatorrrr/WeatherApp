package com.example.kosta.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kosta on 2017-05-08.
 */

public class WeatherAdapter extends BaseAdapter {

    private Context context;
    private List<WeatherDTO> data;
    private LayoutInflater inflater;

    public WeatherAdapter(Context context, List<WeatherDTO> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView temp = (TextView)convertView.findViewById(R.id.temp);
        TextView weather = (TextView)convertView.findViewById(R.id.weather);

        temp.setText(data.get(position).getTemp());
        weather.setText(data.get(position).getWeather());

        return convertView;
    }
}
