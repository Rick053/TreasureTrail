package com.utsdev.treasuretrail;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.utsdev.treasuretrail.adapters.HintAdapter;
import com.utsdev.treasuretrail.fields.CurrentLocation;
import com.utsdev.treasuretrail.fields.Hint;
import com.utsdev.treasuretrail.fields.User;
import com.utsdev.treasuretrail.stuff.DividerItemDecoration;

import org.w3c.dom.UserDataHandler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import tech.cocoon.Constants.Types;
import tech.cocoon.Inbox.BeaconInbox;
import tech.cocoon.Inbox.Inbox;
import tech.cocoon.Message.Beacon;
import tech.cocoon.Constants.Types.ServiceType;
import tech.cocoon.Message.Field;
import tech.cocoon.Message.Format.BeaconFormat;
import tech.cocoon.Sensor.Location.LocationSensorData;

public class MainActivity extends AppCompatActivity {

    public Vibrator v;
    public RecyclerView rcv;
    public HintAdapter adapter;

    //Fields
    public Hint hintField;
    private User userField;
    private int id = 1;
    private CurrentLocation locationField;

    //Notification Types
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NEW_HINT})
    public @interface NotifictionTypes {}

    public static final int NEW_HINT = 1;

    public BeaconFormat bf;
    public BeaconFormat lf;

    //test stuff
    public int count = 2;

    public String myHint;

    public String[] hints = new String[] {
            "Test",
            "Test 2",
            "Hint",
            "Lorem ipsum"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        App.setActivity(this);
        App.startService(MainActivity.this);

//        TextView status = (TextView) App.getActivity().findViewById(R.id.status);
//        Status.assignView(status);


        setupBeaconFormat();

        //Initialize the adapter
        adapter = new HintAdapter(this);

        //Initialize the recyclerview which will display the hints
        rcv = (RecyclerView) findViewById(R.id.hint_list);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);
        rcv.setAdapter(adapter);
        rcv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        adapter.addHint(hints[2]);

        //Initialize the handler which checks the messages every x seconds
        final Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                BeaconInbox inbox = App.getBeaconInbox();
                List<Beacon> msgs = inbox.getList();


                Location location = new LocationSensorData().getLocation();

                lf.setFieldValue(locationField, createValueFromLocation(location));
                Beacon myBeacon = new Beacon(lf, ServiceType.SENSOR_GPS);

                App.getBeaconInbox().pushElement(myBeacon);
                Log.v("LOCATION LOG STUFF", location.toString());

                if(msgs.size() > 0) {
                    for(int j = 0; j < msgs.size(); j++) {
                        Beacon msg = msgs.get(j);

                        if(msg.serviceType == ServiceType.GENERIC_MESSAGE) {
                            //Hint is sent
                            String h = msg.getFieldValue(hintField);
                            String i = msg.getFieldValue(userField);
                            int user_id = Integer.parseInt(i.replace("|", ""));

                            adapter.addHint(h.replace("|", ""));

                            sendNotification(NEW_HINT);

                        } else if (msg.serviceType == ServiceType.SENSOR_GPS) {
                            //TODO Location is sent
                            String l = msg.getFieldValue(locationField);
                            byte[] lbytes = Base64.decode(l, Base64.DEFAULT);
                            String loc = new String(lbytes);

                            String[] locs = loc.split("x");
                            double lon = Double.parseDouble((location.getLongitude() + "").split(",")[0] + "," + locs[0]);
                            double lat = Double.parseDouble((location.getLatitude() + "").split(",")[1] + "," + locs[1]);

                            Location target = new Location(location);
                            target.setLongitude(lon);
                            target.setLatitude(lat);

                            float dist = location.distanceTo(target);

                            if(dist < 5) {
                                v.vibrate(500);

                                App.getBeaconInbox().pushElement(new Beacon(bf, ServiceType.GENERIC_MESSAGE));
                            }
                        }
                    }
                }

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private String createValueFromLocation(Location location) {
        String lon = location.getLongitude() + "";
        String lat = location.getLatitude() + "";

        String lonPart = lon.split(",")[1];
        String latPart = lat.split(",")[1];

        String test = lonPart + "x" + latPart;

        return Base64.encodeToString(test.getBytes(), Base64.DEFAULT);
    }

    private void setupBeaconFormat() {

        hintField = new Hint(23);
        userField = new User(1);
        locationField = new CurrentLocation(23);
        myHint = hints[2];


        bf = new BeaconFormat.Builder(Types.BeaconType.SSID)
                .addField(hintField)
                .addField(userField).build();
        bf.setFieldValue(hintField, myHint);
        bf.setFieldValue(userField, id + "");

        lf = new BeaconFormat.Builder(Types.BeaconType.SSID)
                .addField(locationField)
                .addField(userField).build();
    }

    private void sendNotification(int type) {
        if(type == NEW_HINT) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setVibrate(new long[]{100, 200, 100, 500})
                            .setContentTitle("Treasure Trails")
                            .setContentText("You received a new hint!");

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(NEW_HINT, mBuilder.build());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
