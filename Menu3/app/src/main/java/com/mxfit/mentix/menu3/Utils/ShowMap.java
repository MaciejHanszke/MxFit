package com.mxfit.mentix.menu3.Utils;

import android.app.Activity;
import android.content.Intent;

import com.mxfit.mentix.menu3.HistoryPackage.HistoryActivity;


public class ShowMap {
    public ShowMap(Activity name, String tablename){
        DBNameFormatter dbNameFormatter = new DBNameFormatter();
        Intent history = new Intent(name, HistoryActivity.class);
        history.putExtra("tablename",dbNameFormatter.formatName(tablename));
        history.putExtra("barname",dbNameFormatter.reformatName(tablename,false));
        name.startActivity(history);
    }
}
