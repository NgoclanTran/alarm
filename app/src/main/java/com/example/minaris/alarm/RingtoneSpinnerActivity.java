package com.example.minaris.alarm;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by sebastianstoelen on 03/12/2016.
 */

public class RingtoneSpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private  String selectedRingtone;

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        //get the selected ringtone
        selectedRingtone = (String) parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public String getSelectedRingtone(){
        return selectedRingtone;
    }
}
