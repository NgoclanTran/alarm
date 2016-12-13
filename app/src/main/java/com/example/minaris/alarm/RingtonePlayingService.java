package com.example.minaris.alarm;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

import info.augury.devicegesturelib.Axis;
import info.augury.devicegesturelib.CompareMode;
import info.augury.devicegesturelib.DeviceGestureLibrary;
import info.augury.devicegesturelib.DeviceGestureModel;
import info.augury.devicegesturelib.IGestureDetectListener;
import info.augury.devicegesturelib.IGestureDetector;


public class RingtonePlayingService extends Service implements IGestureDetectListener{

    MediaPlayer media_song;
    int startId;
    String alarmId;
    boolean isRunning;
    //Set up the database helper
    private AlarmDbHelper mDbHelper;
    private SQLiteDatabase db;
    IGestureDetector detector;


    float requiredProximity = 0.75f; //Threshold of detection
    CompareMode mode = CompareMode.Flattened; //Mode of axis data comparison
    long cooldown = 1000 * 1000000; //Idleness interval after detection event in nanoseconds (1000ms)
    long deviation = 200 * 1000000; //Possible deviation of total duration in nanoseconds (200ms)


    @Override
    public void onCreate(){
        mDbHelper = new AlarmDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();
        detector = DeviceGestureLibrary.createGestureDetector(getApplicationContext());
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // fetch the extra string from the alarm on/alarm off values
        String extra = intent.getExtras().getString("extra");
        Log.e("EXTRASTRING", extra);
        // fetch the whale choice integer values
        //Integer whale_sound_choice = intent.getExtras().getInt("whale_choice");

        //Log.e("Ringtone extra is ", extra);

        // Get the state and id through the extra string in the intent
        String[] splitString = extra.split(":");
        String state = splitString[0];
        alarmId = splitString[1];


        // this converts the extra strings from the intent
        // to start IDs, values 0 or 1
        assert state != null;
        switch (state) {
            case "ON":
                startId = 1;
                break;
            case "OFF":
                startId = 0;
                //Log.e("Start ID is ", state);
                break;
            default:
                startId = 0;
                break;
        }



        // if else statements

        // if there is no music playing, and the user pressed "alarm on"
        // music should start playing
        if (!this.isRunning && startId == 1) {
            Log.e("there is no music, ", "and you want start");

            media_song = getRingtoneSong();
            media_song.start();

            this.isRunning = true;
            this.startId = 0;

            //Activate Gesture Listener
            startGestureListener();


            // set up the start command for the notification
            //notify_manager.notify(0, notification_popup);

        }

        // if there is music playing, and the user pressed "alarm off"
        // music should stop playing
        else if (this.isRunning && startId == 0) {
            //Log.e("there is music, ", "and you want end");

            // stop the ringtone
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;

            //Deactivate accelerometer listener
        }

        // these are if the user presses random buttons
        // just to bug-proof the app
        // if there is no music playing, and the user pressed "alarm off"
        // do nothing
        else if (!this.isRunning && startId == 0) {
            //Log.e("there is no music, ", "and you want end");

            this.isRunning = false;
            this.startId = 0;

        }

        // if there is music playing and the user pressed "alarm on"
        // do nothing
        else if (this.isRunning && startId == 1) {
            //Log.e("there is music, ", "and you want start");

            this.isRunning = true;
            this.startId = 1;

        }

        // can't think of anything else, just to catch the odd event
        else {
            //Log.e("else ", "somehow you reached this");

        }



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        //Log.e("on Destroy called", "ta da");

        super.onDestroy();
        this.isRunning = false;
    }

    /*
    Get the correct ringtone by accessing the database through the id of the alarm
     */
    private MediaPlayer getRingtoneSong(){
        Cursor c = db.rawQuery("SELECT * FROM " + AlarmContract.AlarmEntry.TABLE_NAME + " WHERE " + AlarmContract.AlarmEntry._ID + " = " + alarmId, null );
        c.moveToFirst();
        String ringtone = c.getString(c.getColumnIndexOrThrow(AlarmContract.AlarmEntry.RINGTONE));
        switch (ringtone) {
            case "ringtone1":
                return MediaPlayer.create(this, R.raw.ringtone1);
            case "ringtone2":
                return MediaPlayer.create(this, R.raw.ringtone2);
            default:
                return MediaPlayer.create(this, R.raw.ringtone1);

        }

    }


    public void onGestureDetected(int gestureID, long timestamp) {
        // This method is called when a gesture is detected
        // Turn alarm off when this method is called
        Log.i("Motion Detected: ",String.valueOf(gestureID));

        media_song.stop();
        media_song.reset();

        this.isRunning = false;
        this.startId = 0;

        detector.close();
    }

    public void startGestureListener() {
        Cursor c = db.rawQuery("SELECT * FROM " + AlarmContract.MotionEntry.TABLE_NAME, null);
        List<DeviceGestureModel> list = parseCursor(c);
        for (DeviceGestureModel m : list) {
            detector.registerGestureDetection(m, this);
        }
    }

    List<DeviceGestureModel> parseCursor(Cursor c) {
        ArrayList<DeviceGestureModel> modelList = new ArrayList<DeviceGestureModel>();
        c.moveToFirst();
        do {
            long itemId = c.getLong(c.getColumnIndexOrThrow(AlarmContract.AlarmEntry._ID));
            //String toggled = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.ACTIVE));
            String interval = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.INTERVAL));
            String motionName = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.MOTION_NAME));
            String x = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.X));
            String y = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.Y));
            String z = c.getString(c.getColumnIndexOrThrow(AlarmContract.MotionEntry.Z));

            long t = Long.parseLong(interval);
            int name = Integer.parseInt(motionName);
            float[] xaxis = parseAxis(x);
            float[] yaxis = parseAxis(y);
            float[] zaxis = parseAxis(z);

            Axis frontAxis = new Axis(xaxis, requiredProximity, mode);
            Axis sideAxis = new Axis(yaxis, requiredProximity, mode);
            Axis vertAxis = new Axis(zaxis, requiredProximity, mode);

            DeviceGestureModel model = new DeviceGestureModel(name, frontAxis, sideAxis, vertAxis, t, cooldown, deviation);

            modelList.add(model);
        } while (c.moveToNext());
        return modelList;
    }

    public float[] parseAxis(String axis) {
        float [] res;
        String [] axisString = axis.substring(1,axis.length()-1).split(",");
        res = new float[axisString.length];
        for (int i = 0; i < axisString.length; i++) {
            res[i] = Float.parseFloat(axisString[i]);
        }
        return res;
    }
}