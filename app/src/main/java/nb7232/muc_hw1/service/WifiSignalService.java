package nb7232.muc_hw1.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

import nb7232.muc_hw1.database.LocationDbHelper;
import nb7232.muc_hw1.model.WifiSample;

public class WifiSignalService extends IntentService {

    private WifiManager wifiManager;
    private LocationDbHelper locationDbHelper;

    public WifiSignalService() {
        super("WifiSignalService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("WifiSignalService", "Sampling like crazy");

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        locationDbHelper = new LocationDbHelper(this);
        locationDbHelper.open();

        if (isWifiEnabled()) {
            WifiSample wifiSample = sampleWifi();
            wifiSample.setTriggerId(intent.getIntExtra("trigger_id", 0));
            saveSample(wifiSample);
        }
    }

    private long saveSample(WifiSample wifiSample) {
        ContentValues row = new ContentValues();
        row.put("rssi", wifiSample.getRssi());
        row.put("bssid", wifiSample.getBssid());
        row.put("ssid", wifiSample.getSsid());
        row.put("trigger_id", wifiSample.getTriggerId());
        row.put("timestamp", wifiSample.getTimestamp());
        long id = locationDbHelper.getDb().insert("wifi", null, row);
        Log.e("SampledWifi", "id: " + id + "; trigger_id: " + wifiSample.getTriggerId());

        return id;
    }

    private WifiSample sampleWifi() {
        WifiSample wifiSample = new WifiSample();
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        wifiSample.setSsid(wifiInfo.getSSID());
        wifiSample.setBssid(wifiInfo.getBSSID());
        wifiSample.setRssi(wifiInfo.getRssi());
        wifiSample.setTimestamp(DateFormat.getTimeInstance().format(new
                Date()));
        return wifiSample;

    }

    private boolean isWifiEnabled() {
        int wifiState = wifiManager.getWifiState();
        return wifiState == wifiManager.WIFI_STATE_ENABLED;
    }

    private boolean isStrongConnection() {
        int info = wifiManager.getConnectionInfo().getRssi();
        return info > -75;
    }
}
