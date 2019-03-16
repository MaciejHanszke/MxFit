package com.mxfit.mentix.menu3.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mxfit.mentix.menu3.MainActivity;

import static com.mxfit.mentix.menu3.GlobalValues.*;


public class ColorLineMethods {

    final MainActivity activity = MainActivity.instance;
    private SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);



    public void checkLinePrefs()
    {
        isCustomLine = prefs.getBoolean("switch_running_color",false);
        LineColor1 = prefs.getInt("colorR1", 0);
        isMultiColorLine = prefs.getBoolean("switch_multi_color",false);
        LineColor2 = prefs.getInt("colorR2", 0);
        Step = prefs.getInt("slider_smoothness", 20);
        if(isMultiColorLine){
            TempColor = LineColor1;
            ColorIterator = Step;
            getTempColorStep();
        }
    }

    private void getTempColorStep()
    {

        stepColors = new double[3];
        tempColors = new double[3];

        String hexColor1 = String.format("#%06X", (0xFFFFFF & LineColor1));
        String hexColor2 = String.format("#%06X", (0xFFFFFF & LineColor2));
        String hexColor3 = String.format("#%06X", (0xFFFFFF & TempColor));

        int v1 = Integer.valueOf( hexColor1.substring( 1, 3 ), 16 );
        int v2 = Integer.valueOf( hexColor2.substring( 1, 3 ), 16 );

        double v3 = v1-v2;
        v3/=Step;
        stepColors[0] = v3;

        v1 = Integer.valueOf( hexColor1.substring( 3, 5 ), 16 );
        v2 = Integer.valueOf( hexColor2.substring( 3, 5 ), 16 );

        v3 = v1-v2;
        v3/=Step;

        stepColors[1] = v3;

        v1 = Integer.valueOf( hexColor1.substring( 5, 7 ), 16 );
        v2 = Integer.valueOf( hexColor2.substring( 5, 7 ), 16 );

        v3 = v1-v2;
        v3/=Step;

        stepColors[2] = v3;

        tempColors[0] = Integer.valueOf( hexColor3.substring( 1, 3 ), 16 );
        tempColors[1] = Integer.valueOf( hexColor3.substring( 3, 5 ), 16 );
        tempColors[2] = Integer.valueOf( hexColor3.substring( 5, 7 ), 16 );
    }

    public void setLineColor()
    {
        if(isMultiColorLine)
        {
            if(ColorIterator == Step)
            {
                goingUp = false;
            } else if(ColorIterator == 0)
            {
                goingUp = true;
            }

            if(goingUp)
            {
                tempColors[0]+=stepColors[0];
                tempColors[1]+=stepColors[1];
                tempColors[2]+=stepColors[2];
                ColorIterator++;
            } else {
                tempColors[0]-=stepColors[0];
                tempColors[1]-=stepColors[1];
                tempColors[2]-=stepColors[2];
                ColorIterator--;
            }
        } else if(isCustomLine)
        {
            TempColor = LineColor1;
        }
    }
}
