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

import info.augury.devicegesturelib.IGestureDetectListener;


public class RingtonePlayingService extends Service implements IGestureDetectListener{

    MediaPlayer media_song;
    int startId;
    String alarmId;
    boolean isRunning;
    AccelerometerListener accelerometerListener;
    //Set up the database helper
    private AlarmDbHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    public void onCreate(){
        mDbHelper = new AlarmDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        accelerometerListener = AccelerometerListener.getInstance();

        //Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // fetch the extra string from the alarm on/alarm off values
        String extra = intent.getExtras().getString("extra");
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
            //Log.e("there is no music, ", "and you want start");

            media_song = getRingtoneSong();
            media_song.start();

            this.isRunning = true;
            this.startId = 0;

            //Activate accelerometer listener
            accelerometerListener.getManager().registerListener(accelerometerListener,accelerometerListener.sensor, SensorManager.SENSOR_DELAY_GAME);


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
            accelerometerListener.getManager().unregisterListener(accelerometerListener);
            accelerometerListener.snoozing = false;
            accelerometerListener.canceling = false;
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
    }
}