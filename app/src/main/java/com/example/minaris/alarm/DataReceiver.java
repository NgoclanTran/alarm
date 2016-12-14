package com.example.minaris.alarm;

import android.util.Log;

import info.augury.devicegesturelib.IGestureRecordReceiver;

/**
 * Created by jodeneve on 21/11/2016.
 */


public class DataReceiver implements IGestureRecordReceiver {

    private float[] side;
    private float[] front;
    private float[] vert;

    private SetMotionActivity toNotify;

    public DataReceiver(){
        Log.e("DataReciever: ", "new reciever");
    }

    public DataReceiver(SetMotionActivity act) {
        this.toNotify = act;
    }

    @Override
    public void onResults(float[] side, float[] front, float[] vert) {
        Log.e("Receiver called: ","onResults");
        this.side = side;
        this.front = front;
        this.vert = vert;

        toNotify.makeReadyRegisterMotion();
        //System.out.println(side.length);
    }

    public float[] getSide(){
        return side;
    }
    public float[] getFront(){
        return front;
    }
    public float[] getVert(){
        return vert;
    }
}
