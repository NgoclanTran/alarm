package com.example.minaris.alarm;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;

import info.augury.devicegesturelib.Axis;
import info.augury.devicegesturelib.CompareMode;
import info.augury.devicegesturelib.DeviceGestureLibrary;
import info.augury.devicegesturelib.DeviceGestureModel;

/**
 * Created by jodeneve on 23/11/2016.
 */

public class MotionFragment extends Fragment implements View.OnClickListener{

    private boolean hasStarted;
    private Chronometer chronometer;
    private boolean isFase1;
    public MotionFragment() {}
	public Button startStopButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_motion, viewGroup, false);
        hasStarted = false;
        isFase1 = true;
        startStopButton = (Button) view.findViewById(R.id.startStopButton);
        final TextView motionStatus = (TextView) view.findViewById(R.id.motionStatus);
        motionStatus.setText("Fase 1: measure duration of motion");
		
		startStopButton.setOnClickListener(this);

   
        return view;
    }
	
	@Override
	public void onClick(){

	
		
                long duration = 0;


                // Eerste fase, bepaal hoe lang een beweging duurt
                // Button staat op "START"

                if(!hasStarted && isFase1) {
                    startStopButton.setText("Stop");
                    hasStarted = true;
                    Chronometer chrono = (Chronometer) view.findViewById(R.id.chrono);
                    chrono.start();
                }


               // Eerste fase, bepaal hoe lang een beweging duurt
               // Button staat op "STOP"

                if(hasStarted && isFase1){
                    startStopButton.setText("Start");
                    hasStarted = false;
                    Chronometer chrono = (Chronometer) view.findViewById(R.id.chrono);
                    chrono.stop();
                    duration =chrono.getBase();

                }


               // Tweede fase, beweging registreren
               // Button staat op "START"

                if(!hasStarted && !isFase1){
                    motionStatus.setText("Fase 2: record gesture");
                    startStopButton.setText("STOP");
                    //zelf stoppen wanneer tijd om is
                    registerMotion(v.getContext(),duration);

                }


            }





        });

	}

    private void registerMotion(Context context,long duration){
        // Action to record data
        DataReceiver receiver = new DataReceiver();
        long interval = 50*1000000;
        int count =(int) (duration / interval);

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


        //TODO ADD TO DATABASE
    }
}
