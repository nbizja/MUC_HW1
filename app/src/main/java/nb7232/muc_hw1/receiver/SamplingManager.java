package nb7232.muc_hw1.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

public class SamplingManager extends BroadcastReceiver{

    public static final String REGULAR_SAMPLING = "regular_sampling";

    public static final String NIGHT_SAMPLING = "home";
    public static final int NIGHT_SAMPLING_START_HOUR = 1;
    public static final int NIGHT_SAMPLING_END_HOUR = 7;

    public static final String DAY_SAMPLING = "work";
    public static final int DAY_SAMPLING_START_HOUR = 9;
    public static final int DAY_SAMPLING_END_HOUR = 16;

    /**
     * On how many new samples should locations be recalculated.
     */
    public static final int MACHINE_LEARNING_INTERVAL = 100;

    private Context mContext;

    /**
     * Night and day alarms are set
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        int sampling_minute_interval = getSamplingInterval(context);
        Log.e("SamplingManager", "sampling_minute_interval: "+sampling_minute_interval);
        mock();
        if (hasLearned(context) || true) {
            startSampling(context, REGULAR_SAMPLING, 0, sampling_minute_interval);
        } else {
            startSampling(context, DAY_SAMPLING, DAY_SAMPLING_START_HOUR, sampling_minute_interval);
            startSampling(context, NIGHT_SAMPLING, NIGHT_SAMPLING_START_HOUR, sampling_minute_interval);
        }

    }

    public static boolean hasLearned(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String locations = prefs.getString("locations", "");
        return locations != "";
    }

    public static int getSamplingInterval(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        return prefs.getInt("sampling_interval", 1);
    }

    /**
     * Checks if alarm is designated time window.
     * Alarm is canceled if not and next alarm (day/night) is scheduled in this case.
     *
     * @param context
     * @param intent
     * @return boolean true if alarm is in designated time window.
     */
    public static boolean checkAlarm(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int sampling_minute_interval = getSamplingInterval(context);

        if (hasLearned(context) ||true) {

            //startSampling(context, REGULAR_SAMPLING, 0, sampling_minute_interval);
            //cancelSampling(context, DAY_SAMPLING);
            //cancelSampling(context, NIGHT_SAMPLING);
            return true;
        } else if (intent.getAction().equals(DAY_SAMPLING)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) > DAY_SAMPLING_END_HOUR) {
                cancelSampling(context, DAY_SAMPLING);
                startSampling(context, NIGHT_SAMPLING, NIGHT_SAMPLING_START_HOUR, sampling_minute_interval);
                return false;
            } else if (calendar.get(Calendar.HOUR_OF_DAY) < DAY_SAMPLING_START_HOUR) {
                cancelSampling(context, DAY_SAMPLING);
                return false;
            }
        } else if (intent.getAction().equals(NIGHT_SAMPLING)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) > NIGHT_SAMPLING_END_HOUR) {
                cancelSampling(context, NIGHT_SAMPLING);
                return false;

            } else if (calendar.get(Calendar.HOUR_OF_DAY) < NIGHT_SAMPLING_START_HOUR) {
                cancelSampling(context, DAY_SAMPLING);
                startSampling(context, DAY_SAMPLING, DAY_SAMPLING_START_HOUR, sampling_minute_interval);
                return false;
            }
        }
        return true;
    }

    private static void cancelSampling(Context context, String label) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent daySamplingPi = PendingIntent.getBroadcast(context, 1, new Intent(label), PendingIntent.FLAG_NO_CREATE);
        if (daySamplingPi != null) {
            am.cancel(daySamplingPi);
        }
    }

    /**
     * Starts repeating alarm
     *
     * @param context
     * @param label          work/home/regular_sampling
     * @param dayHour        Start hour of sampling
     * @param minuteInterval Minute interval
     */
    private static void startSampling(Context context, String label, int dayHour, int minuteInterval) {
        Log.e("SamplingManager", "startSampling: " + label);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent sampling = new Intent(label);
        sampling.putExtra("label", label);
        PendingIntent samplingPi = PendingIntent.getBroadcast(context, 1, sampling, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, dayHour);
        calendar.set(Calendar.MINUTE, 10);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * minuteInterval, samplingPi);
    }

    private void mock() {
        JSONArray locationsArray = new JSONArray();
        //Log.e("updateLocations", "Starting update");
            try {
                JSONObject locationsJson = new JSONObject();
                locationsJson.put("label", "home");
                locationsJson.put("latitude", 45.789502);
                locationsJson.put("longitude", 13.995830);
                locationsArray.put(locationsJson);

                JSONObject locationsJsonWork = new JSONObject();
                locationsJsonWork.put("label", "work");
                locationsJsonWork.put("latitude", 46.0495562);
                locationsJsonWork.put("longitude", 14.4598176);
                locationsArray.put(locationsJson);
            } catch (JSONException je) {
                Log.e("updateLocations", je.getMessage());
            }
            //Log.e("updateLocations", "updateCentroid()");
            //updateCentroid(location.getKey(), location.getValue()[0], location.getValue()[1]);
        SharedPreferences prefs = mContext.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //Log.e("updateLocations", locationsArray.toString());
        editor.putString("locations", locationsArray.toString());
        editor.commit();
    }
}
