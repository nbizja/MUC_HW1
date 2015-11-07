package nb7232.muc_hw1;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

import nb7232.muc_hw1.database.LocationDbHelper;

/**
 * Created by nejc on 6.11.2015.
 */
public class LocationService extends IntentService {

    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("LocationService", "starting");
        LocationDbHelper locationDbHelper = new LocationDbHelper(getApplicationContext());
        ContentValues row = new ContentValues();
        row.put(LocationDbHelper.LATITUDE, intent.getDoubleExtra("latitude", 0));
        row.put(LocationDbHelper.LONGITUDE, intent.getDoubleExtra("longitude", 0));
        row.put(LocationDbHelper.TIMESTAMP, intent.getStringExtra("timestamp"));
        long id = locationDbHelper.getWritableDatabase().insert(LocationDbHelper.TABLE_NAME, null, row);
        Log.e("LocationService", "id vrstice: " + id);
    }
}
