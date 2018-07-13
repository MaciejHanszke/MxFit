package com.mxfit.mentix.menu3;

class DBNameFormatter {

    String reformatTime(int seconds)
    {
        int minutes;
        int hours;

        minutes = seconds /60;
        hours = minutes / 60 ;
        seconds = seconds %60;
        minutes = minutes%60;

        String time;
        time = hours+":";
        if(minutes<10)
            time += "0"+minutes+":";
        else
            time += minutes+":";

        if(seconds<10)
            time += "0"+seconds;
        else
            time += seconds;
        return time;
    }

    String reformatName(String name, boolean substring)
    {
        String my_new_str = name.replaceAll("_", " ");
        my_new_str = my_new_str.replaceAll("ź", ":");
        my_new_str = my_new_str.replaceAll("ś", "/");
        if(substring)
        {
            my_new_str = my_new_str.substring(2);
        }
        return my_new_str;
    }

    String formatName(String name)
    {
        String my_new_str = "t_" + name.replaceAll(" ", "_");
        my_new_str = my_new_str.replaceAll(":", "ź");
        my_new_str = my_new_str.replaceAll("/", "ś");
        return my_new_str;
    }
}
