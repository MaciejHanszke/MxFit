package com.mxfit.mentix.menu3;


class StopWatch {
    private long startTime = 0;
    private boolean running = false;
    private long currentTime = 0;

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    void stop() {
        this.startTime = System.currentTimeMillis();
        this.running = false;
        currentTime = 0;
    }

    void pause() {
        this.running = false;
        currentTime = System.currentTimeMillis() - startTime;
    }
    void resume() {
        this.running = true;
        this.startTime = System.currentTimeMillis() - currentTime;
    }

    //elaspsed time in milliseconds
    public long getElapsedTimeMili() {
        long elapsed = 0;
        if (running) {
            elapsed =((System.currentTimeMillis() - startTime)/100) % 1000 ;
        }
        return elapsed;
    }

    //elaspsed time in seconds
    private long getElapsedTimeSecs() {
        long elapsed = 0;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000) % 60;
        }
        return elapsed;
    }

    long getRunningTime() {
        long elapsed = 0;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000) ;
        }
        return elapsed;
    }

    //elaspsed time in minutes
    private long getElapsedTimeMin() {
        long elapsed = 0;
        if (running) {
            elapsed = (((System.currentTimeMillis() - startTime) / 1000) / 60 ) % 60;
        }
        return elapsed;
    }

    //elaspsed time in hours
    private long getElapsedTimeHour() {
        long elapsed = 0;
        if (running) {
            elapsed = ((((System.currentTimeMillis() - startTime) / 1000) / 60 ) / 60);
        }
        return elapsed;
    }

    public String toString() {
        String minutes;
        if(getElapsedTimeMin() < 10)
        {
            minutes = "0"+getElapsedTimeMin();
        }else{
            minutes = ""+getElapsedTimeMin();
        }

        String seconds;
        if(getElapsedTimeSecs() < 10)
        {
            seconds = "0"+getElapsedTimeSecs();
        }else{
            seconds = ""+getElapsedTimeSecs();
        }
        return getElapsedTimeHour() + ":" + minutes + ":"
                + seconds;
    }
}
