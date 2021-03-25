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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

public class AlertReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int max1 = 6,max2 = 7,max3 = 2,max4 = 1,max5 = 1,max6 = 1,max7 = 2 ;
        int min = 0;
        int range1 = max1 - min + 1;
        int range2 = max2 - min + 1;
        int range3 = max3 - min + 1;
        int range4 = max4 - min + 1;
        int range5 = max5 - min + 1;
        int range6 = max6 - min + 1;
        int range7 = max7 - min + 1;
        int rand1 = (int)(Math.random() * range1) + min;
        int rand2 = (int)(Math.random() * range2) + min;
        int rand3 = (int)(Math.random() * range3) + min;
        int rand4 = (int)(Math.random() * range4) + min;
        int rand5 = (int)(Math.random() * range5) + min;
        int rand6 = (int)(Math.random() * range6) + min;
        int rand7 = (int)(Math.random() * range7) + min;
        String template[]={"Shop","Student","Teachers","Household","Health and fitness","Cabs transport","Wedding"};
        String type1[]={"Cashbook","Payment","Daily spend","Orders","Sell register","Salary book","Attendance"};
            String type2[]={"Topic name","Homework","Time table","Class notes","Cash splitting","Course planning","Important dates","Exams schedule"};
        String type3[]={"Fee register","Stationary register","Student marks"};
        String type4[]={"Grocery list","Daily spend"};
        String type5[]={"Diet plan","Workout plan"};
        String type6[]={"Trip register","Car service"};
        String type7[]={"Guest list","Food menu","Expenditure list"};
        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-all-document/" + FirebaseAuth.getInstance().getUid();

        RequestQueue queue= Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String msg="";
                try {
                    JSONObject jsonObject=null;
                    JSONArray jsonArray = response.getJSONArray("document");
                    for (int i = 0; i < jsonArray.length(); i++)
                     jsonObject = jsonArray.getJSONObject(i);

                        response=jsonObject;

                        if(response.getString("template").equals("normal"))
                        {}
                        else
                        {
                     if (template[0].equals(response.getString("template"))) {
                         msg=String.valueOf(type1[rand1]);
                    } else if (template[1].equals(response.getString("template"))) {
                         msg=String.valueOf(type2[rand2]);
                    } else if (template[2].equals(response.getString("template"))) {
                        msg=String.valueOf(type3[rand3]);
                    } else if (template[3].equals(response.getString("template"))) {
                              if(response.getString("template_type").equals(type4[0]))
                              {
                                  msg=String.valueOf(type4[1]);
                              }
                              else
                              {
                                  msg=String.valueOf(type4[0]);
                              }
                    } else if (template[4].equals(response.getString("template"))) {
                        if(response.getString("template_type").equals(type5[0]))
                        {
                            msg=String.valueOf(type5[1]);
                        }
                        else
                        {
                            msg=String.valueOf(type5[0]);
                        }
                    } else if (template[5].equals(response.getString("template"))) {
                        if(response.getString("template_type").equals(type6[0]))
                        {
                            msg=String.valueOf(type6[1]);
                        }
                        else
                        {
                            msg=String.valueOf(type6[0]);
                        }
                    } else {
                       msg=String.valueOf(type7[rand7]);
                    }
                    setalarm(context,intent, msg);
                }}
                catch(Exception e)
                {
                    Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);

    }
    public void setalarm(Context context,Intent intent,String s)
    {

        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "news")
                .setSmallIcon(R.mipmap.icon34)
                .setContentTitle("Try "+" "+s+" "+"Register Now")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You can also use"+" "+s+" "+"in Digital Register easily.\n"+"Open app for more details"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());

    }
}
