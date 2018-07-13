package com.mxfit.mentix.menu3;


class PreRun {
    private int Day;
    private float Km;
    private boolean Finished;
    PreRun(int pday, float pkm, boolean pfinished){
     Day = pday;
     Km = pkm;
     Finished = pfinished;
    }

    public int getDay() {
        return Day;
    }

    float getKm() {
        return Km;
    }

    boolean isDone() {
        return Finished;
    }
}
