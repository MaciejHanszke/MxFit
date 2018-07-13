package com.mxfit.mentix.menu3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



class SaveFileCalendar {
    private Calendar c;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date currentDate;
    final MainActivity activity = MainActivity.instance;
    SaveFileCalendar(){
        c = Calendar.getInstance();
     }

     private String convertToString(int days)
     {
         currentDate = new Date();
         c.setTime(currentDate);
         c.add(Calendar.DATE, days);
         return sdf.format(c.getTime());
     }

     void setPushupsDate()
     {
         if(activity.PUdateCount < 3)
         {
             activity.lastPushupDate = convertToString(2);
             activity.PUdateCount++;
         } else {
             activity.lastPushupDate = convertToString(3);
             activity.PUdateCount = 0;
         }

     }

     Date WhichDateFirst(String puDate, String rDate){
         if(!(puDate == null) && !(rDate == null))
         {
             Date dpuDate = toDate(puDate);
             Date drDate = toDate(rDate);
             if(dpuDate.before(drDate)) {
                 return dpuDate;
             }
             else {
                 return drDate;
             }
         }
         else if(puDate == null && !(rDate == null)) {
             return toDate(rDate);
         }
         else if(!(puDate == null) && rDate == null) {
             return toDate(puDate);
         }
         else
             return null;
     }

    void setDayDate(boolean wasPushups)
    {
        if(activity.PUdateCount < 4)
        {
            if(wasPushups)
            {
                activity.lastRunDate = convertToString(1);
                if(activity.PUdateCount != 3)
                {
                    activity.lastPushupDate = convertToString(2);
                } else {
                    activity.lastPushupDate = convertToString(3);
                }
            } else {

                activity.lastPushupDate = convertToString(1);
                if(activity.PUdateCount != 3)
                {
                    activity.lastRunDate = convertToString(2);
                } else {
                    activity.lastRunDate = convertToString(3);
                }
            }
            activity.PUdateCount++;

        } else {
            if(wasPushups)
            {
                activity.lastRunDate = convertToString(2);
                activity.lastPushupDate = convertToString(3);
            } else {
                activity.lastRunDate = convertToString(3);
                activity.lastPushupDate = convertToString(2);
            }
            activity.PUdateCount = 0;
        }

    }

    void checkDayDate()
    {
        if(activity.lastPushupDate != null && activity.lastRunDate != null) {
            Date date1 = toDate(activity.lastPushupDate);
            Date date2 = toDate(activity.lastRunDate);
            Date date3 = toDate(getToday());

            boolean firstPushups = false;

            if (date1.before(date2)) {
                firstPushups = true;
            }

            if(firstPushups && date1.before(date3))
            {
                activity.lastPushupDate = convertToString(date3);
                activity.lastRunDate = convertToString(1);
            } else if (!firstPushups && date2.before(date3))
            {
                activity.lastRunDate = convertToString(date3);
                activity.lastPushupDate = convertToString(1);
            }
        }
    }


     void checkPushupsDate()
     {
         if(activity.lastPushupDate != null) {
             Date date1 = toDate(activity.lastPushupDate);
             Date date2 = toDate(getToday());

             if (date1.before(date2)) {
                 activity.lastPushupDate = convertToString(date2);
             }
         }
     }

    void setRunningDate()
    {
        if(activity.RdateCount < 3)
        {
            activity.lastRunDate = convertToString(2);
            activity.RdateCount++;
        } else {
            activity.lastRunDate = convertToString(3);
            activity.RdateCount = 0;
        }
    }

    void checkRunningDate()
    {
        if(activity.lastRunDate != null) {
            Date date1 = toDate(activity.lastRunDate);
            Date date2 = toDate(getToday());

            if (date1.before(date2)) {
                activity.lastRunDate = convertToString(date2);
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
