package p.vikpo.bylocktracker.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.liveData.TrackerList;

public class FragmentMap extends Fragment implements OnMapReadyCallback
{

    private boolean specLaunch = false;
    public static FragmentMap newInstance()
    {
        return new FragmentMap();
    }

    public static FragmentMap newInstance(int index)
    {
        FragmentMap frag = new FragmentMap();
        Bundle args = new Bundle();
        args.putInt("title", index);
        frag.setArguments(args);
        return frag;
    }

    private GoogleMap googleMap;
    private SharedPreferences sharedPref;
    private TrackerList trackerList;
    private boolean isLocated = false;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        MapView mapView = v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {

        googleMap = map;

        googleMap.setMinZoomPreference(6.0f);
        googleMap.setMaxZoomPreference(20.0f);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.371326, 10.427586), 12.0f));

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        trackerList  = ViewModelProviders.of(this).get(TrackerList.class);


        final Observer<ArrayList<Tracker>> trackerObserver = trackers ->
        {
            ArrayList<Tracker> mapTrackerList = trackerList.getTrackerList(sharedPref).getValue();
            if(mapTrackerList != null)
            {
                for(Tracker s : mapTrackerList)
                {
                    float[] hue = new float[3];
                    Color.colorToHSV(Color.parseColor(s.getColour()), hue);
                    googleMap.addMarker(new MarkerOptions().position(s.getLatLng()).title(s.getBikeOwner()).icon(BitmapDescriptorFactory.defaultMarker(hue[0])));
                }
                if(getArguments() != null)
                {
                    if(getArguments().containsKey("title") && !isLocated)
                    {
                        int targetIndex = getArguments().getInt("title", -1);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapTrackerList.get(targetIndex).getLatLng(), 18.0f));
                        isLocated = true;
                    }
                }

            }
        };

        trackerList.getTrackerList(sharedPref).observe(this, trackerObserver);
    }
}
