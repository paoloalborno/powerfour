package com.powerfour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class MyBReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBReceiver";
    public static final String RECEIVE_JSON = "com.powerfour.RECEIVE_MESSAGE_FROM_GCM";
    private EventDataSource datasource;
    private ListView list;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("com.powerfour.RECEIVE_MESSAGE_FROM_GCM")) {
           // Log.d(TAG, "connect");
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
