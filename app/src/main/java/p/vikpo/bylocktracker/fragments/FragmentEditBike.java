package p.vikpo.bylocktracker.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.helpers.WifiHandler;
import p.vikpo.bylocktracker.liveData.TrackerList;
import p.vikpo.bylocktracker.login.SessionHandler;

public class FragmentEditBike extends Fragment
{
    private Button saveButton, locateButton, routeTo;
    private TextView addressView, editName, percentageView, deviceId;
    private ImageView bikeIcon;
    private Tracker track;
    private int index;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
    }

    public static FragmentEditBike newInstance(int getListNumber)
    {
        FragmentEditBike frag = new FragmentEditBike();
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
        WifiHandler wifiHandler = new WifiHandler(getContext());

        saveButton = v.findViewById(R.id.save_button);
        locateButton = v.findViewById(R.id.btn_view_on_map);
        editName = v.findViewById(R.id.edit_name);
        addressView = v.findViewById(R.id.address_view);
        deviceId = v.findViewById(R.id.edit_bike_deviceID);
        percentageView = v.findViewById(R.id.percentage_view);
        routeTo = v.findViewById(R.id.route_to_button);

        bikeIcon = v.findViewById(R.id.edit_icon);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        TrackerList trackerList = ViewModelProviders.of(this).get(TrackerList.class);

        final Observer<ArrayList<Tracker>> trackerObserver = trackers ->
        {
            track = trackers.get(index);
            String percentage = Double.toString(track.getBatteryPer()) + "%";

            editName.setText(track.getTrackerName());
            addressView.setText(track.getAddress());
            percentageView.setText(percentage);
            bikeIcon.setColorFilter(Color.parseColor(track.getColour()));
            deviceId.setText(track.getDeviceId());
            bikeIcon.setImageResource(track.getIconSource());
        };

        saveButton.setOnClickListener(v1 ->
        {
            Tracker newTrack = new Tracker(track);

            if(!editName.getText().toString().equals(newTrack.getTrackerName()))
            {
                newTrack.setTrackerName(editName.getText().toString());
            }

            trackerList.editTracker(track, newTrack, sharedPref);

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, FragmentList.newInstance());
            fragmentTransaction.commit();
        });

        trackerList.getTrackerList(sharedPref, getContext(), new SessionHandler(getContext()).getUserDetails().getEmail(), wifiHandler.checkWifi()).observe(this, trackerObserver);

        bikeIcon.setOnClickListener(v1 ->
        {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, FragmentChangeSetting.newInstance(index));
            fragmentTransaction.commit();
        });

        locateButton.setOnClickListener(v1 ->
        {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, FragmentMap.newInstance(index));
            fragmentTransaction.commit();
        });

        routeTo.setOnClickListener(v1 ->
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + track.getLatLng().latitude + "," + track.getLatLng().longitude));
            startActivity(browserIntent);
        });

        return v;
    }
}
