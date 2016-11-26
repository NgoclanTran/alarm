package com.example.minaris.alarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import java.util.ArrayList;
import android.widget.ArrayAdapter;

/**
 * Created by jodeneve on 23/11/2016.
 */

public class AlarmFragment extends Fragment {

    ArrayList<String> listItems;
    AlarmAdapter adapter;

    public AlarmFragment () {
        listItems = new ArrayList<String>();
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
