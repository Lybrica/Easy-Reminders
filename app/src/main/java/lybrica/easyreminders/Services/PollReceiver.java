package lybrica.easyreminders.Services;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;

import java.util.ArrayList;

import lybrica.easyreminders.DBAdapter;
import lybrica.easyreminders.R;

public class PollReceiver extends BroadcastReceiver{



    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleAlarms(context);
    }

    static void scheduleAlarms(Context ctxt) {

        DBAdapter myDb;
        myDb = new DBAdapter(ctxt);
        myDb.open();

        SharedPreferences sharedPrefs = ctxt.getSharedPreferences("times", Context.MODE_PRIVATE);



//        ArrayList<String> arrId = myDb.getIDs();
//        ArrayList<String> arrTitle = myDb.getTitles();
//        ArrayList<String> arrContent = myDb.getContents();
//
//        Intent intent = new Intent(ctxt, NotifyService.class);
//
//        for(int i = 0; i < arrId.size(); i++){
//
//            String ii = arrId.get(i);
//            long exactTime = sharedPrefs.getLong(ii,0);
//            if (exactTime != 12) {
//                intent.putExtra("id", Integer.valueOf(ii));
//                intent.putExtra("idLong", Long.parseLong(ii));
//                intent.putExtra("title", arrTitle.get(i));
//                intent.putExtra("text", arrContent.get(i));
//                //intent.putExtra("color", imageId);
//                //intent.putExtra("style", style);
//                PendingIntent sender = PendingIntent.getBroadcast(ctxt, Integer.valueOf(ii), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                AlarmManager am = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
//                am.set(AlarmManager.RTC_WAKEUP, exactTime, sender);
//            }
//        }

        ArrayList<String> arrId = myDb.getIDs();
        ArrayList<String> arrTitle = myDb.getTitles();
        ArrayList<String> arrContent = myDb.getContents();

        Intent intent = new Intent(ctxt, NotifyService.class);

        for(int i = 0; i < arrId.size(); i++){

            String ii = arrId.get(i);

            String title = null;
            String content = null;
            int color = 0;
            int style = 0;
            Boolean styleBoot = null;

            long exactTime = sharedPrefs.getLong(ii,0);

            Cursor cursor = myDb.getRow(Long.parseLong(ii));
            if (cursor.moveToFirst()) {
                title = cursor.getString(DBAdapter.COL_TITLE);
                content = cursor.getString(DBAdapter.COL_NAME);
                color = cursor.getInt(DBAdapter.COL_FAVCOLOUR);
                style = cursor.getInt(DBAdapter.COL_STYLE);
            }

            if (style == 1)
                styleBoot = true;
            if (style == 0)
                styleBoot = false;

            if (exactTime != 12) {
                intent.putExtra("id", Integer.valueOf(ii));
                intent.putExtra("idLong", Long.parseLong(ii));
                intent.putExtra("title", title);
                intent.putExtra("text", content);
                intent.putExtra("color", color);
                intent.putExtra("style", styleBoot);
                PendingIntent sender = PendingIntent.getBroadcast(ctxt, Integer.valueOf(ii), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager am = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, exactTime, sender);
            }

            cursor.close();
        }





        myDb.close();
    }


}
