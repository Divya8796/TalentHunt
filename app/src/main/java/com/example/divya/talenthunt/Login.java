package com.example.divya.talenthunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class Login extends AppCompatActivity {
    private ProgressDialog pDialog;

    JSONParser jsonParser =new JSONParser();
    EditText inputemail,inputpassword;
    Button btn_login,btn_hirer,btn_register;
    TextView register,forgot_pasword;
    String s,Email,Password;
    private static String url_create_user="http://192.168.43.213/Talhunt/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputemail = (EditText) findViewById(R.id.input_email);
        inputpassword = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.login);
        register=(TextView) findViewById(R.id.reg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),Register.class);
                startActivity(i);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Email=inputemail.getText().toString();
                    Password=inputpassword.getText().toString();

                    if(Email.length()==0 || Password.length()==0) {
                        Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        s = new CreateUser(Email,Password).execute().get();
                        if (s.equals("Success")) {
                            Intent i = new Intent(getApplicationContext(),display_random_videos.class);
                            startActivity(i);
                        } else if (s.equals("fail")) {
                            Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }


        class  CreateUser extends AsyncTask<String,String,String>
        {
            String Email;
            String Password;
            SessionaManagement session;
            int x;
            String status;

            public CreateUser(String email,String password)
            {
                    Email=email;
                    Password=password;
            }
            protected void onPreExecute()
            {
                super.onPreExecute();
                pDialog=new ProgressDialog(Login.this);
                pDialog.setMessage("Wait");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {



                HashMap<String,String> p= new HashMap<String,String>();
                p.put("email",Email);
                p.put("password", Password);

                JSONObject json=jsonParser.makeHttpRequest(url_create_user,"POST",p);
                Log.d("Create Response", json.toString());

                try {
                    x=json.getInt("x");
                    session=new SessionaManagement(getApplicationContext());
                    if (x==1)
                    {
                        status="Success";
                        session.createLoginSession(Email);
                    }
                    else
                    {
                        status="fail";
                    }
                }
                catch (JSONException e)
                {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

                return status;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pDialog.dismiss();
            }
        }
   /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
