package com.example.divya.talenthunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog pDialog;
    EditText inputemail,inputname,inputpassword;
    Button btn_register,btn_choose;
    private static String url_create_user="http://192.168.43.213/Talhunt/register.php";
    String s,email,name,password,uploadImage;
    private static final int SELECT_IMAGE=1;
    private Uri filepath;
    private Bitmap bitmap;
    JSONParser jsonParser =new JSONParser();
    SessionaManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputemail=(EditText)findViewById(R.id.input_email);
        inputname=(EditText)findViewById(R.id.input_name);
        inputpassword=(EditText)findViewById(R.id.input_password);

        btn_register=(Button)findViewById(R.id.btn);
        btn_choose=(Button)findViewById(R.id.choose_btn);
        btn_choose.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == btn_choose) {
            showFileChooser();
        }

        if (v == btn_register) {
            register();
        }
    }

    public  void register()
    {
        try
        {
            email=inputemail.getText().toString();
            name=inputname.getText().toString();
            password=inputpassword.getText().toString();
            uploadImage=getStringImage(bitmap);

            if(name.length()==0 || email.length()==0 || password.length()==0 || uploadImage.length()==0)
            {
                Toast.makeText(getApplicationContext(), "Something is missing..", Toast.LENGTH_LONG).show();
            }
            else
            {
                s = new CreateUser(email, name, password,uploadImage).execute().get();

                if (s.equals("Success")) {
                    session=new SessionaManagement(getApplicationContext());
                    session.createLoginSession(email);
                    Intent i = new Intent(getApplicationContext(),display_random_videos.class);
                    startActivity(i);
                } else if (s.equals("fail")) {
                    Toast.makeText(getApplicationContext(), "Something is missing..", Toast.LENGTH_LONG).show();
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void showFileChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select a Image"), SELECT_IMAGE);
    }

    public void onActivityResult(int requestcode,int resultcode,Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
            if (requestcode == SELECT_IMAGE && resultcode==RESULT_OK && data!=null && data.getData()!=null) {
                 filepath=data.getData();
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes=baos.toByteArray();
        String encodedImage=Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    class  CreateUser extends AsyncTask<String,String,String>
    {
        SessionaManagement session;
        int x;
        String status,email,name,password,uploadImage;

        public CreateUser(String email,String name,String password,String uploadImage)
        {
            this.email=email;
            this.name=name;
            this.password=password;
            this.uploadImage=uploadImage;
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog=new ProgressDialog(Register.this);
            pDialog.setMessage("Wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            HashMap<String,String> p= new HashMap<String,String>();
            p.put("email",email);
            p.put("name", name);
            p.put("password", password);
            p.put("uploadImage",uploadImage);

            JSONObject json=jsonParser.makeHttpRequest(url_create_user,"POST",p);
            Log.d("Create Response", json.toString());
            try {
                x=json.getInt("x");
                session=new SessionaManagement(getApplicationContext());
                if (x==1)
                {
                    status="Success";
                    session.createLoginSession(email);
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
}