package com.mxfit.mentix.menu3.Beans;

import java.util.ArrayList;



public class TrainingUnit {
    private int Day;
    private int TrainingLevel;
    private String Sets;
    private Boolean Finished;

    public TrainingUnit(int mLabel, int mTrainingLevel, String mSets, Boolean mFinished)
    {
        Day = mLabel;
        TrainingLevel = mTrainingLevel;
        Sets = mSets.replace('x','-');
        Finished = mFinished;
    }

    public int getLabel() {
        return Day;
    }

    public int getTrainingLevel() {
        return TrainingLevel;
    }

    public String getSets() {
        return Sets;
    }

    public Boolean getFinished() {
        return Finished;
    }
}
