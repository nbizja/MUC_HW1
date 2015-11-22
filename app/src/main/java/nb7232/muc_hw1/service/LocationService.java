package nb7232.muc_hw1.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static final double LOCATION_RADIUS = 200;
    public static final String DEFAULT_LABEL = "other";

    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("LocationService", "Sampling like crazy");
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
            Log.e(TAG, "Connected to GoogleApiClient");
            ContentValues row = new ContentValues();
            row.put(LocationDbHelper.LATITUDE, mCurrentLocation.getLatitude());
            row.put(LocationDbHelper.LONGITUDE, mCurrentLocation.getLongitude());
            row.put(LocationDbHelper.TIMESTAMP, DateFormat.getTimeInstance().format(new
                    Date(mCurrentLocation.getTime())));
            row.put("trigger_id", intent.getIntExtra("trigger_id",0));

            if (intent.getStringExtra("label") == SamplingManager.REGULAR_SAMPLING) {
                row.put(LocationDbHelper.LABEL, classify(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
            } else {
                row.put(LocationDbHelper.LABEL, intent.getStringExtra("label"));
            }
            long id = locationDbHelper.getDb().insert(LocationDbHelper.TABLE_NAME, null, row);
            Log.e("SampledLocation", "id: " + id + "; trigger_id: " + intent.getIntExtra("trigger_id", 0) + " label: "+ classify(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));

            if (id % SamplingManager.MACHINE_LEARNING_INTERVAL == 0) {
                Intent saveIntent = new Intent(getApplicationContext(), MachineLearning.class);
                getApplicationContext().startService(saveIntent);
            }
        }
    }

    public String classify(double latitude, double longitude) {
        SharedPreferences prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String locations = prefs.getString("locations","");
        try{
            JSONArray locationsJson = new JSONArray(locations);
            // The computed distance is stored in results[0].
            //If results has length 2 or greater, the initial bearing is stored in results[1].
            //If results has length 3 or greater, the final bearing is stored in results[2].
            float[] results = new float[1];

            for(int i = 0; i < locations.length(); i++) {
                Location.distanceBetween(locationsJson.getJSONObject(i).getDouble("latitude"),
                        locationsJson.getJSONObject(i).getDouble("longitude"),
                        latitude, longitude, results);
                if (results[0] < LOCATION_RADIUS) {
                    return locationsJson.getJSONObject(i).getString("label");
                }
            }
        } catch (JSONException je) {
            Log.e("ConnectionMapFragment", je.getMessage());
        }

        return DEFAULT_LABEL;
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
