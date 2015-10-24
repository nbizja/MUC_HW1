package nb7232.muc_hw1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;


public class ConnectionMapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.connection_map_fragment, container, false);
        GoogleMap map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.connection_map))
                .getMap();

        return view;
    }

    /*
    @Override
    public void getMapAsync(OnMapReadyCallback callback) {
        super.getMapAsync(callback);
    } */
}
