package nb7232.muc_hw1.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nb7232.muc_hw1.R;
import nb7232.muc_hw1.model.Summary;
import nb7232.muc_hw1.task.SummaryTask;


public class SummaryFragment extends Fragment implements SummaryTask.TaskListener{

    private LayoutInflater layoutInflater;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.summary_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void beforeTaskStarts(String s) {

    }

    @Override
    public void onTaskCompleted(Summary summary) {

        FragmentManager fragmentManager = getFragmentManager();
        View fragment = fragmentManager.findFragmentById(R.id.summary).getView();

        HashMap<String, Double> average = summary.getAverageRssi();
        for(Map.Entry<String,Double> rssi : average.entrySet()) {
            TextView tv;
            if(rssi.getKey().equals("home")) {
                tv = (TextView) fragment.findViewById(R.id.rssi_home);
                tv.setText(rssi.getValue().toString());
                tv.setVisibility(View.VISIBLE);
            } else if (rssi.getKey().equals("work")) {
                tv = (TextView) fragment.findViewById(R.id.rssi_work);
                tv.setText(rssi.getValue().toString());
                tv.setVisibility(View.VISIBLE);
            }
        }

    }
}
