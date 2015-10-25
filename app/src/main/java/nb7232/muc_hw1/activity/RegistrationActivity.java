package nb7232.muc_hw1.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import nb7232.muc_hw1.R;
import nb7232.muc_hw1.model.User;
import nb7232.muc_hw1.model.UserHandler;

public class RegistrationActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        addListenerOnRegButton();
        this.user = new User();

    }

    public void addListenerOnRegButton() {
        findViewById(R.id.registration_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUser();
            }

        });
    }

    public void setUser() {
        EditText occupation = (EditText) findViewById(R.id.registration_occupation_editable);
        if (!user.setOccupation(occupation.getText().toString())) {
            occupation.setError(getResources().getString(R.string.registration_error_occupation));
        }

        EditText age = (EditText) findViewById(R.id.registration_age_editable);
        if (!user.setAge(Integer.parseInt(age.getText().toString()))) {
            age.setError(getResources().getString(R.string.registration_error_age));
        }

        EditText firstName = (EditText) findViewById(R.id.registration_first_name_editable);
        if (!user.setFirstName(firstName.getText().toString())) {
            firstName.setError(getResources().getString(R.string.registration_error_first_name));
        }

        EditText lastName = (EditText) findViewById(R.id.registration_last_name_editable);
        if (!user.setLastName(lastName.getText().toString())) {
            lastName.setError(getResources().getString(R.string.registration_error_last_name));
        }

        EditText email = (EditText) findViewById(R.id.registration_email_editable);
        if (!user.setEmail(email.getText().toString())) {
            email.setError(getResources().getString(R.string.registration_error_invalid_email));
        }

        RadioGroup radioGr = (RadioGroup) findViewById(R.id.registration_sex);
        RadioButton selectedSex = (RadioButton) findViewById(radioGr.getCheckedRadioButtonId());
        if (selectedSex.getId() == R.id.registration_male_radio) {
            user.setSex(User.Sex.MALE);
        } else {
            user.setSex(User.Sex.FEMALE);
        }

        EditText password = (EditText) findViewById(R.id.registration_password_editable);
        if (!user.setRawPassword(password.getText().toString())) {
            password.setError(getResources().getString(R.string.registration_error_password_match));
        }
        EditText password2 = (EditText) findViewById(R.id.registration_password_editable2);
        if (!password.getText().toString().equals(password2.getText().toString())) {
            password2.setError(getResources().getString(R.string.registration_error_password_match));
        }

        UserHandler userHandlerService = new UserHandler(getSharedPreferences("preferences", MODE_PRIVATE));
        boolean success = userHandlerService.register(user);
        if (success) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.registration_error_toast, Toast.LENGTH_LONG).show();
        }
    }


}
