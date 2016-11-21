package com.example.minaris.alarm;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by jodeneve on 21/11/2016.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AlarmDbHelper db = new AlarmDbHelper(this);

        super.onCreate(savedInstanceState);
    }
}
