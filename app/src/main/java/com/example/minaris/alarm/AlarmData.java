package com.example.minaris.alarm;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jodeneve on 26/11/2016.
 */

public class AlarmData implements Serializable {

    public boolean mon;
    public boolean tue;
    public boolean wed;
    public boolean thu;
    public boolean fri;
    public boolean sat;
    public boolean sun;
    public boolean snoozable;
    public String ringtone;

    public Date hour;

    public boolean toggled;

    public AlarmData () {}

    public String getHourAndMinutesText() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(this.hour);
    }

    public String generateDaysText() {
        String text = "";
        text += mon ? "<b> Mon </b>" : " Mon ";
        text += tue ? "<b> Tue </b>" : " Tue ";
        text += wed ? "<b> Wed </b>" : " Wed ";
        text += thu ? "<b> Thu </b>" : " Thu ";
        text += fri ? "<b> Fri </b>" : " Fri ";
        text += sat ? "<b> Sat </b>" : " Sat ";
        text += sun ? "<b> Sun </b>" : " Sun ";
        return text;
    }
}
