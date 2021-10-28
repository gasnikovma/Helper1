package app.gasnikov.helper;

import com.google.firebase.database.ServerValue;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserLocation {
    //object
    private LocationModel geo_point;
    //private Object time_stamp =  ServerValue.TIMESTAMP;
    private User user;


    public UserLocation(LocationModel geo_point, User user) {
        this.geo_point = geo_point;

        this.user = user;
    }
   /* public String time_stamp() {
        return SimpleDateFormat.getDateInstance(DateFormat., Locale.US).format(time_stamp);
    }*/
    public UserLocation() {

    }


    public LocationModel getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(LocationModel geo_point) {
        this.geo_point = geo_point;
    }



  /*  public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }*/



    public void setUser(User user) {
        this.user = user;
    }

   /* public Date getTime_stamp() {
        return time_stamp;
    }*/

    public User getUser() {
        return user;
    }


}
