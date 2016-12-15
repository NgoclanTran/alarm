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

public class MotionAdapter extends BaseAdapter {

    Context context;
    List<MotionData> data;
    SQLiteDatabase db;
    AlarmDbHelper mDbHelper;

    public MotionAdapter(Context context) {
        this.context = context;
        mDbHelper = new AlarmDbHelper(context);
        db = mDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + AlarmContract.MotionEntry.TABLE_NAME,null);
        if (c.getCount() > 0){
            this.data = parseCursor(c);
        } else {
            this.data = new ArrayList<MotionData>();
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
            vi = inflater.inflate(R.layout.motien_row, null);
        }

        TextView name = (TextView) vi.findViewById(R.id.motien);
        name.setText(getItem(i).getName());

        Switch sw = (Switch) vi.findViewById(R.id.motionSwitch);
        sw.setChecked(true);

        return vi;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public MotionData getItem(int i) {
        return data.get(i);
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    List<MotionData> parseCursor(Cursor c) {
        ArrayList<MotionData> motionList = new ArrayList<MotionData>();
        c.moveToFirst();
        do {
            long itemId = c.getLong(c.getColumnIndexOrThrow(AlarmContract.AlarmEntry._ID));
            //String toggled = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.ACTIVE));
            String interval = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.INTERVAL));
            String motionName = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.MOTION_NAME));
            String x = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.X));
            String y = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.Y));
            String z = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.Z));
            MotionData data = new MotionData();
//            if (toggled.equals("true")){
//                data.toggled = true;
//            } else {
//                data.toggled = false;
//            }
            data.interval = Long.parseLong(interval);
            data.name = motionName;
            data.x = parseAxis(x);
            data.y = parseAxis(y);
            data.z = parseAxis(z);
            motionList.add(data);
        } while (c.moveToNext());
        return motionList;
    }

    public Float[] parseAxis(String axis) {
        Float [] res;
        String [] axisString = axis.substring(1,axis.length()-1).split(",");
        res = new Float[axisString.length];
        for (int i = 0; i < axisString.length; i++) {
            res[i] = Float.parseFloat(axisString[i]);
        }
        return res;
    }
}
