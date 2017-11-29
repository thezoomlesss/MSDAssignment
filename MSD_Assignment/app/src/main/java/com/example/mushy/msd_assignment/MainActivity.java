package com.example.mushy.msd_assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    Button login_button;
    Button register_button;
    //DataTaskList dbTL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button) findViewById(R.id.log);
        register_button = (Button) findViewById(R.id.reg);

//       For testing purposes
//       this.deleteDatabase("PassManagerDB");


        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent login_screen= new Intent(MainActivity.this, LogIn_Activity.class);
                startActivity(login_screen);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent register_screen= new Intent(MainActivity.this, SignUp_Activity.class);
                startActivity(register_screen);
            }
        });


    }



}
