package com.example.divya.talenthunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.divya.talenthunt.Upload_Handle;
import java.net.URI;
import java.util.concurrent.ExecutionException;

public class upload extends AppCompatActivity implements View.OnClickListener {

    private Button buttonChoose;
    private Button buttonUpload;
    private TextView textView;
    private TextView textViewResponse;
    private EditText tag;
    private static final int SELECT_VIDEO=3;
    private String selectedPath,username,tagname;
    private static final String PREF_NAME="TalhuntPref";
    SessionaManagement sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        buttonChoose=(Button)findViewById(R.id.buttonchoose);
        buttonUpload=(Button)findViewById(R.id.buttonupload);

        textView=(TextView)findViewById(R.id.textView);
        textViewResponse=(TextView)findViewById(R.id.textViewresponse);

        tag=(EditText)findViewById(R.id.tag);
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    public void chooseVideo()
    {
        Intent i=new Intent();
        i.setType("video/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select a Video"), SELECT_VIDEO);
    }

    public void onActivityResult(int requestcode,int resultcode,Intent data) {
        if (resultcode == RESULT_OK) {
            if (requestcode == SELECT_VIDEO) {
                System.out.println("Select_Video");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                textView.setText(selectedPath);

            }
        }
    }

    private String getPath(Uri uri)
    {
        Cursor cursor=getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id=cursor.getString(0);
        document_id=document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor=getContentResolver().query(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Images.Media._ID+"=?",new String[]{document_id},null);
        cursor.moveToFirst();
        String path=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private int uploadVideo()
    {
        tagname=tag.getText().toString();
        String x=null;
        class UploadVideo extends AsyncTask<Void,Void,String>
        {
            ProgressDialog uploading;

            protected void onPreExecute()
            {
                super.onPreExecute();
                uploading=ProgressDialog.show(upload.this,"Uploading File","please wait",false,false);
            }

            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                uploading.dismiss();
                textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            protected String doInBackground(Void... params)
            {
                SharedPreferences sp=getApplicationContext().getSharedPreferences(PREF_NAME,MODE_PRIVATE);
                username=sp.getString("email",null);
                Upload_Handle u=new Upload_Handle();
                int msg=u.uploadVideo(selectedPath,tagname,username);
                return String.valueOf(msg);

            }

        }
        UploadVideo uv=new UploadVideo();
        try {
            x=uv.execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(x);
    }

    public void onClick(View v)
    {
        if(v==buttonChoose)
        {
            chooseVideo();
        }
        if(v==buttonUpload)
        {
            int msg=uploadVideo();
            if(msg==99)
            {
                Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_LONG).show();
            }
            else if(msg==999)
            {
                Toast.makeText(getApplicationContext(), "ERROR!!!", Toast.LENGTH_LONG).show();
            }
            else if(msg==1)
            {
                Toast.makeText(getApplicationContext(), "Upload Successfully", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(), Profile.class);
                startActivity(i);
            }
            else if(msg==0) {
                Toast.makeText(getApplicationContext(), "Some Problem:-|", Toast.LENGTH_LONG).show();
            }
        }
    }
  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
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
