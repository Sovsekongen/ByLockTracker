package p.vikpo.bylocktracker.trackerCom;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import p.vikpo.bylocktracker.helpers.Tracker;
import p.vikpo.bylocktracker.login.MySingleton;

public class TrackerCom
{
    private Context context;
    private final String trackerURL = "", KEY_OWNER = "Email";
    private Type colType = new TypeToken<ArrayList<Tracker>>(){}.getType();

    public TrackerCom(Context cont)
    {
         context = cont;
    }

    public interface onResponse
    {
        Tracker[] onTrackersLoaded();
    }

    public void loadTrackers(String owner)
    {
        JSONObject request = new JSONObject();
        try
        {
            request.put(KEY_OWNER, owner);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, trackerURL, request, response ->
                {
                    if(response.toString().length() != 0)
                    {
                        onTrackersLoaded(response);
                    }
                    else
                    {
                        Toast.makeText(context, "Der er sket en fejl", Toast.LENGTH_SHORT).show();
                    }
                }, error ->
                {
                    Log.e("bylock", "JSONObjectRequest Error", error);
                    Toast.makeText(context, "Der er sket endnu en fejl :))", Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
    }

    public Tracker[] onTrackersLoaded(JSONObject jObject)
    {
        Tracker[] trackerList;
        Gson converter = new Gson();
        trackerList = converter.fromJson(jObject.toString(), colType);
        return trackerList;
    }
}
