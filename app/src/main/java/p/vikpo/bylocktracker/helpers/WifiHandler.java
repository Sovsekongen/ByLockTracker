package p.vikpo.bylocktracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.preference.PreferenceManager;

public class WifiHandler
{

    private Context context;

    public WifiHandler(Context context)
    {
        this.context = context;
    }

    public String checkWifi()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ipLocal = sharedPreferences.getString("ipLocal", "192.168.1.50");
        String ipPublic = sharedPreferences.getString("ipPublic", "80.210.78.51");

        if(context == null)
        {
            return "http://" + ipLocal + "/?";
        }

        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnected())
        {
            WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String name = wifiInfo.getSSID();

            if(name.contains("Svendbent") || name.contains("FBI Surveillance Van") || name.contains("FBI Surveillance Van_2.4Gre"))
            {
                return "http://" + ipLocal + "/";
            }
            else
            {
                //return "http://" + ipLocal + "/?";
                return "http://" + ipPublic + "/";
            }
        }

        return "http://" + ipLocal + "/";
    }
}
