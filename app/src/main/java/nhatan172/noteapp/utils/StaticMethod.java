package nhatan172.noteapp.utils;

import android.text.Editable;

import java.util.Calendar;


public class StaticMethod {
    //Clean space in head of string
    public static String handleString(Editable editable){
        String s = editable.toString();
        int i = 0;
        while(i<s.length())
            if(s.charAt(i) == ' ')
                i++;
            else break;
        s = s.substring(i,s.length());
        return s;
    }
    public static String reformTime(int hour){
        return hour > 9 ? String.format("%d",hour): String.format("0%d",hour);
    }
    public static String getCurrentDay(){
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
        case Calendar.SUNDAY:
            return "Sunday";
        case Calendar.MONDAY:
            return "Monday";
        case Calendar.THURSDAY:
            return "Thursday";
        case Calendar.WEDNESDAY:
            return "Wednesday";
        case Calendar.TUESDAY:
            return "Tuesday";
        case Calendar.FRIDAY:
            return "Friday";
        case Calendar.SATURDAY:
            return "Saturday";
        default :
            return "Monday";
        }
    }
}
