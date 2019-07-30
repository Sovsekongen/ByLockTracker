package p.vikpo.bylocktracker.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.model.LatLng;

import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.liveData.TrackerList;

public class FragmentAddBike extends Fragment implements FragmentDialogList.OnColourPicked
{
    private TextView add_bike_name, add_bike_tracker;
    private Button add_bike_colour, add_bike_icon, add_bike;
    private SharedPreferences sharedPref;
    private int iconID = R.drawable.android_bike;
    private String colour = "#ffffff";

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_add_bike, container, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        add_bike_name = v.findViewById(R.id.change_bike_name);
        add_bike_tracker = v.findViewById(R.id.change_bike_trackerID);

        add_bike_colour = v.findViewById(R.id.change_bike_colour);
        add_bike_icon = v.findViewById(R.id.change_bike_icon);
        add_bike = v.findViewById(R.id.change_bike);

        add_bike.setOnClickListener(v1 ->
        {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, FragmentList.newInstance());

            if(addBike())
            {
                fragmentTransaction.commit();
            }
        });

        add_bike_colour.setOnClickListener(v1 -> pickColour());
        add_bike_icon.setOnClickListener(v1 -> pickIcon());

        return v;
    }

    private void pickColour()
    {
        if(getActivity() != null)
        {
            if(getFragmentManager() != null)
            {
                FragmentManager fm = getFragmentManager();
                FragmentDialogList editNameDialogFragment = FragmentDialogList.newInstance("Pick Colour");

                editNameDialogFragment.setTargetFragment(FragmentAddBike.this, 300);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        }
    }

    private void pickIcon()
    {
        if(getActivity() != null)
        {
            if(getFragmentManager() != null)
            {
                FragmentManager fm = getFragmentManager();
                FragmentDialogList editNameDialogFragment = FragmentDialogList.newInstance("Pick Icon");

                editNameDialogFragment.setTargetFragment(FragmentAddBike.this, 300);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        }
    }

    public static FragmentAddBike newInstance()
    {
        return new FragmentAddBike();
    }

    private boolean addBike()
    {
        boolean success;
        TrackerList trackerList = ViewModelProviders.of(this).get(TrackerList.class);

        success = trackerList.addTracker(new Tracker(new LatLng(55.392007, 10.406941), add_bike_name.getText().toString(), colour, iconID), sharedPref);

        return success;
    }

    @Override
    public void onColourPicked(String colour)
    {
        if(!colour.equals("null"))
        {
            this.colour = colour;
        }
    }
}
