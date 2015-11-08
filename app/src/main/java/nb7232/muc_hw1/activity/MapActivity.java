package nb7232.muc_hw1.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import nb7232.muc_hw1.LocationAlarmReceiver;

public class MapActivity extends AppCompatActivity {


    // UI Widgets.
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;
    protected TextView mLastUpdateTimeTextView;
    protected TextView mLatitudeTextView;
    protected TextView mLongitudeTextView;

    private PendingIntent pendingIntent;

    public static final String NIGHT_SAMPLING = "NIGHT_SAMPLING";
    public static final int NIGHT_SAMPLING_START_HOUR = 1;
    public static final int NIGHT_SAMPLING_END_HOUR = 7;

    public static final String DAY_SAMPLING = "DAY_SAMPLING";
    public static final int DAY_SAMPLING_START_HOUR = 9;
    public static final int DAY_SAMPLING_END_HOUR = 16;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve a PendingIntent that will perform a broadcast
        //Intent alarmIntent = new Intent(this, LocationAlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        startAlarm(getApplicationContext());

        /*
        setContentView(R.layout.main_activity);

        // Locate the UI widgets.
        mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);
        mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);
        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);

        // Set labels.
        mLatitudeLabel = "Latitude";
        mLongitudeLabel = "Longitude";
        mLastUpdateTimeLabel = "Update time";

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
        */
    }

    public static boolean checkAlarm(Context context, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (intent.getAction().equals(DAY_SAMPLING)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) > DAY_SAMPLING_END_HOUR || calendar.get(Calendar.HOUR_OF_DAY) < DAY_SAMPLING_START_HOUR) {
                Log.e("checkAlarm", "intent action = cancel day");
                PendingIntent daySamplingPi = PendingIntent.getBroadcast(context, 1, new Intent(DAY_SAMPLING), PendingIntent.FLAG_NO_CREATE);
                if (daySamplingPi != null) {
                    Log.e("checkAlarm", "existing dayintent");
                    am.cancel(daySamplingPi);
                }
                return false;
            }

        }
        if (intent.getAction().equals(NIGHT_SAMPLING)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) > NIGHT_SAMPLING_END_HOUR || calendar.get(Calendar.HOUR_OF_DAY) < NIGHT_SAMPLING_START_HOUR) {
                Log.e("checkAlarm", "intent action = cancel night");
                PendingIntent nightSamplingPi = PendingIntent.getBroadcast(context, 1, new Intent(NIGHT_SAMPLING), PendingIntent.FLAG_NO_CREATE);
                if (nightSamplingPi != null) {
                    Log.e("checkAlarm", "existing nightintent");
                    am.cancel(nightSamplingPi);
                }
                return false;
            }
        }
        return true;
    }

    public void startAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Night sampling:
        Intent nightSampling = new Intent(NIGHT_SAMPLING);
        PendingIntent nightSamplingPi = PendingIntent.getBroadcast(context, 1, nightSampling, 0);

        // Set the alarm to start at 1:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 1, nightSamplingPi);


        // Day sampling:
        Intent daySampling = new Intent(NIGHT_SAMPLING);
        PendingIntent daySamplingPi = PendingIntent.getBroadcast(context, 1, daySampling, 0);

        // Set the alarm to start at 9:00 am.
        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.setTimeInMillis(System.currentTimeMillis());
        dayCalendar.set(Calendar.HOUR_OF_DAY, 1);
        dayCalendar.set(Calendar.MINUTE, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, dayCalendar.getTimeInMillis(),
                1000 * 60 * 1, daySamplingPi);

    }
}
