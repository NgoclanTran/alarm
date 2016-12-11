package com.example.minaris.alarm;

import com.example.minaris.alarm.AlarmDectecListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.os.SystemClock;
import android.util.Log;

import info.augury.devicegesturelib.Axis;
import info.augury.devicegesturelib.CompareMode;
import info.augury.devicegesturelib.DeviceGestureLibrary;
import info.augury.devicegesturelib.DeviceGestureModel;
import info.augury.devicegesturelib.IGestureDetector;




/**
 * Created by Minaris on 05-12-16.
 */

public class SetMotionActivity extends AppCompatActivity {
    Context context;
    boolean hasStarted;
    boolean isPhase1;
    Button startButton;
    TextView motionStatus;
    long tStart;
    long tEnd;

    DeviceGestureModel testModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setmotien);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;


        hasStarted = false;
        isPhase1 = true;
        startButton = (Button) findViewById(R.id.startStopButton);
        motionStatus = (TextView) findViewById(R.id.motionStatus);
        motionStatus.setText("Fase 1: measure duration of motion");


        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isPhase1){
                    motionStatus.setText("Phase 1: start record duration!");
                    isPhase1 = false;
                    tStart = System.currentTimeMillis();
                    startButton.setText("STOP");

                }else{
                    motionStatus.setText("Phase 2: record motion");
                    isPhase1 = true;
                    tEnd = System.currentTimeMillis();
                    long duration = (tEnd - tStart)/1000;
                    System.out.println(String.valueOf(duration));
                    Log.e("Duration: ", String.valueOf(duration));
                    motionStatus.setText(String.valueOf(duration));
                    registerMotion(v.getContext(),duration);
                }


            }

//                long duration = 0;
//                // Eerste fase, bepaal hoe lang een beweging duurt
//                // Button staat op "START"
//                System.out.println("START BUTTON CLICKED");
//
//                if(!hasStarted && isPhase1) {
//                    startStopButton.setText("Stop");
//                    hasStarted = true;
//                    Chronometer chrono = (Chronometer) findViewById(R.id.chrono);
//                    chrono.setBase(SystemClock.elapsedRealtime());
//                    chrono.start();
//                    System.out.println("START");
//                }
//
//
//                // Eerste fase, bepaal hoe lang een beweging duurt
//                // Button staat op "STOP"
//
//                if(hasStarted && isPhase1){
//                    startStopButton.setText("Start");
//                    hasStarted = false;
//                    Chronometer chrono = (Chronometer) findViewById(R.id.chrono);
//                    chrono.stop();
//                    duration =chrono.getBase();
//                    System.out.println("STOP");
//
//                }
//
//
//                // Tweede fase, beweging registreren
//                // Button staat op "START"
//
//                if(!hasStarted && !isPhase1){
//                    motionStatus.setText("Fase 2: record gesture");
//                    startStopButton.setText("STOP");
//                    //zelf stoppen wanneer tijd om is
//                    registerMotion(v.getContext(),duration);
//
//                }
//
//
//            }
//



        });

//        Button test = (Button) findViewById(R.id.testDetect);
//        test.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                IGestureDetector detector = DeviceGestureLibrary.createGestureDetector(context);
//                AlarmDectecListener listener = new AlarmDectecListener();
//                detector.registerGestureDetection(testModel,listener);
//
//
//            }
//        });


    }

    private void registerMotion(Context context,long duration){
        // Action to record data
        DataReceiver receiver = new DataReceiver();
        long interval = 50*1000000;
        //int count =(int) (duration / interval);
        int count = 10;

        DeviceGestureLibrary.recordGesture(context, interval, count, receiver);

        // Create axis for gesture model
        float[] frontAxisRecord = receiver.getFront();
        float requiredProximity = 0.72f; // threshold for detection
        CompareMode mode = CompareMode.Flattened; // Mode of axis data comparison
        Axis frontAxis = new Axis(frontAxisRecord,requiredProximity,mode);

        float[] sideAxisRecord = receiver.getSide();
        Axis sideAxis = new Axis(sideAxisRecord,requiredProximity,mode);

        float[] vertAxisRecord = receiver.getVert();
        Axis vertAxis = new Axis(vertAxisRecord,requiredProximity,mode);

        int id = 100; // TODO gebruiker moet naam geven, nog niet in design
        long cooldown = 1000 * 1000000; //Idleness interval after detection event in nanoseconds (1000ms)
        long deviation = 200 * 1000000; //Possible deviation of total duration in nanoseconds (200ms)

        DeviceGestureModel model = new DeviceGestureModel(id, frontAxis, sideAxis, vertAxis, interval, cooldown, deviation);
        testModel = model;

        //TODO ADD TO DATABASE
    }

}

