package com.example.minaris.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;

import com.gc.materialdesign.views.ButtonRectangle;

import info.augury.devicegesturelib.DeviceGestureLibrary;

@TargetApi(Build.VERSION_CODES.M)
public class SetAlarmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //to make our alarm manager
    AlarmManager alarm_manager;
    NotificationManager notify_manager;
    Notification notification_popup;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pending_intent;
    AccelerometerListener acceloremeter_listener;
    int choose_whale_sound;
    Calendar calendar;
    Intent my_intent;
    AlarmDbHelper mDbHelper;
    SQLiteDatabase db;
    RingtoneSpinnerActivity ringtoneSpinnerActivity;
    Switch snoozeSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setclock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        //Initialize sensor

        acceloremeter_listener = AccelerometerListener.getInstance();
        acceloremeter_listener.setContext(context);
        acceloremeter_listener.setActivity(this);
        //acceloremeter_listener.getManager().registerListener(acceloremeter_listener, acceloremeter_listener.getSensor(), SensorManager.SENSOR_DELAY_GAME);


        // initialize our alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize our timepicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        // create an instance of a calendar
        calendar = Calendar.getInstance();

        // create an intent to the Alarm Receiver class
        my_intent = new Intent(this.context, Alarm_Receiver.class);

        //Set up the database helper
        mDbHelper = new AlarmDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();

        //set up the notification manager
        notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // set up an intent that goes to the Main Activity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), SetAlarmActivity.class);
        // set up a pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                intent_main_activity, 0);

        // make the notification parameters
        notification_popup = new Notification.Builder(this)
                .setContentTitle("SmartAlarm")
                .setContentText("Alarm is snoozed for 2 minutes")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)
                .build();

        /*
         * Initialize the switch to select wheter an alarm is snoozable or not
         */
        snoozeSwitch = (Switch) findViewById(R.id.snooze_switch);

        //set the switch to ON
        snoozeSwitch.setChecked(true);

        /*
         * Initialize the spinner to select the appropriate ringtone
         */

        Spinner spinner = (Spinner) findViewById(R.id.ringtone_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ringtones_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Create new ringtoneSpinnerActivity
        ringtoneSpinnerActivity = new RingtoneSpinnerActivity();
        // Set the newly created activity to the spinner
        spinner.setOnItemSelectedListener(ringtoneSpinnerActivity);


        // initialize start button
        ButtonRectangle alarm_on = (ButtonRectangle) findViewById(R.id.alarm_on);

        // create an onClick listener to start the alarm
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                // setting calendar instance with the hour and minute that we picked
                // on the time picker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);


                //Add the newly created alarm to the database and get its row Id
                long alarmId = addAlarmToDatabase(calendar.getTimeInMillis(), "repeat");

                // put in extra string into my_intent
                // tells the clock that you pressed the "alarm on" button and wich alarm is added
                my_intent.putExtra("extra", "ON:" + alarmId);

                // put in an extra int into my_intent
                // tells the clock that you want a certain value from the drop-down menu/spinner
                //my_intent.putExtra("whale_choice", choose_whale_sound);
                //Log.e("The whale id is" , String.valueOf(choose_whale_sound));

                // create a pending intent that delays the intent
                // until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(SetAlarmActivity.this, 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // set the alarm manager
                alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);


            }





        });




    }

    /*private void set_alarm_text(String output) {
        update_text.setText(output);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        // outputting whatever id the user has selected
        //Toast.makeText(parent.getContext(), "the spinner item is "
        //        + id, Toast.LENGTH_SHORT).show();
        choose_whale_sound = (int) id;


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }

    public void snooze(){
        // method that changes the update text Textbox
        //set_alarm_text("Alarm motion snooze!");

        // cancel the alarm
        if(pending_intent == null){
            return;
        }

        //Log.e("Acc Z", String.valueOf(acceloremeter_listener.az));

        alarm_manager.cancel(pending_intent);

        // put extra string into my_intent
        // tells the clock that you pressed the "alarm off" button
        my_intent.putExtra("extra", "alarm snooze");
        // also put an extra int into the alarm off section
        // to prevent crashes in a Null Pointer Exception
        // my_intent.putExtra("whale_choice", choose_whale_sound);


        // stop the ringtone
        sendBroadcast(my_intent);

        my_intent.putExtra("extra", "alarm on");

        // put in an extra int into my_intent
        // tells the clock that you want a certain value from the drop-down menu/spinner
        //my_intent.putExtra("whale_choice", choose_whale_sound);
        //Log.e("The whale id is" , String.valueOf(choose_whale_sound));

        // create a pending intent that delays the intent
        // until the specified calendar time
        pending_intent = PendingIntent.getBroadcast(SetAlarmActivity.this, 0,
                my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar.getInstance();
        calendar.add(Calendar.SECOND,20);

        // set the alarm manager
        alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pending_intent);

        notify_manager.notify(0, notification_popup);
    }

    public void cancel() {

        // method that changes the update text Textbox
        //set_alarm_text("Alarm motion off!");

        // cancel the alarm
        if (pending_intent == null) {
            return;
        }

        Log.e("Acc X", String.valueOf(acceloremeter_listener.ax));

        alarm_manager.cancel(pending_intent);

        // put extra string into my_intent
        // tells the clock that you pressed the "alarm off" button
        my_intent.putExtra("extra", "alarm off");
        // also put an extra int into the alarm off section
        // to prevent crashes in a Null Pointer Exception
        // my_intent.putExtra("whale_choice", choose_whale_sound);


        // stop the ringtone
        sendBroadcast(my_intent);
    }

    /*
    Method to add an alarm for a given time to the database

    @param millis: the time the alarm should go off, specified in milliseconds
    @param ringtone: the name of the ringtone file that should be played when the alarm goes off
    @param snoozable: indicates wheter it is possible to snooze this alarm (1 = snoozable)
    @param active: indicates whether this alarm is currently activated (1 = active)
    @param reapeat: string containing the days the alarm should go off "Mon Tue Wed Thu Fri Sat Sun"
     */
    public long addAlarmToDatabase(Long millis, String repeat){

        //Code to clear database everytime
        //TODO: Delete this line for final version
        db.execSQL("delete from " + AlarmContract.AlarmEntry.TABLE_NAME);
        
        int snoozable = 1;
        if (! snoozeSwitch.isChecked()) {
            snoozable = 0;
        }
        

        ContentValues values = new ContentValues();
        values.put(AlarmContract.AlarmEntry.TIME_SLOT, millis.toString());
        values.put(AlarmContract.AlarmEntry.RINGTONE, ringtoneSpinnerActivity.getSelectedRingtone());
        values.put(AlarmContract.AlarmEntry.SNOOZABLE, snoozable);
        values.put(AlarmContract.AlarmEntry.ACTIVE, 1);
        values.put(AlarmContract.AlarmEntry.REPEAT,repeat);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(AlarmContract.AlarmEntry.TABLE_NAME, null, values);

        Intent intent = new Intent(this.context, MainActivity.class);
        startActivity(intent);

        return newRowId;
    }
}