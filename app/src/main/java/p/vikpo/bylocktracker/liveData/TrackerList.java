package p.vikpo.bylocktracker.liveData;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import p.vikpo.bylocktracker.helpers.Tracker;

public class TrackerList extends ViewModel
{
    private MutableLiveData<ArrayList<Tracker>> trackerList;
    private ArrayList<Tracker> trackerPrefList;
    private Gson gson = new Gson();
    private Type colType = new TypeToken<ArrayList<Tracker>>(){}.getType();

    public MutableLiveData<ArrayList<Tracker>> getTrackerList(SharedPreferences sharedPref)
    {
        if(trackerList == null)
        {
            trackerList = new MutableLiveData<>();
        }

        loadTrackers(sharedPref);

        return trackerList;
    }

    private void loadTrackers(SharedPreferences sharedPref)
    {
        Handler myHandler = new Handler();

        myHandler.postDelayed(() ->{
            if(trackerPrefList == null)
            {
                trackerPrefList = gson.fromJson(sharedPref.getString("tracker", ""), colType);

                if(trackerPrefList == null)
                {
                    trackerPrefList = new ArrayList<>();
                }
                Log.e("byLock", Integer.toString(trackerPrefList.size()));
            }
            trackerList.setValue(trackerPrefList);
        },200);
    }

    public void addTracker(Tracker tracker, SharedPreferences sharedPref)
    {
        if(trackerList == null)
        {
            getTrackerList(sharedPref);
        }

        if(trackerPrefList == null )
        {
            if((trackerPrefList = gson.fromJson(sharedPref.getString("tracker", ""), colType)) == null)
            {
                trackerPrefList = new ArrayList<>();
            }
        }

        trackerPrefList.add(tracker);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("tracker", gson.toJson(trackerPrefList));
        editor.apply();
    }

    public void updateAddresses(Geocoder geoCoder, SharedPreferences sharedPref)
    {
        loadTrackers(sharedPref);
        trackerPrefList = trackerList.getValue();

        if(trackerPrefList != null)
        {
            for (Tracker s : trackerPrefList)
            {
                LatLng latLng = s.getLatLng();
                ArrayList<Address> addresses = new ArrayList<>();

                try
                {
                    addresses.addAll(geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1));
                }
                catch (IOException ioe)
                {
                    Log.e("byLock", ioe.getMessage());
                }

                String address = addresses.get(0).getAddressLine(0);
                s.setAddress(address);
            }
        }
        else
        {
            Log.e("byLock", "Du sutter Viktor");
        }


    }
}
