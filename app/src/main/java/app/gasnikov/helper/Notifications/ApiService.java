package app.gasnikov.helper.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA6mA1xK8:APA91bFXHDc-_iUcS6KlMexfHupqGdH4melU4UgI9vK03rghYMUwxlMdzwdhoofhdL-EGnjJqyTyCRv7GTHYEWMPMEtubTQDhJkBYgcT10seur_fz0cyQnroKBNwHV0qAFSy7PZVSpZI",

            }
    )
    @POST("fcm/send")
    Call<Response>sendnotification(@Body Sender body);

}
