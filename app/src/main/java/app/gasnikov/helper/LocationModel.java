package app.gasnikov.helper;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationModel implements Parcelable {
    private double lat, lng;


    LocationModel() {}



    public LocationModel(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }


    protected LocationModel(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    public double getLat() { return lat; }
    public double getLng() { return lng; }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);

    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
