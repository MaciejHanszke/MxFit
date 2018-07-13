package com.mxfit.mentix.menu3;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mxfit.mentix.menu3.MainActivity;


class ColorLineMethods {

    final MainActivity activity = MainActivity.instance;
    private SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);



    void checkLinePrefs()
    {
        activity.isCustomLine = prefs.getBoolean("switch_running_color",false);
        activity.LineColor1 = prefs.getInt("colorR1", 0);
        activity.isMultiColorLine = prefs.getBoolean("switch_multi_color",false);
        activity.LineColor2 = prefs.getInt("colorR2", 0);
        activity.Step = prefs.getInt("slider_smoothness", 20);
        if(activity.isMultiColorLine){
            activity.TempColor = activity.LineColor1;
            activity.ColorIterator = activity.Step;
            getTempColorStep();
        }
    }

    private void getTempColorStep()
    {

        activity.stepColors = new double[3];
        activity.tempColors = new double[3];

        String hexColor1 = String.format("#%06X", (0xFFFFFF & activity.LineColor1));
        String hexColor2 = String.format("#%06X", (0xFFFFFF & activity.LineColor2));
        String hexColor3 = String.format("#%06X", (0xFFFFFF & activity.TempColor));

        int v1 = Integer.valueOf( hexColor1.substring( 1, 3 ), 16 );
        int v2 = Integer.valueOf( hexColor2.substring( 1, 3 ), 16 );

        double v3 = v1-v2;
        v3/=activity.Step;
        activity.stepColors[0] = v3;

        v1 = Integer.valueOf( hexColor1.substring( 3, 5 ), 16 );
        v2 = Integer.valueOf( hexColor2.substring( 3, 5 ), 16 );

        v3 = v1-v2;
        v3/=activity.Step;

        activity.stepColors[1] = v3;

        v1 = Integer.valueOf( hexColor1.substring( 5, 7 ), 16 );
        v2 = Integer.valueOf( hexColor2.substring( 5, 7 ), 16 );

        v3 = v1-v2;
        v3/=activity.Step;

        activity.stepColors[2] = v3;

        activity.tempColors[0] = Integer.valueOf( hexColor3.substring( 1, 3 ), 16 );
        activity.tempColors[1] = Integer.valueOf( hexColor3.substring( 3, 5 ), 16 );
        activity.tempColors[2] = Integer.valueOf( hexColor3.substring( 5, 7 ), 16 );
    }

    void setLineColor()
    {
        if(activity.isMultiColorLine)
        {
            if(activity.ColorIterator == activity.Step)
            {
                activity.goingUp = false;
            } else if(activity.ColorIterator == 0)
            {
                activity.goingUp = true;
            }

            if(activity.goingUp)
            {
                activity.tempColors[0]+=activity.stepColors[0];
                activity.tempColors[1]+=activity.stepColors[1];
                activity.tempColors[2]+=activity.stepColors[2];
                activity.ColorIterator++;
            } else {
                activity.tempColors[0]-=activity.stepColors[0];
                activity.tempColors[1]-=activity.stepColors[1];
                activity.tempColors[2]-=activity.stepColors[2];
                activity.ColorIterator--;
            }
        } else if(activity.isCustomLine)
        {
            activity.TempColor = activity.LineColor1;
        }
    }
}
