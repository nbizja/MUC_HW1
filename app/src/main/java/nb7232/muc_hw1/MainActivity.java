package nb7232.muc_hw1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Fragment mapFragment = new MapFragment();
    private Fragment summaryFragment = new SummaryFragment();
    private Fragment settingsFragment = new SettingsFragment();
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        Log.e("mainActivity", prefs.getString("email", "nemanista"));
        if (prefs.contains("email")) {
            Log.e("Fragments", "juhej");
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(new FragmentSwitcher(getSupportFragmentManager(),
                    MainActivity.this));

            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);

        } else {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }
    }

    public TabLayout.OnTabSelectedListener tabListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentFragment = mapFragment;
                        break;
                    case 1:
                        currentFragment = summaryFragment;
                        break;
                    case 2:
                        currentFragment = settingsFragment;
                        break;
                    default:
                        currentFragment = summaryFragment;
                        break;
                }
                currentFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(
                        android.R.id.content, currentFragment).commit();

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }


    public void dispatchTakePictureIntent() {
        Log.e("Camera", "dispatch!");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void changeSettings() {
        Log.e("ChangeSettings", "Start");
        User user = new User();

        EditText firstName = (EditText) findViewById(R.id.registration_first_name_editable);
        if (user.setFirstName(firstName.getText().toString())) {
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

        EditText samplingInterval = (EditText) findViewById(R.id.settings_sampling_interval);
        if (!user.setSamplingInterval(Integer.parseInt(samplingInterval.getText().toString()))) {
            samplingInterval.setError(getResources().getString(R.string.settings_error_sampling_interval));
        }

        Registration registrationService = new Registration(getSharedPreferences("preferences", MODE_PRIVATE));
        registrationService.changeSettings(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Camera", "returned");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.e("Camera", "its All good");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView settingsView = (ImageView) findViewById(R.id.settings_image);
            settingsView.setBackgroundResource(0);
            settingsView.setImageBitmap(imageBitmap);
        }
    }
}
