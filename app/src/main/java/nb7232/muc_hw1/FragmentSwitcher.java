package nb7232.muc_hw1;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;


public class FragmentSwitcher extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private Context context;

    private MapFragment mapFragment = new MapFragment();
    private SummaryFragment summaryFragment = new SummaryFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private Fragment currentFragment;
    private String tabTitle;

    public FragmentSwitcher(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                currentFragment = mapFragment;
                tabTitle = "Map";
                break;
            case 1:
                currentFragment = summaryFragment;
                tabTitle = "Summary";
                break;
            case 2:
                currentFragment = settingsFragment;
                tabTitle = "Settings";
                break;
            default:
                currentFragment = summaryFragment;
                tabTitle = "Summary";
                break;
        }
        return currentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return "krneki";
    }
}
