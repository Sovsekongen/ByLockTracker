package p.vikpo.bylocktracker.helpers;

import java.util.UUID;

public class Tracker
{
    private double latitude, longitude, batteryPer;
    private String bikeOwner, colour;
    private UUID id;

    public Tracker()
    {
        latitude = 55.367913;
        longitude = 10.428155;
        batteryPer = 100;
        bikeOwner = "Owner Ownersen";
        colour = "#48696b";
        id = UUID.randomUUID();
    }


    public Tracker(double lat, double longt, double bat, String bikeOwner)
    {
        this.latitude = lat;
        this.longitude = longt;
        this.batteryPer = bat;
        this.bikeOwner = bikeOwner;
        id = UUID.randomUUID();
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
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

    @Override
    public String toString()
    {
        return getBikeOwner();
    }
}
