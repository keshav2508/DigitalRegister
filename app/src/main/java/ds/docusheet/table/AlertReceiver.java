package ds.docusheet.table;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/referrals-count/" + FirebaseAuth.getInstance().getUid();

        RequestQueue queue= Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String documentcount=response;
                if(Integer.parseInt(documentcount)>=3)
                {


                }
                else
                setalarm(context,intent,"Unlock Access to PDF VIP now");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "p"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);


    }

    public void setalarm(Context context,Intent intent,String s)
    {

        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "news")
                .setSmallIcon(R.drawable.icon34)
                .setContentTitle(s)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Share Digital Register With your friends & get access to PDF VIP.\n"+" Open app for more details"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());

    }
}
