package nb7232.muc_hw1.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nb7232.muc_hw1.R;


public class SummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.summary_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
