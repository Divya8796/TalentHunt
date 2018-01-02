package com.example.divya.talenthunt;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonHirerRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLogin=(Button)findViewById(R.id.btn_login1);
        buttonRegister=(Button)findViewById(R.id.btn_register1);
        buttonHirerRegister=(Button)findViewById(R.id.btn_hirer_register1);
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        buttonHirerRegister.setOnClickListener(this);
    }

    //@Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
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


    public void onClick(View v)
    {
        if(v== buttonLogin)
        {
            Intent i=new Intent(MainActivity.this,Login.class);
            startActivity(i);
        }
        if(v==buttonRegister)
        {
                Intent i=new Intent(MainActivity.this,Register.class);
                startActivity(i);
        }
        if(v==buttonHirerRegister)
        {
            Intent i=new Intent(MainActivity.this,Hirer.class);
            startActivity(i);
        }
    }

}
