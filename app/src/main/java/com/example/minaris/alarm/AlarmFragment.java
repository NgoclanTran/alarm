package com.example.minaris.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by jodeneve on 23/11/2016.
 */

public class AlarmFragment extends Fragment {

    //ArrayList<String> listItems;
    AlarmAdapter adapter;

    public AlarmFragment () {
        //listItems = new ArrayList<String>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, viewGroup, false);
        final ListView lv = (ListView) view.findViewById(R.id.listviewAlarm);
//        adapter=new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_list_item_1,
//                listItems);
        adapter = new AlarmAdapter(getContext());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("in item on click");
                AlarmData ad = adapter.getItem(i);
                Intent intent = new Intent(getContext(), SetAlarmActivity.class);
                intent.putExtra("theAlarm", ad);
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
                adapter.addData();
            }

        });
    }

}
