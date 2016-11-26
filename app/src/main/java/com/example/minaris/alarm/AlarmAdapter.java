package com.example.minaris.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jodeneve on 26/11/2016.
 */

public class AlarmAdapter extends BaseAdapter {

    Context context;
    List<AlarmData> data;

    public AlarmAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<AlarmData>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vi = view;
        if(vi == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.alarm_row, null);
        }

        TextView text = (TextView) vi.findViewById(R.id.date);
        Date date = getItem(i).hour;
        String hour = ((Integer) date.getHours()).toString() +
                ":" + ((Integer) date.getMinutes()).toString();
        text.setText(hour);

        return vi;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public AlarmData getItem(int i) {
        return data.get(i);
    }

    public void addData () {
        AlarmData ad = new AlarmData();
      //DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        ad.hour = new Date();
        data.add(ad);
        this.notifyDataSetChanged();
    }
}
