package com.example.minaris.alarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jodeneve on 08/12/2016.
 */

public class MotienAdapter extends BaseAdapter {

    Context context;
    List<MotienData> data;
    SQLiteDatabase db;
    AlarmDbHelper mDbHelper;

    public MotienAdapter(Context context) {
        this.context = context;
        mDbHelper = new AlarmDbHelper(context);
        db = mDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + AlarmContract.MotionEntry.TABLE_NAME,null);
        if (c.getCount() > 0){
            this.data = parseCursor(c);
        } else {
            this.data = new ArrayList<MotienData>();
        }

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

        TextView name = (TextView) vi.findViewById(R.id.date);
        name.setText(getItem(i).getName());

        Switch sw = (Switch) vi.findViewById(R.id.alarmSwitch);
        sw.setChecked(getItem(i).toggled);

        return vi;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public MotienData getItem(int i) {
        return data.get(i);
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    List<MotienData> parseCursor(Cursor c) {
        ArrayList<MotienData> motienList = new ArrayList<MotienData>();
        c.moveToFirst();
        do {

        } while (c.moveToNext());
        return motienList;
    }
}
