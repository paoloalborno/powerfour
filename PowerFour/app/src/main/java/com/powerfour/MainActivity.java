package com.powerfour;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.List;

//to try this app, follow the instructions in to HowToTest.txt

public class MainActivity extends AppCompatActivity {

    //PAUL declarations not used:
    //private ProgressBar mRegistrationProgressBar;
    //private TextView mInformationTextView;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EventDataSource datasource;
    private ArrayList<String> listOfPW4Events;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfPW4Events = new ArrayList<String>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //PAUL: questa funzione registra lo smartphone chiamando il servizio Google, se disponibile
        RegisterID();

        datasource = new EventDataSource(this);
        datasource.open();

        List<PW4Event> values = datasource.getAllComments();
        for (PW4Event object : values) {
            //PAUL: uncheck se vogliamo pulire il DB dell'applicazine
            //datasource.deletePW4Event(object);
            listOfPW4Events.add(0, object.getComment());
        }

        CustomList adapter = new CustomList(MainActivity.this, listOfPW4Events);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        //PAUL: Go to pw4 button
        findViewById(R.id.pw4_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pw4.net"));
                startActivity(browserIntent);
            }
        });

        //PAUL: Qui possiamo utilizzare i dati di ogni evento PW4
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            //PAUL: Quando viene tappato un evento
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                TableLayout tl = (TableLayout) view;

                TableRow tr = (TableRow) tl.getChildAt(0);
                TextView tw = (TextView) tr.getChildAt(1);
                Switch iAmIn = (Switch) tr.getChildAt(2);       //il bottone Switch che cambia

                //PAUL: Cambio lo stato dello switch
                if (iAmIn.isChecked()) {
                    iAmIn.setChecked(false);
                } else {
                    iAmIn.setChecked(true);
                }

                String date = tw.getText().toString();  //se la data fosse scritta nel messaggio evento PW4
                CharSequence text = "Coming soon!";

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        //TODO: Login section
        /*
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.putExtra(EXTRA_MESSAGE, message);
            startActivityForResult(loginIntent, 1);
        */

        //TODO: Check if the token sent to the GCM server is valid
         /*
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true);
            if (sentToken) {
                mInformationTextView.setText(getString(R.string.gcm_send_message));
            } else {
                mInformationTextView.setText(getString(R.string.token_error_message));
            }
            }
        };
        */

        //TODO: other stuff
        /*
            mInformationTextView = (TextView) findViewById(R.id.informationTextView);
            WebView myWebView = (WebView) findViewById(R.id.webView);
            myWebView.loadUrl("http://www.pw4.net");
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    protected void RegisterID() {
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    //TODO: Non usata ancora, potrebbe essere utile
    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }
            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }
        }

        return views;
    }

    //TODO: Ricezione intent dalla activity di login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                if (checkPlayServices()) {
                    Intent intent = new Intent(this, RegistrationIntentService.class);
                    startService(intent);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //No Result
            }
        }
    }








}