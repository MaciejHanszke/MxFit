package com.mxfit.mentix.menu3.Utils;

import android.content.Context;

import com.mxfit.mentix.menu3.R;

/**
 * Created by Mentix on 2018-03-24.
 */

public class GetColors {
    public static int getFont(Context ctx, int themeNumber){
        int textColor;
        if(themeNumber == 1)
            textColor = ctx.getResources().getColor(R.color.cardEdgeColor);
        else if(themeNumber == 2)
            textColor = ctx.getResources().getColor(R.color.colorPrimary);
        else if(themeNumber == 3)
            textColor = ctx.getResources().getColor(R.color.Gold);
        else
            textColor = ctx.getResources().getColor(R.color.cardEdgeColor);
        return textColor;
    }
}
