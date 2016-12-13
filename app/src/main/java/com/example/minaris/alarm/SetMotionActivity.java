package com.example.minaris.alarm;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

import java.util.Arrays;

import info.augury.devicegesturelib.Axis;
import info.augury.devicegesturelib.CompareMode;
import info.augury.devicegesturelib.DeviceGestureLibrary;
import info.augury.devicegesturelib.DeviceGestureModel;
import info.augury.devicegesturelib.IGestureDetector;



/**
 * Created by Minaris on 05-12-16.
 */

public class SetMotionActivity extends AppCompatActivity {
    float[] front;
    float[] side;
    float[] vert;


    Context context;
    boolean hasStarted;
    boolean isPhase1;

    Button startButton;
    TextView taskText;
    TextView motionStatus;
    long tStart;
    long tEnd;
    long interval = 4 * 100000; //Interval between measures in nanoseconds (5ms)
    long duration = 0; // duration of gesutre  (measured in nanoseconds)
    DataReceiver receiver;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    AlarmDbHelper mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setmotien);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        //Set up the database helper
        mDbHelper = new AlarmDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();


        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"Short duration (120 ms)", "Long duration (200 ms)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        hasStarted = false;
        isPhase1 = true;
        startButton = (Button) findViewById(R.id.startStopButton);
        motionStatus = (TextView) findViewById(R.id.motionStatus);
        motionStatus.setText("Phase 1");

        taskText = (TextView) findViewById(R.id.taskText);
        taskText.setText("record time of gesture");



        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("Status: ", "Fase 1 gestart");
                if (isPhase1 & !hasStarted) {
                    hasStarted = true;
                    tStart = System.nanoTime();
                    startButton.setText("STOP");


                } else if (isPhase1 & hasStarted) {
                    Log.e("Status: ", "fase 1 gestopt");
                    isPhase1 = false;
                    hasStarted = false;
                    tEnd = System.nanoTime();
                    motionStatus.setText("Phase 2");
                    taskText.setText("record gesture, click start");
                    startButton.setText("START");

                } else if (!isPhase1 & !hasStarted) {
                    taskText.setText("Do gesture again");
                    Log.e("Status: ", "fase 2 gestart");
                    startButton.setText("STOP");
                    hasStarted = true;

                    long duration = (tEnd - tStart) / 100;
                    //System.out.println(String.valueOf(duration));
                    Log.e("Duration: ", String.valueOf(duration));
                    motionStatus.setText(String.valueOf(duration));

                    //Action to record data
                    receiver = new DataReceiver();
                    Log.e("DataReceiver: ", "created");
                    Log.e("Duration: ", String.valueOf(duration));
                    int count = (int) (duration/interval);
                    Log.e("Count: ", String.valueOf(count));
                    Log.e("Interval: ", String.valueOf(interval));


                    DeviceGestureLibrary.recordGesture(context, interval, count, receiver);


                } else {
                    taskText.setText("Finished");
                    Log.e("Status: ", "fase 2 gestopt");
                    long duration = tEnd - tStart;
                    registerMotion(v.getContext(), duration);
                }


            }
        });

//        Button test = (Button) findViewById(R.id.testDetect);
//        test.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Log.e("Test Dectecion: ", "Try detect gesture");
//                detector = DeviceGestureLibrary.createGestureDetector(context);
//
//                RingtonePlayingService listener = new RingtonePlayingService();
////                if(listener == null)
////                    Log.e("Listener: ","null");
//                detector.registerGestureDetection(testModel, listener);
//
//
//            }
//        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void registerMotion(Context context, long duration) {
        //

        // Create axis for gesture model
        float[] frontAxisRecord = receiver.getFront();
        if (frontAxisRecord == null)
            System.out.println("Axis null");

        System.out.println("Front axis: " + frontAxisRecord.length);

        float requiredProximity = 0.5f; // threshold for detection
        CompareMode mode = CompareMode.Flattened; // Mode of axis data comparison
        Axis frontAxis = new Axis(frontAxisRecord, requiredProximity, mode);

        float[] sideAxisRecord = receiver.getSide();
        Axis sideAxis = new Axis(sideAxisRecord, requiredProximity, mode);

        float[] vertAxisRecord = receiver.getVert();
        Axis vertAxis = new Axis(vertAxisRecord, requiredProximity, mode);

        EditText idEditText = (EditText) findViewById(R.id.motionID);
        int id = Integer.parseInt(idEditText.getText().toString());
        long cooldown = 1000 * 1000000; //Idleness interval after detection event in nanoseconds (1000ms)
        long deviation = 200 * 1000000; //Possible deviation of total duration in nanoseconds (200ms)

        System.out.println("Front axis: " + frontAxis.toString());
        System.out.println("Side axis: " + sideAxis.toString());
        System.out.println("Vert axis: " + vertAxis.toString());


        //DeviceGestureModel model = new DeviceGestureModel(id, frontAxis, sideAxis, vertAxis, interval, cooldown, deviation);
        //testModel = model;

        addMotionToDatabase(id, frontAxisRecord, sideAxisRecord, vertAxisRecord);

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SetMotion Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void addMotionToDatabase(int id, float[] fA, float[] sA, float[] vA){
        String stringFA = Arrays.toString(fA);
        String stringSA = Arrays.toString(sA);
        String stringVA = Arrays.toString(vA);
        String stringInterval = Long.toString(interval);

        ContentValues values = new ContentValues();
        values.put(AlarmContract.MotionEntry.MOTION_NAME, Integer.toString(id));
        values.put(AlarmContract.MotionEntry.X, stringFA);
        values.put(AlarmContract.MotionEntry.Y, stringSA);
        values.put(AlarmContract.MotionEntry.Z, stringVA);
        values.put(AlarmContract.MotionEntry.INTERVAL,stringInterval);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(AlarmContract.MotionEntry.TABLE_NAME, null, values);

    }
}


