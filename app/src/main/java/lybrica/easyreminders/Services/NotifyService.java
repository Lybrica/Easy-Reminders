package lybrica.easyreminders.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import lybrica.easyreminders.R;

public class NotifyService extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            Intent cIntent = new Intent(context,NotifyActions.class);
            //cIntent.putExtra("action",1);
            cIntent.putExtra("idLong",intent.getLongExtra("idLong", 1));
            cIntent.putExtra("id",intent.getIntExtra("id", 1));
            PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, cIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Intent sIntent = new Intent(context,NotifyActions.class);
            PendingIntent snoozeIntent = PendingIntent.getBroadcast(context, 123, sIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent dIntent = new Intent(context,NotifyActions.class);
            PendingIntent doneIntent = PendingIntent.getBroadcast(context, 123, dIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent deleteIntent = PendingIntent.getActivity(context, 0, cIntent, 0);

            int colr = context.getResources().getColor(R.color.colorPrimary);

            NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_notifications_white_48dp)
            .setColor(colr)
            .setContentTitle(intent.getStringExtra("title"))
            .setContentText(intent.getStringExtra("text"))
            //.addAction(R.drawable.ic_notifications_paused, "Snooze", snoozeIntent)
            //.addAction(R.drawable.ic_done, "Done", doneIntent)
            .setLights(Color.RED, 1000, 1000)
            //.setDeleteIntent(deleteIntent)
            .setContentIntent(contentIntent);

            if (intent.getBooleanExtra("style", true)) { //insistent notifiaction check

                Intent serviceIntent = new Intent(context, AlarmService.class);
                context.startService(serviceIntent);

            } else {
                boolean vibb = prefs.getBoolean("norm_vibration", true);
                if (vibb == true) {
                    mBuilder.setVibrate(new long[]{0, 200});
                } else {
                }

                mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            }

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(intent.getIntExtra("id", 12), mBuilder.build());

            SharedPreferences sharedPrefs = context.getSharedPreferences("times", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();

            //editor.remove(String.valueOf(intent.getIntExtra("id",0)));
            editor.putLong(String.valueOf(intent.getIntExtra("id",0)),12);
            editor.commit();
        }
    }


