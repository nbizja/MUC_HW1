package nb7232.muc_hw1;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.UUID;


public class Registration {

    private SharedPreferences prefs;

    public Registration(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public boolean register(User user) {
        Log.e("REGISTER CALLED", "RGSTR");
        if (user.isValid()) {
            Log.e("isValid", "true");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("uuid", UUID.randomUUID().toString());
            editor.putString("first_name", user.getFirstName());
            editor.putString("last_name", user.getLastName());
            editor.putString("email", user.getEmail());
            editor.putString("password", user.getPassword());
            editor.putString("occupation", user.getOccupation());
            editor.putInt("age", user.getAge());
            editor.putString("sex", user.getSex().toString());
            editor.putString("device", Build.BRAND + " " + Build.MODEL);
            editor.putLong("timestamp", Calendar.getInstance().getTimeInMillis());
            editor.commit();
            Log.e("intent", "plz work");
            return true;
        }
        return false;
    }
}
