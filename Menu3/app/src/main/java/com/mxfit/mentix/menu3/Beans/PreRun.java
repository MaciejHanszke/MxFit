package com.mxfit.mentix.menu3.Beans;


public class PreRun {
    private int Day;
    private float Km;
    private boolean Finished;
    public PreRun(int pday, float pkm, boolean pfinished){
     Day = pday;
     Km = pkm;
     Finished = pfinished;
    }

    public int getDay() {
        return Day;
    }

    public float getKm() {
        return Km;
    }

    public boolean isDone() {
        return Finished;
    }
}
