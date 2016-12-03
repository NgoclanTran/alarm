package com.example.minaris.alarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jodeneve on 26/11/2016.
 */

public class AlarmAdapter extends BaseAdapter {

    Context context;
    List<AlarmData> data;
    SQLiteDatabase db;
    AlarmDbHelper mDbHelper;

    public AlarmAdapter(Context context) {
        this.context = context;
        mDbHelper = new AlarmDbHelper(context);
        db = mDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + AlarmContract.AlarmEntry.TABLE_NAME,null);
        this.data = parseCursor(c);
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

        TextView date = (TextView) vi.findViewById(R.id.date);
        date.setText(getItem(i).getHourAndMinutesText());

        TextView days = (TextView) vi.findViewById(R.id.days);
        days.setText(Html.fromHtml(getItem(i).generateDaysText()));

        Switch sw = (Switch) vi.findViewById(R.id.alarmSwitch);
        sw.setChecked(getItem(i).toggled);

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

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    public void addData () {
        AlarmData ad = new AlarmData();
        ad.hour = new Date();
        data.add(ad);
        this.notifyDataSetChanged();
    }

    public ArrayList<AlarmData> parseCursor(Cursor c){
        ArrayList<AlarmData> alarmList = new ArrayList<AlarmData>();
       c.moveToFirst();
        do {
            long itemId = c.getLong(c.getColumnIndexOrThrow(AlarmContract.AlarmEntry._ID));
            String time = c.getString(c.getColumnIndexOrThrow(AlarmContract.AlarmEntry.TIME_SLOT));
            String ringtone = c.getString(c.getColumnIndexOrThrow(AlarmContract.AlarmEntry.RINGTONE));
            String snoozable = c.getString(c.getColumnIndexOrThrow(AlarmContract.AlarmEntry.SNOOZABLE));
            AlarmData data = new AlarmData();
            data.ringtone = ringtone;
            if (snoozable.equals("true")){
                data.snoozable = true;
            } else {
                data.snoozable = false;
            }
            Date date = new Date(new Long(time));
            data.hour = date;
            alarmList.add(data);

        } while (c.moveToNext());
        return alarmList;
    }
}
