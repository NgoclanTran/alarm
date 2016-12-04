package com.example.minaris.alarm;

import info.augury.devicegesturelib.IGestureDetectListener;

/**
 * Created by Minaris on 04-12-16.
 */

/**
 * This will be used when a gesture is detected and the corresponding action will be executed
 */
public class SnoozeDetecterListener implements IGestureDetectListener{
    @Override

    /**
     * This method is called when a gesture is detected
     * @param gestureID the id of the detected gesture
     *
     */
    public void onGestureDetected(int gestureID, long timestamp) {
        // This method is called when a gisture is detected
        // Action to do: snooze alarm
    }
}
