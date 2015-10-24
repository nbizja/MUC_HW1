package nb7232.muc_hw1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;


public class ConnectionMapFragment extends Fragment {

    protected GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.connection_map_fragment, container, false);
        if (map == null) {
            map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.connection_map))
                    .getMap();
        }

        return view;
    }
}
