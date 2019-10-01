package p.vikpo.bylocktracker.liveData;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.login.MySingleton;
import p.vikpo.bylocktracker.login.SessionHandler;

public class TrackerList extends ViewModel
{
    private MutableLiveData<ArrayList<Tracker>> trackerList;
    private ArrayList<Tracker> trackerPrefList;
    private Gson gson = new Gson();
    private Type colType = new TypeToken<Collection<Tracker>>(){}.getType();
    private SharedPreferences sharedPref;
    private boolean hasLoaded = false, success = false;
    private final String login_url = "tracker/html";
    private String conAddr, eMail;
    private Context context;

    public MutableLiveData<ArrayList<Tracker>> getTrackerList(SharedPreferences sharedPref, Context context, String eMail, String ipAddr)
    {
        if(trackerList == null)
        {
            trackerList = new MutableLiveData<>();
        }
        this.sharedPref = sharedPref;
        this.eMail = eMail;
        this.context = context;
        this.conAddr = ipAddr + login_url;

        if(isNetworkAvailable() && !hasLoaded)
        {
            loadTrackers();
        }
        else if(!isNetworkAvailable() && !hasLoaded)
        {
            loadTrackers(sharedPref);
        }

        return trackerList;
    }

    private void loadTrackers(SharedPreferences sharedPref)
    {
        Handler myHandler = new Handler();

        myHandler.postDelayed(() ->
        {
            if(trackerPrefList == null)
            {
                trackerPrefList = gson.fromJson(sharedPref.getString("trackers", ""), colType);

                if(trackerPrefList == null)
                {
                    trackerPrefList = new ArrayList<>();
                }
            }
            trackerList.setValue(trackerPrefList);
        },200);
    }

    private void loadTrackers()
    {
        JSONArray request = new JSONArray();
        try
        {
            request.put(0, eMail);
        }
        catch (JSONException e)
        {
            Log.e("bylock", "JSON", e);
        }

        try
        {
            Log.e("bylock", conAddr);
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.POST, conAddr + "/getTrackers.php", request, response ->
            {
                if(response.length() > 0)
                {
                    trackerPrefList = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create().fromJson(response.toString(), colType);
                    sharedPref.edit().putString("trackers", gson.toJson(trackerPrefList)).apply();
                    trackerList.setValue(trackerPrefList);
                    hasLoaded = true;
                }
                else
                {
                    loadTrackers(sharedPref);
                }
            },
                    error ->
                    {
                        Log.e("bylock", error.toString(), error);
                    });
            MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
        }
        catch (RuntimeException e)
        {
            Log.e("bylock", "Dab", e);
        }
    }

    public boolean addTracker(Tracker tracker)
    {
        JSONObject request = new JSONObject();
        try
        {
            //Populate the request parameters
            request.put("DevID", tracker.getId());
            request.put("DevName", tracker.getTrackerName());
            request.put("DevColour", tracker.getColour());
            request.put("DevOwner", eMail);
            request.put("DevIconID", tracker.getIconId());
            request.put("DevPin", tracker.getPin());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, conAddr + "addTracker.php", request, response ->
        {
            try
            {
                if (response.getInt("status") == 0)
                {
                    success = true;
                }
            }
            catch (JSONException e)
            {
                Log.e("bylock", "error", e);
            }
        }, error ->
        {

        });

        MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);

        return success;
    }

    public boolean editTracker(Tracker tracker, Tracker targetTracker, SharedPreferences sharedPref)
    {
        boolean success;
        checkTrackerList(sharedPref);

        for (Tracker t : trackerPrefList)
        {
            if(t == tracker)
            {
                t.setColour(targetTracker.getColour());
                t.setBikeOwner(targetTracker.getBikeOwner());
                t.setIconSource(targetTracker.getIconSource());
            }
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("tracker", gson.toJson(trackerPrefList));
        success = editor.commit();

        return success;
    }

    public boolean deleteTracker(Tracker tracker, SharedPreferences sharedPref)
    {
        JSONObject request = new JSONObject();
        try
        {
            //Populate the request parameters
            request.put("DevID", tracker.getId());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, conAddr + "deleteTracker.php", request, response ->
        {
            try
            {
                if (response.getInt("status") == 0)
                {
                    success = true;
                }
            }
            catch (JSONException e)
            {
                Log.e("bylock", "error", e);
            }
        }, error ->
        {

        });

        MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);

        return true;
    }

    private void checkTrackerList(SharedPreferences sharedPref)
    {
        if(trackerList == null)
        {
            //getTrackerList(sharedPref);
        }

        if(trackerPrefList == null)
        {
            if((trackerPrefList = gson.fromJson(sharedPref.getString("trackers", ""), colType)) == null)
            {
                trackerPrefList = new ArrayList<>();
            }
        }
    }

    public void updateAddresses(Geocoder geoCoder, SharedPreferences sharedPref)
    {
        loadTrackers(sharedPref);
        trackerPrefList = trackerList.getValue();

        if(trackerPrefList != null && trackerPrefList.size() != 0 && isEmulator())
        {
            for (Tracker s : trackerPrefList)
            {
                try
                {
                    ArrayList<Address> addresses = new ArrayList<>(geoCoder.getFromLocation(s.getLatLocation(), s.getLongLocation(), 1));

                    if(addresses.get(0).getAddressLine(0) != null)
                    {
                        String address = addresses.get(0).getAddressLine(0);
                        s.setAddress(address);
                    }

                }
                catch (IOException ioe)
                {
                    Log.e("byLock", ioe.getMessage(), ioe);
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("tracker", gson.toJson(trackerPrefList));
            editor.apply();
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}