package p.vikpo.bylocktracker.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.helpers.TrackerAdapter;

public class list_fragment extends ListFragment
{
    private ArrayAdapter<Tracker> listAdapter;
    private ArrayList<Tracker> trackers = new ArrayList<>();
    private Geocoder geoCoder;

    public static list_fragment newInstance()
    {
        return new list_fragment();
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Tracker tracker = new Tracker(), tracker1 = new Tracker(55.391918, 10.406703, 100, "Marcus pa Cour", "#D81B60");
        trackers.add(tracker);
        trackers.add(tracker1);

        listAdapter = new TrackerAdapter(getContext(), trackers);

        getListView().setAdapter(listAdapter);

        geoCoder = new Geocoder(getContext(), Locale.getDefault());

        updateAdresses();
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
