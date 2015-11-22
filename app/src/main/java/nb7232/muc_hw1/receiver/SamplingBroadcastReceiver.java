package nb7232.muc_hw1.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import nb7232.muc_hw1.database.LocationDbHelper;
import nb7232.muc_hw1.service.LocationService;
import nb7232.muc_hw1.service.WifiSignalService;


/**
 * Class receives alarm signal configured in SamplingManager
 */
public class SamplingBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", "alarm received!");

        if (SamplingManager.checkAlarm(context, intent)) {
            Log.e("onReceive", "alarm ok! ");

            LocationDbHelper locationDbHelper = new LocationDbHelper(context);
            locationDbHelper.open();
            Cursor c = locationDbHelper.getDb().rawQuery("SELECT MAX(trigger_id) as trigger_id FROM location", null);
            c.moveToFirst();
            int trigger_id = c.getInt(c.getColumnIndex("trigger_id")) + 1;

            Intent locationSamplingIntent = new Intent(context, LocationService.class);
            locationSamplingIntent.putExtra("label", intent.getStringExtra("label"));
            locationSamplingIntent.putExtra("trigger_id", trigger_id);

            Intent wifiSamplingIntent = new Intent(context, WifiSignalService.class);
            wifiSamplingIntent.putExtra("label", intent.getStringExtra("label"));
            wifiSamplingIntent.putExtra("trigger_id", trigger_id);


            context.startService(locationSamplingIntent);
            context.startService(wifiSamplingIntent);
        }
    }
}
