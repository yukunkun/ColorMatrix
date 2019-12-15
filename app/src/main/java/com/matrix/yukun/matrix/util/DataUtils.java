package com.matrix.yukun.matrix.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: kun .
 * date:   On 2018/11/14
 */
public class DataUtils {
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static boolean isNumericzidai(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    /**
     *
     * @param time 秒
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;

    }


    public static String getDuration(long duration) {
        if(duration==0){
            return "未知";
        }
        if(duration<=60){
            return duration+"''";
        }else if(duration<60*60){
            return duration/60+"'"+duration%60+"''";
        }else if(duration<60*60*24){
            return duration/(60*60)+"°"+duration/(60*60)%60+"'"+duration%60+"''";
        }
        return "";
    }

    public static String getCurrentHour() {
        String hh = getTime(System.currentTimeMillis(), "HH");
        return hh;
    }

    public static String getDataTime(long times){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(times);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getNoteTime(long times){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH点");
        Date date = new Date(times);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getGaiaTime(long times){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(times);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getTime(long times,String format){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(times);
        res = simpleDateFormat.format(date);
        return res;
    }
}
