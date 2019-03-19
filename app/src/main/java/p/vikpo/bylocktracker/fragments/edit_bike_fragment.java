package p.vikpo.bylocktracker.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.liveData.TrackerList;

public class edit_bike_fragment extends Fragment
{
    private Button saveButton, routeToButton, backButton;
    private TextView latlngView, addressView, editName, percentageView;
    private ImageView bikeIcon, batteryIcon;
    private int index;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
    }

    public static edit_bike_fragment newInstance(int getListNumber)
    {
        edit_bike_fragment frag = new edit_bike_fragment();
        Bundle args = new Bundle();
        args.putInt("listNumber", getListNumber);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_edit_bike, container, false);
        index = getArguments().getInt("listNumber");

        saveButton = v.findViewById(R.id.save_button);
        routeToButton = v.findViewById(R.id.route_to_button);
        backButton = v.findViewById(R.id.button_back);

        editName = v.findViewById(R.id.edit_name);
        latlngView = v.findViewById(R.id.latlng_view);
        addressView = v.findViewById(R.id.address_view);
        percentageView = v.findViewById(R.id.percentage_view);

        bikeIcon = v.findViewById(R.id.edit_icon);
        batteryIcon = v.findViewById(R.id.battery_view);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        TrackerList trackerList = ViewModelProviders.of(this).get(TrackerList.class);

        final Observer<ArrayList<Tracker>> trackerObserver = trackers ->
        {
            Tracker track = trackers.get(index);
            String longLat = Double.toString(track.getLatLng().latitude) + ", " + Double.toString(track.getLatLng().longitude),
            perc = Double.toString(track.getBatteryPer()) + "%";

            editName.setText(track.getBikeOwner());
            addressView.setText(track.getAddress());
            percentageView.setText(perc);
            latlngView.setText(longLat);
            bikeIcon.setColorFilter(Color.parseColor(track.getColour()));
            bikeIcon.setImageResource(track.getIconSource());
        };

        saveButton.setOnClickListener(v1 ->
        {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, list_fragment.newInstance());
            fragmentTransaction.commit();
        });

        backButton.setOnClickListener(v1 ->
        {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, list_fragment.newInstance());
            fragmentTransaction.commit();
        });

        trackerList.getTrackerList(sharedPref).observe(this, trackerObserver);

        return v;
    }
}
