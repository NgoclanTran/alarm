package com.example.minaris.alarm;

import java.io.Serializable;

/**
 * Created by jodeneve on 08/12/2016.
 */

public class MotionData implements Serializable{

    public String name;
    public long interval;
    public Boolean toggled;

    public Float[] x;
    public Float[] y;
    public Float[] z;

    public String getName() {
        return name;
    }

}
