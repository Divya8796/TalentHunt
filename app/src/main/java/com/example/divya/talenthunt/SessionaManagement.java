package com.example.divya.talenthunt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.net.ContentHandler;
import java.util.HashMap;

/**
 * Created by Divya on 19-10-2016.
 */
public class SessionaManagement
{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE=0;
    private static final String PREF_NAME="TalhuntPref";
    private static final String IS_LOGIN="IsLoggedIn";
    public static final String KEY_NAME="name";
    public static final String KEY_EMAIL="email";

    public SessionaManagement(Context context)
    {
        this._context=context;
        pref=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }

    public void createLoginSession(String email)
    {
        editor.putBoolean(IS_LOGIN,true);
     //   editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public HashMap<String,String>getUserDetails()
    {
        HashMap<String,String>  user= new HashMap<String,String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }

    public void checkLogin()
    {
        if(!this.isLoggedIn())
        {
            Intent i=new Intent(_context,Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

   public void logoutUser()
   {
       editor.clear();
       editor.commit();
       Intent i=new Intent(_context,Login.class);
       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       _context.startActivity(i);
   }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN,false);
    }
}
