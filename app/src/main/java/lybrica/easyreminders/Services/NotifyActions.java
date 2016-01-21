package lybrica.easyreminders.Services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import lybrica.easyreminders.Activities.MainActivity;

public class NotifyActions extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(intent.getIntExtra("id", 12));

            Intent i = new Intent();
            i.setClass(context, MainActivity.class);
            i.putExtra("more", 1);
            i.putExtra("idLong",intent.getLongExtra("idLong", 1));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent all = new Intent(context ,AlarmService.class);
            context.stopService(all); // Stop alarm
            context.startActivity(i); // Start main with// popup window




    }
}
