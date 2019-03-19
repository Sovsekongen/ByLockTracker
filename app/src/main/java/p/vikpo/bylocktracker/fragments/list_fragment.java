package p.vikpo.bylocktracker.fragments;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.helpers.TrackerAdapter;
import p.vikpo.bylocktracker.liveData.TrackerList;

public class list_fragment extends ListFragment
{

    private FloatingActionButton fab;
    private TrackerAdapter listAdapter;
    private ArrayList<Tracker> trackers = new ArrayList<>();
    private SharedPreferences sharedPref;
    private Geocoder geoCoder;
    private Tracker tracker = new Tracker(), tracker1 = new Tracker(55.391918, 10.406703, 100, "Marcus pa Cour", "#D81B60");
    private TrackerList trackerList;

    public static list_fragment newInstance()
    {
        return new list_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        trackerList  = ViewModelProviders.of(this).get(TrackerList.class);
        final Observer<ArrayList<Tracker>> trackerObserver = trackers ->
        {
            listAdapter = new TrackerAdapter(getContext(), trackers);
            getListView().setAdapter(listAdapter);
        };

        trackerList.getTrackerList(sharedPref).observe(this, trackerObserver);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        fab = v.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(v1 ->
        {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, map_fragment.newInstance());
            fragmentTransaction.commit();
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        geoCoder = new Geocoder(getContext(), Locale.getDefault());

        /*
        trackerList.addTracker(tracker, sharedPref);
        trackerList.addTracker(tracker1, sharedPref);*/
        //updateAdresses();
    }

    public void updateAdresses()
    {
        for(Tracker s : trackers)
        {
            double lat = s.getLatitude(), longi = s.getLongitude();
            ArrayList<Address> addresses = new ArrayList<>();

            try
            {
                addresses = (ArrayList) geoCoder.getFromLocation(lat, longi, 1);
            }
            catch(IOException ioe)
            {

            }

            String address = addresses.get(0).getAddressLine(0);
            s.setAddress(address);
        }
    }
}
