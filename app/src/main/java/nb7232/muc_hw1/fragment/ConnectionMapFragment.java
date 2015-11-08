package nb7232.muc_hw1.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;


public class ConnectionMapFragment extends SupportMapFragment {

    public ConnectionMapFragment() {
        super();
    }
    private GoogleMap googleMap;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        context = inflater.getContext();
        googleMap = getMap();
        //Log.e("MapFragment", "adding markers");
        addMarkers();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        syncMarkers();
    }

    private void syncMarkers() {
        googleMap.clear();
        addMarkers();
    }

    /**
     * Gets location data from shared preferences and displays it on map
     */
    private void addMarkers() {
        SharedPreferences prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        try{
            JSONArray defaultLocation = new JSONArray();
            defaultLocation.put(new JSONObject().put("latitude", 46.0762662).put("longitude",14.5093724).put("label", "home"));
            defaultLocation.put(new JSONObject().put("latitude", 46.049840).put("longitude",14.468701).put("label", "work"));
            JSONArray locations = new JSONArray(prefs.getString("locations",defaultLocation.toString()));
            for(int i = 0; i < locations.length(); i++) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(locations.getJSONObject(i).getDouble("latitude"), locations.getJSONObject(i).getDouble("longitude")))
                        .title(locations.getJSONObject(i).getString("label")));
            }

        } catch (JSONException je) {
            Log.e("ConnectionMapFragment", je.getMessage());
        }
    }
}
