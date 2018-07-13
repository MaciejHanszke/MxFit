package com.mxfit.mentix.menu3;

import java.util.ArrayList;



public class Pushups {
    private int Day;
    private int TrainingLevel;
    private String Sets;
    private Boolean Finished;

    public Pushups(int mLabel, int mTrainingLevel, String mSets, Boolean mFinished)
    {
        Day = mLabel;
        TrainingLevel = mTrainingLevel;
        Sets = mSets.replace('x','-');
        Finished = mFinished;
    }

    int getLabel() {
        return Day;
    }

    int getTrainingLevel() {
        return TrainingLevel;
    }

    String getSets() {
        return Sets;
    }

    Boolean getFinished() {
        return Finished;
    }
}
