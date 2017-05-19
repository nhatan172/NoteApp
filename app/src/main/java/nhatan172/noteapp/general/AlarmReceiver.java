package nhatan172.noteapp.general;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import nhatan172.noteapp.R;
import nhatan172.noteapp.detail.DetailActivity;

/**
 * Created by nhata on 12/05/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private int  noteIndex = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String note = "";
        if (bundle != null){
            note = bundle.getString("NOTE");
            noteIndex = bundle.getInt("INDEX");
        }
        createNotification(context,"Note notification",note);
    }

    private void createNotification(Context context, String s, String s1) {
        Intent newIntent = new Intent(context,DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MODE",2);
        bundle.putInt("ItemPosition",noteIndex);
        newIntent.putExtras(bundle);
        PendingIntent notifiInten = PendingIntent.getActivity(context,noteIndex,newIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(s)
                .setContentText(s1);
        mBuilder.setContentIntent(notifiInten);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        mBuilder.setAutoCancel(true);
        NotificationManager notiMan = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notiMan.notify(noteIndex,mBuilder.build());

    }

}
