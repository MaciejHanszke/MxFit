package com.mxfit.mentix.menu3;

import android.content.SharedPreferences;

public class GlobalValues {
    //trainings
    public static int pushupsDay = 1;
    public static int pushupsTraininglevel = 1;

    public static int rday = 1;
    public static float goal = 1;

    public static int situpsDay = 1;
    public static int situpsTraininglevel = 1;


    public static int pushupsDone = 0;
    public static int situpsDone = 0;

    public static String lastPushupDate;
    public static String lastSitupDate;
    public static String lastRunDate;
    public static boolean Synchronised = false;

    public static int PUdateCount = 0;
    public static int SUdateCount = 0;
    public static int RdateCount = 0;

    //Settings preferences
    public static SharedPreferences prefs;
    public static int welcomeScreen = 0;
    public static int REFRESH = 2;

    //checkLinePrefs()
    public static boolean isCustomLine;
    public static int LineColor1;
    public static boolean isMultiColorLine;
    public static int LineColor2;
    public static boolean goingUp = true;
    public static int TempColor;
    public static int Step = 20;
    public static int ColorIterator;
    public static double[] stepColors;
    public static double[] tempColors;

    public static Float situpsCalibrationLow;
    public static Float situpsCalibrationHigh;


    public static boolean AlarmEnabled;
    public static boolean FirstZoom = true;

    public static void saveTrainingData() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("Pushups_day", pushupsDay);
        editor.putInt("Training_level", pushupsTraininglevel);

        editor.putInt("Situps_day", situpsDay);
        editor.putInt("Situps_Training_level", situpsTraininglevel);
        editor.putInt("Situps_number", situpsDone);
        editor.putString("Date_situps", lastSitupDate);

        editor.putInt("Pushups_number", pushupsDone);

        editor.putInt("Pushups_break_number", PUdateCount);
        editor.putString("Date_pushup", lastPushupDate);

        editor.putInt("Running_day", rday);
        editor.putFloat("Running_goal", goal);
        editor.putInt("Running_break_number", RdateCount);
        editor.putString("Date_running", lastRunDate);

        if(situpsCalibrationLow == null)
            situpsCalibrationLow = -1.0f;
        editor.putFloat("situps_low", situpsCalibrationLow);

        if(situpsCalibrationHigh == null)
            situpsCalibrationHigh = -1.0f;
        editor.putFloat("situps_high", situpsCalibrationHigh);

        editor.putBoolean("Synchronise_Pushups_Running", Synchronised);
        editor.apply();

    }

    public static void loadTrainingData(){
        pushupsDay = prefs.getInt("Pushups_day",1);
        pushupsTraininglevel = prefs.getInt("Training_level",1);
        pushupsDone = prefs.getInt("Pushups_number",0);

        situpsDay = prefs.getInt("Situps_day",1);
        situpsTraininglevel = prefs.getInt("Situps_Training_level",1);
        situpsDone = prefs.getInt("Situps_number",1);
        lastSitupDate = prefs.getString("Date_situps", null);


        PUdateCount = prefs.getInt("Pushups_break_number",0);
        lastPushupDate = prefs.getString("Date_pushup", null);

        rday = prefs.getInt("Running_day",1);
        goal = prefs.getFloat("Running_goal", (float) 1.0);
        RdateCount = prefs.getInt("Running_break_number",0);
        lastRunDate = prefs.getString("Date_running", null);

        situpsCalibrationLow = prefs.getFloat("situps_low", -1.0f);
        if(situpsCalibrationLow == -1.0f)
            situpsCalibrationLow = null;

        situpsCalibrationHigh = prefs.getFloat("situps_high", -1.0f);
        if(situpsCalibrationHigh == -1.0f)
            situpsCalibrationHigh = null;

        Synchronised = prefs.getBoolean("Synchronise_Pushups_Running",false);
    }


}
