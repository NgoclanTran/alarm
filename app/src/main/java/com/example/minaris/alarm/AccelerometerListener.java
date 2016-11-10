package com.example.minaris.alarm;

/**
 * Created by sebastianstoelen on 07/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerListener implements SensorEventListener {

    private static AccelerometerListener accelerometerListener = new AccelerometerListener();

    SensorManager manager;
    Sensor sensor;
    Context context;
    MainActivity activity;
    Boolean snoozing;
    Boolean canceling;

    private AccelerometerListener(){
        snoozing = false;
        canceling = false;
    }

    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    public void setContext(Context context){
        this.context = context;
        manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public SensorManager getManager(){
        return manager;
    }

    public Sensor getSensor(){
        return sensor;
    }

    public Context getContext(){
        return context;
    }

    public static AccelerometerListener getInstance(){
        return accelerometerListener;
    }

    float ax;
    float ay;
    float az;

    public void onSensorChanged(SensorEvent event) {
        if (Math.abs(az) > 1 && (! (snoozing || canceling ))){
            snoozing = true;
            activity.snooze();
        } else if (Math.abs(ax) > 1 && (! (snoozing || canceling ))){
            canceling = true;
            activity.cancel();
        }
        ax = event.values[0];
        ay = event.values[1];
        az = event.values[2];
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
