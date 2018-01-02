package com.example.divya.talenthunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class display_random_videos extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFish;
    private video_adapter mAdapter;
    private static String url_create_user="http://192.168.43.213/Talhunt/display.php";
    SearchView searchView = null;
    JSONParser jsonParser =new JSONParser();
    JSONArray details=null;
    List<Data> data = new ArrayList<>();
    SessionaManagement sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_random_videos);
        new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(display_random_videos.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;
        public String user_id;
        public String video_id,path;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HashMap<String,String> p= new HashMap<String,String>();
                p.put("name","xyz");
                JSONObject json = jsonParser.makeHttpRequest(url_create_user, "POST", p);
                Log.d("Create Response", json.toString());
                details = json.getJSONArray("result");
                for (int i = 0; i < details.length(); i++) {
                    JSONObject json_data = details.getJSONObject(i);
                    Data m_data = new Data();
                    m_data.user_id = json_data.getString("user_id");
                    m_data.path = json_data.getString("url");
                    data.add(m_data);
                }

            }
            catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            mRVFish = (RecyclerView) findViewById(R.id.videoList1);
            mAdapter = new video_adapter(display_random_videos.this, data);
            mRVFish.setAdapter(mAdapter);
            mRVFish.setLayoutManager(new LinearLayoutManager(display_random_videos.this));
            pdLoading.dismiss();
            // return null;
        }
        //this method will be running on UI thread


    }


   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_random_videos, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
