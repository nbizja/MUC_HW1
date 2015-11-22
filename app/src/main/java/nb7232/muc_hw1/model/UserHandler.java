package nb7232.muc_hw1.model;

import android.content.SharedPreferences;
import android.os.Build;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;


public class UserHandler {

    private SharedPreferences prefs;

    public UserHandler(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public boolean register(User user) {
        if (!user.isValid()) {
            return false;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("uuid", UUID.randomUUID().toString());
        editor.putString("first_name", user.getFirstName());
        editor.putString("last_name", user.getLastName());
        editor.putString("email", user.getEmail());
        editor.putString("password", md5(user.getRawPassword()));
        editor.putString("occupation", user.getOccupation());
        editor.putInt("age", user.getAge());
        editor.putString("sex", user.getSex().toString());
        editor.putString("device", Build.BRAND + " " + Build.MODEL);
        editor.putLong("timestamp", Calendar.getInstance().getTimeInMillis());
        editor.putInt("sampling_interval", 1);
        editor.commit();

        return true;
    }

    public boolean changeSettings(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        if (!user.isValid()) {
            return false;
        }
        editor.putString("first_name", user.getFirstName());
        editor.putString("last_name", user.getLastName());
        editor.putString("email", user.getEmail());
        editor.putInt("sampling_interval", user.getSamplingInterval());
        editor.commit();

        return true;
    }

    public User getUser() {
        User user = new User();
        prefs.getString("uuid", UUID.randomUUID().toString());
        user.setFirstName(prefs.getString("first_name", "First name"));
        user.setLastName(prefs.getString("last_name", "Last name"));
        user.setEmail(prefs.getString("email", "E-mail"));
        user.setHashedPassword(prefs.getString("password", "Password"));
        user.setOccupation(prefs.getString("occupation", "Occupation"));
        user.setAge(prefs.getInt("age", 20));
        user.setSex(User.Sex.valueOf(prefs.getString("sex", "MALE")));
        user.setSamplingInterval(prefs.getInt("sampling_interval", 15));

        return user;
    }

    /**
     * function md5 encryption for passwords
     *
     * @param password
     * @return passwordEncrypted
     * @link https://cmanios.wordpress.com/2012/03/19/android-md5-password-encryption/
     */
    public static final String md5(final String password) {
        try {

            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
