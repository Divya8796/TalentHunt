/*how to upload image*/
package com.example.divya.talenthunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class Profile extends AppCompatActivity {

    TextView email,name;
    ImageView image;
    JSONParser jsonParser =new JSONParser();
    String username,name1,imageurl;
    private static final String PREF_NAME="TalhuntPref";
    JSONArray details=null;
    private static String url_create_user="http://192.168.43.213/Talhunt/profile.php";
    private Bitmap[] bitmaps;
    SessionaManagement sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=(TextView)findViewById(R.id.input_name);
        email=(TextView)findViewById(R.id.email);
        image=(ImageView)findViewById(R.id.image);
        try {
            SharedPreferences sp=getApplicationContext().getSharedPreferences(PREF_NAME,MODE_PRIVATE);
            username=sp.getString("email",null);
            name1=new CreateUser(username).execute().get();
            name.setText(name1);
            email.setText(username);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class  CreateUser extends AsyncTask<String,String,String>
    {
        String email;
        String name;
        String status;
        URL url=null;
        Bitmap img;
        public CreateUser(String email)
        {
            this.email=email;
        }
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> p= new HashMap<String,String>();
            p.put("email", email);
            JSONObject json=jsonParser.makeHttpRequest(url_create_user,"POST",p);
            Log.d("Create Response", json.toString());
            try {
                details=json.getJSONArray("result");
                for(int i=0;i<details.length();i++) {
                    JSONObject c=details.getJSONObject(i);
                    name = c.getString("name");
                    imageurl=c.getString("url");
                }
                imageurl="http://192.168.43.213/Talhunt/uploads/image/"+imageurl;
                url=new URL(imageurl);
                img= BitmapFactory.decodeStream(url.openConnection().getInputStream());
                image.setImageBitmap(img);
            }
            catch (JSONException e)
            {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return name;
          }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent i = new Intent(getApplicationContext(),search.class);
            startActivity(i);
        }
        else if (id == R.id.action_logout) {
            sm=new SessionaManagement(getApplicationContext());
            sm.logoutUser();
        }
        else if (id == R.id.action_profile) {
            Intent i = new Intent(getApplicationContext(),Profile.class);
            startActivity(i);
        }
        else if (id == R.id.action_upload) {
            Intent i = new Intent(getApplicationContext(),upload.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);    }
}
