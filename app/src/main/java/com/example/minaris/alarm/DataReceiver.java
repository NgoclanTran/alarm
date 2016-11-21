package com.example.minaris.alarm;

import info.augury.devicegesturelib.IGestureRecordReceiver;

/**
 * Created by jodeneve on 21/11/2016.
 */

public class DataReceiver implements IGestureRecordReceiver {

    @Override
    public void onResults(float[] side, float[] front, float[] vert) {
        System.out.println(side.length);
    }
}
