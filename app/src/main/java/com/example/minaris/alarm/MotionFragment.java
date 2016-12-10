package com.example.minaris.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;

/**
 * Created by jodeneve on 23/11/2016.
 */

public class  MotionFragment extends Fragment{


    //ArrayList<String> listItems;
    MotienAdapter adapter;


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

        adapter = new MotienAdapter(getContext());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("in item on click");
                MotienData md = adapter.getItem(i);
                Intent intent = new Intent(getContext(), SetMotionActivity.class);
                intent.putExtra("theMotien", md);
                startActivity(intent);
            }

        });

       return view;
    }
    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
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
                Intent intent = new Intent(getContext(), SetMotionActivity.class);
                startActivity(intent);
            }

        });
    }
}
