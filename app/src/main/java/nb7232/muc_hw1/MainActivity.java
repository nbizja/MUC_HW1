package nb7232.muc_hw1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Log.e("mainActivity", prefs.getString("email", "nemanista"));
        if (prefs.contains("email")) {
            Log.e("Fragments", "juhej");
            // During initial setup, plug in the details fragment.
            SummaryFragment summaryFragment = new SummaryFragment();
            summaryFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, summaryFragment).commit();

        } else {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }
        //startActivity(intent);

        /*setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }
}
