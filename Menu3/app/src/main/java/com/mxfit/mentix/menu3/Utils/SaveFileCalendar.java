package com.mxfit.mentix.menu3.Utils;

import com.mxfit.mentix.menu3.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.mxfit.mentix.menu3.GlobalValues.*;


public class SaveFileCalendar {
    private Calendar c;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date currentDate;
    final MainActivity activity = MainActivity.instance;
    public SaveFileCalendar(){
        c = Calendar.getInstance();
     }

     private String convertToString(int days)
     {
         currentDate = new Date();
         c.setTime(currentDate);
         c.add(Calendar.DATE, days);
         return sdf.format(c.getTime());
     }

     public void setPushupsDate()
     {
         if(PUdateCount < 3)
         {
             lastPushupDate = convertToString(2);
             PUdateCount++;
         } else {
             lastPushupDate = convertToString(3);
             PUdateCount = 0;
         }

     }

    public void setSitupsDate()
    {
        if(SUdateCount < 3)
        {
            lastSitupDate = convertToString(2);
            SUdateCount++;
        } else {
            lastSitupDate = convertToString(3);
            SUdateCount = 0;
        }

    }

    public Date WhichDateFirst(ArrayList<String> date){
        if(date.size() == 0) return null;
            Date [] dates = new Date[date.size()];
            for(int i = 0; i<date.size(); i++)
                dates[i] = toDate(date.get(i));
            Arrays.sort(dates);
            return dates[0];
    }

    public void setDayDate(boolean wasPushups)
    {
        if(PUdateCount < 4)
        {
            if(wasPushups)
            {
                lastRunDate = convertToString(1);
                if(PUdateCount != 3)
                {
                    lastPushupDate = convertToString(2);
                } else {
                    lastPushupDate = convertToString(3);
                }
            } else {

                lastPushupDate = convertToString(1);
                if(PUdateCount != 3)
                {
                    lastRunDate = convertToString(2);
                } else {
                    lastRunDate = convertToString(3);
                }
            }
            PUdateCount++;

        } else {
            if(wasPushups)
            {
                lastRunDate = convertToString(2);
                lastPushupDate = convertToString(3);
            } else {
                lastRunDate = convertToString(3);
                lastPushupDate = convertToString(2);
            }
            PUdateCount = 0;
        }

    }

    public void checkDayDate()
    {
        if(lastPushupDate != null && lastRunDate != null) {
            Date date1 = toDate(lastPushupDate);
            Date date2 = toDate(lastRunDate);
            Date date3 = toDate(getToday());

            boolean firstPushups = false;

            if (date1.before(date2)) {
                firstPushups = true;
            }

            if(firstPushups && date1.before(date3))
            {
                lastPushupDate = convertToString(date3);
                lastRunDate = convertToString(1);
            } else if (!firstPushups && date2.before(date3))
            {
                lastRunDate = convertToString(date3);
                lastPushupDate = convertToString(1);
            }
        }
    }


     public void checkPushupsDate()
     {
         if(lastPushupDate != null) {
             Date date1 = toDate(lastPushupDate);
             Date date2 = toDate(getToday());

             if (date1.before(date2)) {
                 lastPushupDate = convertToString(date2);
             }
         }
     }

    public void setRunningDate()
    {
        if(RdateCount < 3)
        {
            lastRunDate = convertToString(2);
            RdateCount++;
        } else {
            lastRunDate = convertToString(3);
            RdateCount = 0;
        }
    }

    public void checkRunningDate()
    {
        if(lastRunDate != null) {
            Date date1 = toDate(lastRunDate);
            Date date2 = toDate(getToday());

            if (date1.before(date2)) {
                lastRunDate = convertToString(date2);
            }
        }
    }

    private String convertToString(Date date)
    {
        currentDate = date;
        c.setTime(currentDate);
        return sdf.format(c.getTime());
    }

    private String getToday()
    {
        currentDate = new Date();
        return sdf.format(currentDate);
    }

    private Date toDate(String Date)
    {
        try{
         c.setTime(sdf.parse(Date));
        }catch(ParseException e){
            e.printStackTrace();
        }
        return c.getTime();
    }

}
