package com.mxfit.mentix.menu3.Beans;



public class Run {
    private String Label;
    private String Distance;
    private String Time;

    public Run(String mLabel, String mDistance, String mTime)
    {
        Label = mLabel;
        Distance = mDistance;
        Time = mTime;
    }

    public String getLabel(){return Label;}
    public String getDistance(){return Distance;}
    public String getTime(){return Time;}
}
