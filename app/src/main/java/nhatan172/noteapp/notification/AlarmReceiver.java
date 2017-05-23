package nhatan172.noteapp.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import nhatan172.noteapp.R;
import nhatan172.noteapp.activity.DetailActivity;


public class AlarmReceiver extends BroadcastReceiver {
    private int  mNoteIndex = 0;
    private static final String ARG_NOTE = "NOTE";
    private static final String ARG_INDEX = "INDEX";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String noteText = "";
        if (bundle != null) {
            noteText = bundle.getString(ARG_NOTE);
            mNoteIndex = bundle.getInt(ARG_INDEX);
        }
        createNotification(context, "Note notification", noteText);
    }

    private void createNotification(Context context, String title, String noteText) {
        Intent newIntent = new Intent(context,DetailActivity.class);
        Bundle bundle = new Bundle();

        bundle.putInt("MODE",2);
        bundle.putInt("ItemPosition",mNoteIndex);
        newIntent.putExtras(bundle);
        PendingIntent notifiIntent = PendingIntent.getActivity(context, mNoteIndex, newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Builder mBuilder = new Builder(context)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(noteText);
        mBuilder.setContentIntent(notifiIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        mBuilder.setAutoCancel(true);
        NotificationManager notiMan = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notiMan.notify(mNoteIndex,mBuilder.build());
    }
}
