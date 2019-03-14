package p.vikpo.bylocktracker.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import p.vikpo.bylocktracker.R;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class map_fragment extends Fragment implements OnMapReadyCallback
{
    public static map_fragment newInstance()
    {
        return new map_fragment();
    }


    private final String PROVIDER = LocationManager.GPS_PROVIDER;
    private boolean isLocationUpdated;
    private GoogleMap googleMap;
    private LatLngBounds bounds;
    private LatLng lat, lng;
    private MapView mapView;
    private double longitude, latitude;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isLocationUpdated = false;
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
        View v = inflater.inflate(R.layout.fragment_map, container, false);


        lat = new LatLng(0, 0);
        lng = new LatLng(0, 0);
        bounds = new LatLngBounds(lat, lng);

        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_directions_bike_black_24dp);

        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location)
            {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                String stats = "Latitude: " + Double.toString(latitude) + " Longitude: " + Double.toString(longitude);
                if(getContext() != null)
                {
                    Toast.makeText(getContext(), stats, Toast.LENGTH_SHORT).show();
                }

                LatLng sydney = new LatLng(latitude, longitude);
                if(bitmap != null)
                {
                    googleMap.addMarker(new MarkerOptions().position(sydney)
                            .title("Marker in ODENSE BABY")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                }
                else
                {
                    googleMap.addMarker(new MarkerOptions().position(sydney)
                            .title("Marker in ODENSE BABY")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.android_bike)));
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                isLocationUpdated = true;
            }

            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            public void onProviderEnabled(String provider)
            {

            }

            public void onProviderDisabled(String provider)
            {

            }
        };

        if(isLocationUpdated)
        {
            locationManager.removeUpdates(locationListener);
        }
        else
        {
            checkPermission();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        googleMap = map;

        //googleMap.setLatLngBoundsForCameraTarget(bounds);
        googleMap.setMinZoomPreference(6.0f);
        googleMap.setMaxZoomPreference(16.0f);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.371326, 10.427586), 12.0f));
        getLocation();
    }

    private void checkPermission()
    {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
        }
    }


    private void getLocation()
    {
        checkPermission();
        locationManager.requestLocationUpdates(PROVIDER, 5000, 0, locationListener);
    }
}
