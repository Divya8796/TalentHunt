package com.example.divya.talenthunt;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class search extends AppCompatActivity implements SearchView.OnQueryTextListener{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFish;
    private video_adapter mAdapter;
    private static String url_create_user="http://192.168.43.213/Talhunt/search.php";
    SearchView searchView = null;
    JSONParser jsonParser =new JSONParser();
    JSONArray details=null;
    List<Data> data = new ArrayList<>();
    SessionaManagement sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }
    public boolean onQueryTextSubmit(String query) {
        new AsyncFetch(query).execute();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
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

 //   @Override
    /*protected void onNewIntent(Intent intent) {
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
            new AsyncFetch(query).execute();

        }
    }*/

    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(search.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;
        public String user_id;
        public String video_id,path;

        public AsyncFetch(String searchQuery)
        {
            this.searchQuery = searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                HashMap<String, String> p = new HashMap<String, String>();
                p.put("searchQuery", searchQuery);
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
                mRVFish = (RecyclerView) findViewById(R.id.videoList);
                mAdapter = new video_adapter(search.this, data);
                mRVFish.setAdapter(mAdapter);
                mRVFish.setLayoutManager(new LinearLayoutManager(search.this));
                pdLoading.dismiss();
               // return null;
            }
            //this method will be running on UI thread


            }

        }


