package com.powerfour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

public class MyBReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    private EventDataSource datasource;
    private ListView list;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("com.powerfour.RECEIVE_MESSAGE_FROM_GCM")) {
            String messageString = intent.getStringExtra("msg");
            Log.d(TAG,messageString + "I have to insert");
            datasource = new EventDataSource(context);
            datasource.open();
            if(!messageString.contains("inside")) {
                datasource.createPW4Event(messageString);
            }
        }
    }
}
