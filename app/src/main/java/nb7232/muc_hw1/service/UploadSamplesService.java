package nb7232.muc_hw1.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nb7232.muc_hw1.database.LocationDbHelper;

/**
 * Created by nejc on 22.11.2015.
 */
public class UploadSamplesService extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final String BASEURL = "http://spletna-prijava.si/muc/lumen/";

    public UploadSamplesService() {
        super("UploadSamplesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocationDbHelper locationDbHelper = new LocationDbHelper(getApplicationContext());
        locationDbHelper.open();
        OkHttpClient client = new OkHttpClient();

        Cursor check = locationDbHelper.getDb().rawQuery("SELECT MAX(timestamp) as timestamp FROM location WHERE uploaded = 1", null);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -6);
        DateFormat.getTimeInstance().format(new
                Date());
        SimpleDateFormat tsformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(check.moveToFirst()) {
            try {
                if (tsformat.parse(check.getString(check.getColumnIndex("timestamp"))).getTime() < cal.getTimeInMillis()) {
                    check.close();
                    Cursor c = locationDbHelper.getDb().rawQuery("SELECT * FROM wifi WHERE uploaded = 0", null);
                    JSONArray wifiSamples = new JSONArray();
                    while(c.moveToNext()) {
                        JSONObject wifiSample = new JSONObject();
                        wifiSample.put("id", c.getInt(c.getColumnIndex("id")));
                        wifiSample.put("trigger_id", c.getInt(c.getColumnIndex("trigger_id")));
                        wifiSample.put("ssid", c.getString(c.getColumnIndex("ssid")));
                        wifiSample.put("bssid", c.getString(c.getColumnIndex("bssid")));
                        wifiSample.put("rssi", c.getInt(c.getColumnIndex("rssi")));
                        wifiSample.put("timestamp", c.getString(c.getColumnIndex("timestamp")));

                        wifiSamples.put(wifiSample);
                    }

                    RequestBody body = RequestBody.create(JSON, wifiSamples.toString());
                    Request request = new Request.Builder()
                            .url(BASEURL+"addWifiSamples")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        if(c.moveToFirst()) {
                            int firstId = c.getInt(c.getColumnIndex("id"));
                            c.moveToLast();
                            int lastId = c.getInt(c.getColumnIndex("id"));
                            String strSQL = "UPDATE wifi SET uploaded = 1 WHERE id >= ;"+firstId+" AND id <= "+lastId;
                            locationDbHelper.getDb().execSQL(strSQL);
                        }
                    }
                    c.close();

                    Cursor c2 = locationDbHelper.getDb().rawQuery("SELECT * FROM location WHERE uploaded = 1", null);
                    JSONArray locationSamples = new JSONArray();

                    while(c2.moveToNext()) {
                        JSONObject locationSample = new JSONObject();
                        locationSample.put("id", c2.getInt(c2.getColumnIndex("id")));
                        locationSample.put("trigger_id", c2.getInt(c2.getColumnIndex("trigger_id")));
                        locationSample.put("label", c2.getString(c2.getColumnIndex("label")));
                        locationSample.put("latitude", c2.getDouble(c2.getColumnIndex("latitude")));
                        locationSample.put("longitude", c2.getDouble(c2.getColumnIndex("longitude")));
                        locationSample.put("timestamp", c2.getString(c2.getColumnIndex("timestamp")));

                        locationSamples.put(locationSample);
                    }
                    RequestBody body2 = RequestBody.create(JSON, locationSamples.toString());
                    Request request2 = new Request.Builder()
                            .url(BASEURL+"addLocationSamples")
                            .post(body2)
                            .build();
                    Response locResponse = client.newCall(request2).execute();
                    if (locResponse.code() == 200) {
                        if(c2.moveToFirst()) {
                            int firstId = c2.getInt(c2.getColumnIndex("id"));
                            c2.moveToLast();
                            int lastId = c2.getInt(c2.getColumnIndex("id"));
                            String strSQL = "UPDATE location SET uploaded = 1 WHERE id >= ;"+firstId+" AND id <= "+lastId;
                            locationDbHelper.getDb().execSQL(strSQL);
                        }
                    }
                    c2.close();
                }
            } catch (Exception e) {
                Log.e("UploadSampleService", e.getMessage());
            }
        }
    }
}
