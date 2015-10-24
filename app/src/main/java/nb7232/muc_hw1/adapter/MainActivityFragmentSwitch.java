package nb7232.muc_hw1.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import nb7232.muc_hw1.R;
import nb7232.muc_hw1.fragment.ConnectionMapFragment;
import nb7232.muc_hw1.fragment.SettingsFragment;
import nb7232.muc_hw1.fragment.SummaryFragment;


public class MainActivityFragmentSwitch extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private Context context;

    private ConnectionMapFragment connectionMapFragment = new ConnectionMapFragment();
    private SummaryFragment summaryFragment = new SummaryFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private List<String> tabTitles;

    public MainActivityFragmentSwitch(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.tabTitles = new ArrayList<String>();
        this.tabTitles.add(0, context.getString(R.string.tab_map));
        this.tabTitles.add(1, context.getString(R.string.tab_summary));
        this.tabTitles.add(2, context.getString(R.string.tab_settings));
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return connectionMapFragment;
            case 1:
                return summaryFragment;
            case 2:
                return settingsFragment;
            default:
                return summaryFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitles.get(position);
    }
}
