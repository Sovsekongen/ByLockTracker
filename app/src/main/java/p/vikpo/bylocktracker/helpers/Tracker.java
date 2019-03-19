package p.vikpo.bylocktracker.helpers;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class Tracker
{
    private LatLng latLng;
    private double batteryPer;
    private String bikeOwner, colour, address;
    private UUID id;

    public Tracker()
    {
        latLng = new LatLng(55.367913, 10.428155);
        batteryPer = 100;
        bikeOwner = "Owner Ownersen";
        colour = "#48696b";
        id = UUID.randomUUID();
    }

    public Tracker(double lat, double longt, double bat, String bikeOwner, String colour)
    {
        this.latLng = new LatLng(lat, longt);
        this.batteryPer = bat;
        this.bikeOwner = bikeOwner;
        this.colour = colour;
        id = UUID.randomUUID();
    }

    public Tracker(LatLng latLng, String bikeOwner, String colour)
    {
        this.latLng = latLng;
        this.bikeOwner = bikeOwner;
        this.colour = colour;
        id = UUID.randomUUID();
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

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
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
