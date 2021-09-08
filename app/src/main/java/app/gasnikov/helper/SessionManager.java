package app.gasnikov.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;
    public static final String s="u";
    public static final String KEY_EMAIL="email";

    public static final String KEY_PASSWORD="password";
    public SessionManager(Context context){
        context=context;
        usersSession=context.getSharedPreferences(s,Context.MODE_PRIVATE);
        editor=usersSession.edit();
    }
    public void remember(String email, String password){
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }
    public HashMap<String,String>h(){
        HashMap<String,String>data=new HashMap<>();
        data.put(KEY_EMAIL,usersSession.getString(KEY_EMAIL,null));
        data.put(KEY_PASSWORD,usersSession.getString(KEY_PASSWORD,null));
        return data;
    }


}
