package com.example.minaris.alarm;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;

import info.augury.devicegesturelib.Axis;
import info.augury.devicegesturelib.CompareMode;
import info.augury.devicegesturelib.DeviceGestureLibrary;
import info.augury.devicegesturelib.DeviceGestureModel;

/**
 * Created by jodeneve on 23/11/2016.
 */

public class MotionFragment extends Fragment{


    //ArrayList<String> listItems;
    AlarmAdapter adapter;


    private boolean hasStarted;
    private Chronometer chronometer;
    private boolean isFase1;
    public MotionFragment() {}
	public Button startStopButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarm, viewGroup, false);
        final ListView lv = (ListView) view.findViewById(R.id.listviewAlarm);

        adapter = new AlarmAdapter(getContext());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("in item on click");
                AlarmData ad = adapter.getItem(i);
                Intent intent = new Intent(getContext(), setMotionActivity.class);
                intent.putExtra("theAlarm", ad);
                startActivity(intent);
            }

        });

       return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }

        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //adapter.addData();
                Intent intent = new Intent(getContext(), setMotionActivity.class);
                startActivity(intent);
            }

        });
    }
}
