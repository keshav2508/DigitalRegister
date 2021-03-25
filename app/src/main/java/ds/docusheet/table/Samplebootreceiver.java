package ds.docusheet.table;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Samplebootreceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
           Alaramhelper.setalarm(context);
        }

    }

}
