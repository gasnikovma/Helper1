package app.gasnikov.helper;

public class LocationModel {
    private double lat, lng;


    LocationModel() {}



    public LocationModel(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }



    public double getLat() { return lat; }
    public double getLng() { return lng; }
}
