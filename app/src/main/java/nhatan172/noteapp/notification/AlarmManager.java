package nhatan172.noteapp.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nhatan172.noteapp.activity.MainActivity;

public class AlarmManager {
    public static void setAlarm(int id, String time, String title) {
        if (!time.equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date date = df.parse(time);
                Long alertTime = date.getTime();
                Bundle infoBunlde = new Bundle();
                infoBunlde.putInt("INDEX", id);
                infoBunlde.putString("NOTE", title);
                Intent alertIntent = new Intent(MainActivity.getAppContext(), AlarmReceiver.class);
                alertIntent.putExtras(infoBunlde);
                android.app.AlarmManager alarmManager = (android.app.AlarmManager) MainActivity
                        .getAppContext()
                        .getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, alertTime,
                        PendingIntent.getBroadcast(MainActivity.getAppContext(),
                        id, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public static void cancelAlarm(int index) {
        Intent alertIntent = new Intent(MainActivity.getAppContext(), AlarmReceiver.class);
        android.app.AlarmManager alarmManager = (android.app.AlarmManager)MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel( PendingIntent.getBroadcast(MainActivity.getAppContext(), index, alertIntent, PendingIntent.FLAG_CANCEL_CURRENT));
    }
}
