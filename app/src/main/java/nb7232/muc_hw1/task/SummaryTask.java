package nb7232.muc_hw1.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.util.HashMap;

import nb7232.muc_hw1.database.LocationDbHelper;
import nb7232.muc_hw1.model.AccessPoint;
import nb7232.muc_hw1.model.Summary;

/**
 * Created by nejc on 22.11.2015.
 */
public class SummaryTask extends AsyncTask<Object, Void, Summary> {

    public TaskListener taskListener;
    private Context mContext;

    public interface TaskListener {
        void beforeTaskStarts(String s);
        void onTaskCompleted(Summary summary);
    }


    public SummaryTask(Context context, TaskListener taskListener) {
        this.taskListener = taskListener;
        mContext = context;
    }

    @Override
    protected Summary doInBackground(Object[] params) {
        Log.e("SummaryTask", "doInBackground");
        Summary summary = new Summary();
        LocationDbHelper locationDbHelper = new LocationDbHelper(mContext);
        locationDbHelper.open();

        //GET AVERAGE RSSI
        Cursor c = locationDbHelper.getDb().rawQuery("SELECT AVG(rssi) as avg_rssi,label FROM wifi INNER JOIN location on wifi.trigger_id = location.trigger_id GROUP BY label",null);
        while(c.moveToNext()) {
            Double avg = c.getDouble(c.getColumnIndex("avg_rssi"));
            String label = c.getString(c.getColumnIndex("label"));
            summary.addAverageRssi(label, avg);
        }

        //GET TOP 10 APs
        Cursor c2 = locationDbHelper.getDb().rawQuery("SELECT COUNT(wifi.id) AS 'count',AVG(rssi) as avg_rssi, ssid, bssid, label, AVG(latitude) as avg_latitude, AVG(longitude) as avg_longitude " +
                "FROM wifi INNER JOIN location on wifi.trigger_id = location.trigger_id GROUP BY bssid ORDER BY 'count' DESC",null);
        int count = 0;

        while(c2.moveToNext() && count < 10) {
            AccessPoint accessPoint = new AccessPoint();
            accessPoint.setSsid(c2.getString(c2.getColumnIndex("ssid")));
            accessPoint.setBssid(c2.getString(c2.getColumnIndex("bssid")));
            accessPoint.setLabel(c2.getString(c2.getColumnIndex("label")));
            accessPoint.setRssi(c2.getDouble(c2.getColumnIndex("avg_rssi")));
            accessPoint.setLatitude(c2.getDouble(c2.getColumnIndex("avg_latitude")));
            accessPoint.setLongitude(c2.getDouble(c2.getColumnIndex("avg_longitude")));

            summary.addAccessPoint(accessPoint);
            count++;
        }
        Log.e("SummaryTask", "return summary");

        return summary;

    }

    @Override
    protected void onPostExecute(Summary summary) {
        super.onPostExecute(summary);
        Log.e("SummaryTask", "onPostExecute");
        taskListener.onTaskCompleted(summary);
    }
}
