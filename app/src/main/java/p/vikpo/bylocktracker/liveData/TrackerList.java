package p.vikpo.bylocktracker.liveData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private SharedPreferences.Editor editor;
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
                Log.e("byLock", Integer.toString(trackerPrefList.size()));
            }
            trackerList.setValue(trackerPrefList);
        },2000);
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

        editor = sharedPref.edit();

        editor.putString("tracker", gson.toJson(trackerPrefList));
        editor.apply();
    }
}
