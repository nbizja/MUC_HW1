package nb7232.muc_hw1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.util.Calendar;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity{

    private String occupation;
    private Integer age;
    private String firstName;
    private String lastName;
    private String email;
    private boolean sex;
    private String password;

    private boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        addListenerOnRegButton();
        isValid = true;

    }

    public void addListenerOnRegButton() {
        findViewById(R.id.registration_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set();
            }

        });
    }

    public void set() {
        this.isValid = true;
        setOccupation(R.id.registration_occupation_editable);
        setAge(R.id.registration_age_editable);
        setFirstName(R.id.registration_first_name_editable);
        setLastName(R.id.registration_last_name_editable);
        setEmail(R.id.registration_email_editable);
        setSex(R.id.registration_sex);
        setPassword(R.id.registration_password_editable);
        checkRetypedPassword(R.id.registration_password_editable2);

        register();
    }

    public void register() {
        Log.e("REGISTER CALLED", "RGSTR");
        if (isValid) {
            Log.e("isValid", "true");

            SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("uuid",  UUID.randomUUID().toString());
            editor.putString("first_name", firstName);
            editor.putString("last_name", lastName);
            editor.putString("email", email);
            editor.putString("password", md5(password));
            editor.putString("occupation", occupation);
            editor.putInt("age", age);
            editor.putBoolean("sex", sex);
            editor.putString("device", Build.BRAND + " " + Build.MODEL);
            editor.putLong("timestamp", Calendar.getInstance().getTimeInMillis());
            editor.commit();
            Log.e("intent", "plz work");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void setOccupation(int id) {
        EditText et = (EditText) findViewById(id);
        if (et.getText().length() <1) {
            this.isValid = false;
            et.setError(getResources().getString(R.string.registration_error_occupation));
        }
        this.occupation = et.getText().toString();
    }

    public void setAge(int id) {
        EditText et = (EditText) findViewById(id);
        try {
            age = Integer.parseInt(et.getText().toString());
        } catch (Exception e) {
            this.isValid = false;
            et.setError(getResources().getString(R.string.registration_error_age));
        }
    }

    public void setFirstName(int id) {
        EditText et = (EditText) findViewById(id);
        if (et.getText().length() < 2) {
            this.isValid = false;
            et.setError(getResources().getString(R.string.registration_error_first_name));
        }
        this.firstName = et.getText().toString();
    }

    public void setLastName(int id) {
        EditText et = (EditText) findViewById(id);
        if (et.getText().length() < 2) {
            this.isValid = false;
            et.setError(getResources().getString(R.string.registration_error_last_name));
        }
        this.lastName = et.getText().toString();
    }

    public void setEmail(int id) {
        EditText et = (EditText) findViewById(id);
        if (et.getText().length() < 4) {
            this.isValid = false;
            et.setError(getResources().getString(R.string.registration_error_blank_email));
        } else if (!Validator.isValidEmail(et.getText())) {
            this.isValid = false;
            et.setError(getResources().getString(R.string.registration_error_invalid_email));
        }
        this.email = et.getText().toString();
    }

    public void setSex(int id) {
        RadioGroup radioGr = (RadioGroup) findViewById(id);
        RadioButton selectedSex = (RadioButton) findViewById(radioGr.getCheckedRadioButtonId());
        this.sex = selectedSex.getId() == R.id.registration_male_radio;
    }

    public void setPassword(int id) {
        EditText et = (EditText) findViewById(id);
        if (et.getText().length() < 8 || et.getText().length() > 16) {
            this.isValid = false;
            et.setError(getResources().getString(R.string.registration_error_password_length));
        }
        this.password = et.getText().toString();
    }

    public void checkRetypedPassword(int id) {
        EditText et = (EditText) findViewById(id);
        if (!et.getText().toString().equals(this.password)) {
            this.isValid = false;
            et.setError(getResources().getString(R.string.registration_error_password_match));
        }
    }

    /**
     * function md5 encryption for passwords
     *
     * @param password
     * @return passwordEncrypted
     * @link https://cmanios.wordpress.com/2012/03/19/android-md5-password-encryption/
     */
    private static final String md5(final String password) {
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
