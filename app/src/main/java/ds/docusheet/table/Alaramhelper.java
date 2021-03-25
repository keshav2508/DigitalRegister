package ds.docusheet.table;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class Alaramhelper {

    public static void setalarm(Context context)
    {
        Toast.makeText(context,"Alarm set",Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name ="news";
            String description ="Get informed with new updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("news", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(context,AlertReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,0);
        Intent intent1=new Intent(context,AlertReceiver2.class);
        PendingIntent pendingIntent1=PendingIntent.getBroadcast(context,0,intent1,0);
        Intent intent2=new Intent(context,AlertReceiver3.class);
        PendingIntent pendingIntent2=PendingIntent.getBroadcast(context,0,intent2,0);

        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 10);
        c.set(Calendar.MINUTE ,0);
        c.set(Calendar.SECOND,0);

        Calendar c1=Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, 12);
        c1.set(Calendar.MINUTE ,0);
        c1.set(Calendar.SECOND,0);
         alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
          alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,c1.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent1);
         alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),  3 * 24 * 60 * 60 * 1000 ,pendingIntent2);

    }
}