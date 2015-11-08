package nb7232.muc_hw1.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import nb7232.muc_hw1.database.LocationDbHelper;

public class LocationService extends IntentService {

    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("LocationService", "starting");
        LocationDbHelper locationDbHelper = new LocationDbHelper(getApplicationContext());
        locationDbHelper.open();
        ContentValues row = new ContentValues();
        row.put(LocationDbHelper.LATITUDE, intent.getDoubleExtra("latitude", 0));
        row.put(LocationDbHelper.LONGITUDE, intent.getDoubleExtra("longitude", 0));
        row.put(LocationDbHelper.LABEL, intent.getStringExtra("label"));
        row.put(LocationDbHelper.TIMESTAMP, intent.getStringExtra("timestamp"));
        long id = locationDbHelper.getDb().insert(LocationDbHelper.TABLE_NAME, null, row);
        Log.e("LocationService", "id vrstice: " + id);
        if (id % 10 == 0) {
            Intent saveIntent = new Intent(getApplicationContext(), MachineLearning.class);
            getApplicationContext().startService(saveIntent);
        }
    }
}
