package nb7232.muc_hw1.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

import nb7232.muc_hw1.database.LocationDbHelper;
import nb7232.muc_hw1.receiver.SamplingManager;

/**
 * Receives location data and label from SamplingBroadcastReceiver and saves it to DB.
 * On every 100 samples MachineLearning class is called to calculate home/work locations.
 */
public class LocationService extends IntentService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "LocationReceiver";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    private LocationDbHelper locationDbHelper;
    private Intent intent;

    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.intent = intent;
        buildGoogleApiClient(this);
        mGoogleApiClient.connect();

        locationDbHelper = new LocationDbHelper(this);
        locationDbHelper.open();
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            Log.i(TAG, "Connected to GoogleApiClient");
            ContentValues row = new ContentValues();
            row.put(LocationDbHelper.LATITUDE, mCurrentLocation.getLatitude());
            row.put(LocationDbHelper.LONGITUDE, mCurrentLocation.getLongitude());
            row.put(LocationDbHelper.LABEL, intent.getStringExtra("label"));
            row.put(LocationDbHelper.TIMESTAMP, DateFormat.getTimeInstance().format(new
                    Date(mCurrentLocation.getTime())));
            long id = locationDbHelper.getDb().insert(LocationDbHelper.TABLE_NAME, null, row);
            Log.e("SampledLocation", "id: " + id + "; trigger_id: "+intent.getIntExtra("trigger_id",0));

            if (id % SamplingManager.MACHINE_LEARNING_INTERVAL == 0) {
                Intent saveIntent = new Intent(getApplicationContext(), MachineLearning.class);
                getApplicationContext().startService(saveIntent);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


}
