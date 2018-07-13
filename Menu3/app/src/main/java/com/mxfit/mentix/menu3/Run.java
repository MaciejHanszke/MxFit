package com.mxfit.mentix.menu3;



class Run {
    private String Label;
    private String Distance;
    private String Time;

    Run(String mLabel, String mDistance, String mTime)
    {
        Label = mLabel;
        Distance = mDistance;
        Time = mTime;
    }

    String getLabel(){return Label;}
    String getDistance(){return Distance;}
    public String getTime(){return Time;}
}
