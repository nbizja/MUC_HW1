package nb7232.muc_hw1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class SummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ScrollView scroller = new ScrollView(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());

        TextView avgRssiHome = new TextView(getActivity());
        TextView avgRssiWork = new TextView(getActivity());
        TextView strognestAp = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        avgRssiHome.setPadding(padding, padding, padding, padding);
        avgRssiWork.setPadding(padding, padding, padding, padding);
        strognestAp.setPadding(padding, padding, padding, padding);

        //add views to layout
        layout.addView(avgRssiHome);
        layout.addView(avgRssiWork);
        layout.addView(strognestAp);

        //set texts
        avgRssiHome.setText(getResources().getText(R.string.main_summary_rssi_home_text));
        avgRssiWork.setText(getResources().getText(R.string.main_summary_rssi_work_text));
        strognestAp.setText(getResources().getText(R.string.main_summary_strongest_ap_text));

        scroller.addView(layout);
        
        return scroller;

    }
}
