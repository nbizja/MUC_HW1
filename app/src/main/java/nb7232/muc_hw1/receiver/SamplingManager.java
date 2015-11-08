package nb7232.muc_hw1.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class SamplingManager extends BroadcastReceiver {

    public static final String NIGHT_SAMPLING = "home";
    public static final int NIGHT_SAMPLING_START_HOUR = 1;
    public static final int NIGHT_SAMPLING_END_HOUR = 7;

    public static final String DAY_SAMPLING = "work";
    public static final int DAY_SAMPLING_START_HOUR = 9;
    public static final int DAY_SAMPLING_END_HOUR = 16;

    public static final int SAMPLING_MINUTE_INTERVAL = 10;
    /**
     * On how many new samples should locations be recalculated.
     */
    public static final int MACHINE_LEARNING_INTERVAL = 100;

    /**
     * Night and day alarms are set
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        startSampling(context, DAY_SAMPLING, DAY_SAMPLING_START_HOUR, 1);
        startSampling(context, NIGHT_SAMPLING, NIGHT_SAMPLING_START_HOUR, 1);
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
        if (intent.getAction().equals(DAY_SAMPLING)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) > DAY_SAMPLING_END_HOUR) {
                cancelSampling(context, DAY_SAMPLING);
                startSampling(context, NIGHT_SAMPLING, NIGHT_SAMPLING_START_HOUR, SAMPLING_MINUTE_INTERVAL);
                return false;
            } else if (calendar.get(Calendar.HOUR_OF_DAY) < DAY_SAMPLING_START_HOUR) {
                cancelSampling(context, DAY_SAMPLING);
                return false;
            }
        }
        if (intent.getAction().equals(NIGHT_SAMPLING)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) > NIGHT_SAMPLING_END_HOUR) {
                cancelSampling(context, NIGHT_SAMPLING);
                return false;

            } else if (calendar.get(Calendar.HOUR_OF_DAY) < NIGHT_SAMPLING_START_HOUR) {
                cancelSampling(context, DAY_SAMPLING);
                startSampling(context, DAY_SAMPLING, DAY_SAMPLING_START_HOUR, SAMPLING_MINUTE_INTERVAL);
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
     * @param label          work/home
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
        calendar.set(Calendar.MINUTE, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 6 * minuteInterval, samplingPi);
    }
}
