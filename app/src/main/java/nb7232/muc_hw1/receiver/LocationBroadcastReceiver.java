package nb7232.muc_hw1.receiver;

import android.content.BroadcastReceiver;
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

import nb7232.muc_hw1.service.LocationService;

public class LocationBroadcastReceiver extends BroadcastReceiver implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "LocationReceiver";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    protected Context context;
    private String label;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", "Intent: " + intent.getAction());
        if (SamplingManager.checkAlarm(context, intent)) {
            Log.e("onReceive", "alarm ok! " );
            this.context = context;
            label = intent.getStringExtra("label");
            buildGoogleApiClient(context.getApplicationContext());
            mGoogleApiClient.connect();
        }
    }

    private void sampleLocation(String label) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            Intent saveIntent = new Intent(context, LocationService.class);
            saveIntent.putExtra("latitude", mCurrentLocation.getLatitude());
            saveIntent.putExtra("longitude", mCurrentLocation.getLongitude());
            saveIntent.putExtra("label", label);
            saveIntent.putExtra("timestamp", DateFormat.getTimeInstance().format(new
                    Date(mCurrentLocation.getTime())));
            context.startService(saveIntent);
        }
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
        Log.i(TAG, "Connected to GoogleApiClient");
        sampleLocation(label);
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
