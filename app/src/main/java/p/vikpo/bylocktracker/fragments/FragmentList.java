package p.vikpo.bylocktracker.fragments;

import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.helpers.TrackerAdapter;
import p.vikpo.bylocktracker.liveData.TrackerList;

public class FragmentList extends ListFragment
{
    private TrackerAdapter listAdapter;
    private SharedPreferences sharedPref;
    private TrackerList trackerList;
    private Geocoder geoCoder;


    public static FragmentList newInstance()
    {
        return new FragmentList();
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
            trackerList.updateAddresses(geoCoder, sharedPref);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        FloatingActionButton fab = v.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(v1 ->
        {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, FragmentAddBike.newInstance());
            fragmentTransaction.commit();
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        geoCoder = new Geocoder(getContext(), Locale.getDefault());

        getListView().setOnItemClickListener((parent, view1, position, id) ->
        {
            FragmentTransaction fragmentTransaction = FragmentList.this.getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, FragmentEditBike.newInstance((int) id));
            fragmentTransaction.commit();
        });
    }
}
