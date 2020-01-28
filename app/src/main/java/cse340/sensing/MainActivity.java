package cse340.sensing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Checkable;
import android.widget.ImageView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.LocationFence;

import cse340.sensing.fence.CSEFenceListener;
import cse340.sensing.fence.FSHFenceListener;
import cse340.sensing.fence.FenceBroadcastReceiver;
import cse340.sensing.fence.HUBFenceListener;
import cse340.sensing.fence.KNEFenceListener;
import cse340.sensing.fence.SAVFenceListener;
import cse340.sensing.snapshot.WeatherSnapshotListener;

public class MainActivity extends ContextActivity {
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;

    protected PrependableTextView mUpdate;  // the words that show up on thw screen

    protected Resources mResources;

    protected ImageView imageView;

    protected FenceBroadcastReceiver cseFenceListener;
    protected FenceBroadcastReceiver hubFenceListener;
    protected FenceBroadcastReceiver kneFenceListener;
    protected FenceBroadcastReceiver savFenceListener;
    protected FenceBroadcastReceiver fshFenceListener;



    @Override
    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mUpdate = findViewById(R.id.update);
        mResources = getResources();
        imageView = findViewById(R.id.imageView);

        // Setup the broadcast listeners needed for the fences.
        setupFenceListeners();

        findViewById(R.id.weather_snapshot_button).setOnClickListener(v -> {
            // TODO: Add a weather snapshot.
            // First, ensure fine location access. Then, setup snapshot using getWeather()
            if (ensureFineLocationAccess()) {
                setSnapshotListener(Awareness.
                                getSnapshotClient(this).getWeather(),
                        new WeatherSnapshotListener(mUpdate, mResources, imageView));
            }
        });

        findViewById(R.id.cse_checkbox).setOnClickListener(v -> {
            // TODO: Do something similar for location fence.
            if (((Checkable) v).isChecked() && ensureFineLocationAccess()) {
                cseFenceListener.register();
            } else {
                cseFenceListener.unregister();
            }
        });

        findViewById(R.id.hub_checkbox).setOnClickListener(v -> {
            // TODO: Do something similar for location fence.
            if (((Checkable) v).isChecked() && ensureFineLocationAccess()) {
                hubFenceListener.register();
            } else {
                hubFenceListener.unregister();
            }
        });

        findViewById(R.id.kne_checkbox).setOnClickListener(v -> {
            // TODO: Do something similar for location fence.
            if (((Checkable) v).isChecked() && ensureFineLocationAccess()) {
                kneFenceListener.register();
            } else {
                kneFenceListener.unregister();
            }
        });

        findViewById(R.id.sav_checkbox).setOnClickListener(v -> {
            // TODO: Do something similar for location fence.
            if (((Checkable) v).isChecked() && ensureFineLocationAccess()) {
                savFenceListener.register();
            } else {
                savFenceListener.unregister();
            }
        });

        findViewById(R.id.fsh_checkbox).setOnClickListener(v -> {
            // TODO: Do something similar for location fence.
            if (((Checkable) v).isChecked() && ensureFineLocationAccess()) {
                fshFenceListener.register();
            } else {
                fshFenceListener.unregister();
            }
        });
    }

    protected final boolean ensureFineLocationAccess() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            Log.i("CSE340", "Need fine location permission");
            return false;
        }

        return true;
    }

    @SuppressLint("MissingPermission")
    protected void setupFenceListeners() {

        /*
         * cse Building
         */
        cseFenceListener = new CSEFenceListener(LocationFence.entering
                (CSEFenceListener.CSE_LATITUDE, CSEFenceListener.CSE_LONGITUDE,
                        CSEFenceListener.CSE_RADIUS),
                LocationFence.in(CSEFenceListener.CSE_LATITUDE,
                        CSEFenceListener.CSE_LONGITUDE, CSEFenceListener.CSE_RADIUS,
                        CSEFenceListener.DWELL_TIME_MILLIS), LocationFence.exiting
                (CSEFenceListener.CSE_LATITUDE, CSEFenceListener.CSE_LONGITUDE,
                        CSEFenceListener.CSE_RADIUS), this, this, mUpdate, imageView);

        /*
         * hub
         */
        hubFenceListener = new HUBFenceListener(LocationFence.entering
                (HUBFenceListener.HUB_LATITUDE, HUBFenceListener.HUB_LONGITUDE,
                        HUBFenceListener.HUB_RADIUS),
                LocationFence.in(HUBFenceListener.HUB_LATITUDE,
                        HUBFenceListener.HUB_LONGITUDE, HUBFenceListener.HUB_RADIUS,
                        HUBFenceListener.DWELL_TIME_MILLIS), LocationFence.exiting
                (HUBFenceListener.HUB_LATITUDE, HUBFenceListener.HUB_LONGITUDE,
                        HUBFenceListener.HUB_RADIUS), this, this, mUpdate, imageView);

        /*
         * kane Hall
         */
        kneFenceListener = new KNEFenceListener(LocationFence.entering
                (KNEFenceListener.KNE_LATITUDE, KNEFenceListener.KNE_LONGITUDE,
                        KNEFenceListener.KNE_RADIUS),
                LocationFence.in(KNEFenceListener.KNE_LATITUDE,
                        KNEFenceListener.KNE_LONGITUDE, KNEFenceListener.KNE_RADIUS,
                        KNEFenceListener.DWELL_TIME_MILLIS), LocationFence.exiting
                (KNEFenceListener.KNE_LATITUDE, KNEFenceListener.KNE_LONGITUDE,
                        KNEFenceListener.KNE_RADIUS), this, this, mUpdate, imageView);

        /*
         * savery HALL
         */
        savFenceListener = new SAVFenceListener(LocationFence.entering
                (SAVFenceListener.SAV_LATITUDE, SAVFenceListener.SAV_LONGITUDE,
                        SAVFenceListener.SAV_RADIUS),
                LocationFence.in(SAVFenceListener.SAV_LATITUDE,
                        SAVFenceListener.SAV_LONGITUDE, SAVFenceListener.SAV_RADIUS,
                        SAVFenceListener.DWELL_TIME_MILLIS), LocationFence.exiting
                (SAVFenceListener.SAV_LATITUDE, SAVFenceListener.SAV_LONGITUDE,
                        SAVFenceListener.SAV_RADIUS), this, this, mUpdate, imageView);

        /*
         * Fishery & Science Building
         */
        fshFenceListener = new FSHFenceListener(LocationFence.entering
                (FSHFenceListener.FSH_LATITUDE, FSHFenceListener.FSH_LONGITUDE,
                        FSHFenceListener.FSH_RADIUS),
                LocationFence.in(FSHFenceListener.FSH_LATITUDE,
                        FSHFenceListener.FSH_LONGITUDE, FSHFenceListener.FSH_RADIUS,
                        FSHFenceListener.DWELL_TIME_MILLIS), LocationFence.exiting
                (FSHFenceListener.FSH_LATITUDE, FSHFenceListener.FSH_LONGITUDE,
                        FSHFenceListener.FSH_RADIUS), this, this, mUpdate, imageView);
    }
}