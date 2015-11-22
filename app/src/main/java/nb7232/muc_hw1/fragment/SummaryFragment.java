package nb7232.muc_hw1.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nb7232.muc_hw1.R;
import nb7232.muc_hw1.model.Summary;
import nb7232.muc_hw1.task.SummaryTask;


public class SummaryFragment extends Fragment implements SummaryTask.TaskListener{

    private View fragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.summary_fragment, container, false);
        refreshData();
        return fragment;
    }

    @Override
    public void onResume() {
        Log.e("SummaryFragment", "onResumen");
        super.onResume();
        refreshData();

    }

    public void refreshData() {
        new SummaryTask(fragment.getContext(), this).execute();
    }

    @Override
    public void beforeTaskStarts(String s) {

    }

    @Override
    public void onTaskCompleted(Summary summary) {
        Log.e("SummaryFragment", "onTaskCompleted");

        HashMap<String, Double> average = summary.getAverageRssi();
        Log.e("SummaryFragment", "average.length = " + average.size() + " " + Arrays.toString(average.keySet().toArray()));

        if(average.containsKey("home")) {
            TextView tv = (TextView) fragment.findViewById(R.id.rssi_home);
            tv.setText(average.get("home").toString());
            tv.setVisibility(View.VISIBLE);
            Log.e("SummaryFragment", "SHOULD BE VISIBLE = " + average.get("home").toString());

        }
        if (average.containsKey("work")) {
                TextView tv2 = (TextView) fragment.findViewById(R.id.rssi_work);
                tv2.setText(average.get("work").toString());
                tv2.setVisibility(View.VISIBLE);
        }


    }
}
