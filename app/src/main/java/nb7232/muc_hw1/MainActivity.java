package nb7232.muc_hw1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Log.e("mainActivity", prefs.getString("email", "nemanista"));
        if (prefs.contains("email")) {
            Log.e("Fragments", "juhej");
            // During initial setup, plug in the details fragment.
            /*SummaryFragment summaryFragment = new SummaryFragment();
            summaryFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, summaryFragment).commit();
            */
            SettingsFragment settingsFragment = new SettingsFragment();
            settingsFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, settingsFragment).commit();
        } else {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }
    }

    public void dispatchTakePictureIntent() {
        Log.e("Camera", "dispatch!");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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
