package p.vikpo.bylocktracker.helpers;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;
import java.util.UUID;

import p.vikpo.bylocktracker.R;

public class Tracker
{
    private LatLng latLng;
    private double batteryPer, latLocation, longLocation;
    private int iconId, pin;
    private String bikeOwner, colour, address, trackerName, deviceId;
    private Timestamp lastSeen;

    public int getIconId()
    {
        return iconId;
    }

    public void setIconId(int iconId)
    {
        this.iconId = iconId;
    }

    public int getPin()
    {
        return pin;
    }

    public void setPin(int pin)
    {
        this.pin = pin;
    }

    public String getTrackerName()
    {
        return trackerName;
    }

    public void setTrackerName(String trackerName)
    {
        this.trackerName = trackerName;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public Timestamp getLastSeen()
    {
        return lastSeen;
    }

    public void setLastSeen(Timestamp lastSeen)
    {
        this.lastSeen = lastSeen;
    }

    public Timestamp getActiveDate()
    {
        return activeDate;
    }

    public void setActiveDate(Timestamp activeDate)
    {
        this.activeDate = activeDate;
    }

    private Timestamp activeDate;

    public double getLatLocation()
    {
        return latLocation;
    }

    public void setLatLocation(double latLocation)
    {
        this.latLocation = latLocation;
    }

    public double getLongLocation()
    {
        return longLocation;
    }

    public void setLongLocation(double longLocation)
    {
        this.longLocation = longLocation;
    }

    public Tracker(String id, double longValue, String name, int iconId, String colour, Timestamp lastSeen, String bikeOwner, int pin, double latValue)
    {
        this.deviceId = id;
        this.latLng = new LatLng(longValue, latValue);
        this.latLocation = latValue;
        this.longLocation = longValue;
        this.trackerName = name;
        this.bikeOwner = bikeOwner;
        this.pin = pin;
        this.lastSeen = lastSeen;
        this.colour = colour;
        this.iconId = iconId;
    }

    public Tracker(LatLng latLng, String bikeOwner, String colour, int iconId)
    {
        this.latLng = latLng;
        this.bikeOwner = bikeOwner;
        this.colour = colour;
        this.iconId = iconId;
        //id = UUID.randomUUID();
    }

    public Tracker(Tracker t)
    {
        this.latLng = t.latLng;
        this.bikeOwner = t.bikeOwner;
        this.colour = t.colour;
        this.iconId = t.iconId;
        //this.id = UUID.randomUUID();
    }

    public int getIconSource()
    {
        return iconId;
    }

    public void setIconSource(int iconSource)
    {
        this.iconId = iconSource;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
    }

    public double getBatteryPer()
    {
        return batteryPer;
    }

    public void setBatteryPer(double batteryPer)
    {
        this.batteryPer = batteryPer;
    }

    public String getBikeOwner()
    {
        return bikeOwner;
    }

    public void setBikeOwner(String bikeOwner)
    {
        this.bikeOwner = bikeOwner;
    }

    public String getId()
    {
        return deviceId;
    }

    public void setId(String id)
    {
        this.deviceId = id;
    }

    public String getColour()
    {
        return colour;
    }

    public void setColour(String colour)
    {
        this.colour = colour;
    }

    @Override
    public String toString()
    {
        return getBikeOwner();
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
