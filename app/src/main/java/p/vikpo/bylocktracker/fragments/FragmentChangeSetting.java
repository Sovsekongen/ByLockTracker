package p.vikpo.bylocktracker.fragments;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.helpers.WifiHandler;
import p.vikpo.bylocktracker.liveData.TrackerList;
import p.vikpo.bylocktracker.login.SessionHandler;

public class FragmentChangeSetting extends Fragment implements FragmentDialogList.OnColourPicked, FragmentDialogList.OnIconPicked
{

    private TextView change_bike_name;
    private Button change_bike_colour, change_bike_icon, change_bike;
    private SharedPreferences sharedPref;
    private int iconID = R.drawable.android_bike, listNum;
    private String colour;
    private Tracker track;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_change_setting, container, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        WifiHandler wifiHandler = new WifiHandler(getContext());

        listNum = getArguments().getInt("listNumber");
        change_bike_name = v.findViewById(R.id.change_bike_name);

        change_bike_colour = v.findViewById(R.id.change_bike_colour);
        change_bike_icon = v.findViewById(R.id.change_bike_icon);
        change_bike = v.findViewById(R.id.change_bike);

        TrackerList trackerList = ViewModelProviders.of(this).get(TrackerList.class);

        final Observer<ArrayList<Tracker>> trackerObserver = trackers ->
        {
            track = trackers.get(listNum);
            colour = track.getColour();

            change_bike_name.setText(track.getBikeOwner());
        };

        change_bike.setOnClickListener(v1 ->
        {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, FragmentEditBike.newInstance(listNum));

            Tracker bufTracker = new Tracker(track);

            if(!change_bike_name.getText().toString().equals(track.getBikeOwner()) || track.getColour() != colour || track.getIconSource() != iconID)
            {
                bufTracker.setBikeOwner(change_bike_name.getText().toString());
                bufTracker.setColour(colour);
                bufTracker.setIconSource(iconID);

                if(editBike(track, bufTracker, sharedPref))
                {
                    fragmentTransaction.commit();
                }
            }
            else
            {
                fragmentTransaction.commit();
            }
        });

        trackerList.getTrackerList(sharedPref, getContext(), new SessionHandler(getContext()).getUserDetails().getEmail(), wifiHandler.checkWifi()).observe(this, trackerObserver);

        change_bike_colour.setOnClickListener(v1 -> pickColour());
        change_bike_icon.setOnClickListener(v1 -> pickIcon());

        return v;
    }

    private void pickIcon()
    {
        if(getActivity() != null)
        {
            if(getFragmentManager() != null)
            {
                FragmentManager fm = getFragmentManager();
                FragmentDialogList editNameDialogFragment = FragmentDialogList.newInstance("Pick Icon");

                editNameDialogFragment.setTargetFragment(FragmentChangeSetting.this, 300);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        }
    }

    public static FragmentChangeSetting newInstance(int getListNumber)
    {
        FragmentChangeSetting frag = new FragmentChangeSetting();
        Bundle args = new Bundle();
        args.putInt("listNumber", getListNumber);
        frag.setArguments(args);

        return frag;
    }

    private boolean editBike(Tracker tracker, Tracker bufTracker, SharedPreferences sharedPref)
    {
        boolean success;
        TrackerList trackerList = ViewModelProviders.of(this).get(TrackerList.class);
        success = trackerList.editTracker(tracker, bufTracker, sharedPref);

        return success;
    }

    private void pickColour()
    {
        if(getActivity() != null)
        {
            if(getFragmentManager() != null)
            {
                FragmentManager fm = getFragmentManager();
                FragmentDialogList editNameDialogFragment = FragmentDialogList.newInstance("Pick Colour");

                editNameDialogFragment.setTargetFragment(FragmentChangeSetting.this, 300);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        }
    }

    @Override
    public void onColourPicked(String colour)
    {
        if(!colour.equals("null"))
        {
            this.colour = colour;
        }
    }

    @Override
    public void onIconPicked(int id)
    {
        if(id != 0)
        {
           this.iconID = id;
        }
    }
}
