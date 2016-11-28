package com.example.minaris.alarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.gc.materialdesign.views.ButtonRectangle;

/**
 * Created by jodeneve on 23/11/2016.
 */

public class MotionFragment extends Fragment {

    private boolean hasStarted;
    private Chronometer chronometer;
    private boolean isFase1;
    public MotionFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_motion, viewGroup, false);
        hasStarted = false;
        isFase1 = true;
        final Button startStopButton = (Button) view.findViewById(R.id.startStopButton);
//        chronometer = new Chronometer(this);

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                long elapsedTime = 0;
                /*
                 Eerste fase, bepaal hoe lang een beweging duurt
                 Button staat op "START"
                 */
                if(!hasStarted && isFase1) {
                    startStopButton.setText("Stop");
                    hasStarted = true;
                    Chronometer chrono = v.findViewById(R.id.chrono);
                    chrono.start();
                }

                /*
                Eerste fase, bepaal hoe lang een beweging duurt
                Button staat op "STOP"
                 */
                if(hasStarted && isFase1){
                    startStopButton.setText("Start");
                    hasStarted = false;
                    Chronometer chrono = v.findViewById(R.id.chrono);
                    chrono.stop();
                    elapsedTime =chrono.getBase();

                }

                /*
                Tweede fase, beweging registreren
                Button staat op "START"
                 */
                if(!hasStarted && !isFase1){
                    startStopButton.setText("STOP");
                    //zelf stoppen wanneer tijd om is
                    registerMotion();

                }


            }





        });

        return view;
    }

    private void registerMotion(){
    }
}
