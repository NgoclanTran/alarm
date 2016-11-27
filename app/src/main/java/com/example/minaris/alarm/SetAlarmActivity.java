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
import android.widget.Button;
import android.widget.TextView;
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
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    AccelerometerListener acceloremeter_listener;
    float ax, ay, az;
    int choose_whale_sound;
    Calendar calendar;
    Intent my_intent;
    AlarmDbHelper mDbHelper;
    SQLiteDatabase db;


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

        //initialize our text update box
        //update_text = (TextView) findViewById(R.id.update_text);

        // create an instance of a calendar
        calendar = Calendar.getInstance();

        // create an intent to the Alarm Receiver class
        my_intent = new Intent(this.context, Alarm_Receiver.class);

        mDbHelper = new AlarmDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();

/*
        // create the spinner in the main UI
        Spinner spinner = (Spinner) findViewById(R.id.richard_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.whale_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set an onclick listener to the onItemSelected method
        spinner.setOnItemSelectedListener(this);
*/
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

                // get the int values of the hour and minute
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                // convert the int values to strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                // convert 24-hour time to 12-hour time
                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    //10:7 --> 10:07
                    minute_string = "0" + String.valueOf(minute);
                }

                // method that changes the update text Textbox
                //set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);

                // put in extra string into my_intent
                // tells the clock that you pressed the "alarm on" button
                my_intent.putExtra("extra", "alarm on");

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

                addAlarmToDatabase(hour_string + ":" + minute_string);

            }





        });

       /* // initialize the stop button
        Button alarm_snooze = (Button) findViewById(R.id.alarm_snooze);
        // create an onClick listener to stop the alarm or undo an alarm set


        alarm_snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataReceiver receiver = new DataReceiver();
                long interval = 50 * 1000000; //Interval between measures in nanoseconds (50ms)
                int count = 10; //Number of measures

                DeviceGestureLibrary.recordGesture(context, interval, count, receiver);
            }
        });*/



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

        Log.e("Acc Z", String.valueOf(acceloremeter_listener.az));

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

        calendar.getInstance();
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
    };

    /*
    Method to add an alarm for a given time to the database
     */
    public void addAlarmToDatabase(String timeString){
        System.out.println("ADDALARM");
        ContentValues values = new ContentValues();
        values.put(AlarmContract.AlarmEntry.TIME_SLOT, timeString);
        values.put(AlarmContract.AlarmEntry.RINGTONE, "ringtone");
        values.put(AlarmContract.AlarmEntry.SNOOZABLE, "true");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(AlarmContract.AlarmEntry.TABLE_NAME, null, values);
    }
}