package nb7232.muc_hw1.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import nb7232.muc_hw1.database.LocationDbHelper;

/**
 * Receives location data and label from LocationBroadcastReceiver and saves it to DB.
 * On every 100 samples MachineLearning class is called to calculate home/work locations.
 */
public class LocationService extends IntentService {

    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.e("LocationService", "starting");
        LocationDbHelper locationDbHelper = new LocationDbHelper(getApplicationContext());
        locationDbHelper.open();
        ContentValues row = new ContentValues();
        row.put(LocationDbHelper.LATITUDE, intent.getDoubleExtra("latitude", 0));
        row.put(LocationDbHelper.LONGITUDE, intent.getDoubleExtra("longitude", 0));
        row.put(LocationDbHelper.LABEL, intent.getStringExtra("label"));
        row.put(LocationDbHelper.TIMESTAMP, intent.getStringExtra("timestamp"));
        long id = locationDbHelper.getDb().insert(LocationDbHelper.TABLE_NAME, null, row);
        //Log.e("LocationService", "id vrstice: " + id);
        SharedPreferences prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        ;
        if (id % 100 == 0) {
            Intent saveIntent = new Intent(getApplicationContext(), MachineLearning.class);
            getApplicationContext().startService(saveIntent);
        }
    }
}
