package com.example.mushy.msd_assignment;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/*
    Function used for the add a new website screen
 */

public class AddWebsite_Activity extends AppCompatActivity
{
    private DataTaskList db_TL_p;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_website_);

        final EditText wName = (EditText) findViewById(R.id.wName2);
        final EditText uName = (EditText) findViewById(R.id.userName2);
        final EditText pass = (EditText) findViewById(R.id.password2);
        final EditText note = (EditText) findViewById(R.id.Notes2);

        wName.requestFocus(); // used so we get the focus on the first text view
        Button back = (Button) findViewById(R.id.back_b2);
        Button add = (Button) findViewById(R.id.add_button2);

        // Getting the value passed from the vault
        userId= Integer.parseInt(getIntent().getStringExtra("userId"));


        db_TL_p = new DataTaskList(this);
        try{
            db_TL_p.open();


        }
        catch (Exception e){
            e.printStackTrace();
        }


        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*
                    W stands for website
                    U stands for user of the app
                    UW stands for user of the website
                    O == U
                 */
                String wName_s = String.valueOf(wName.getText());
                String uwName_s = String.valueOf(uName.getText());
                String uwPass_s = String.valueOf(pass.getText());
                String wNote_s = String.valueOf(note.getText());
                String oName, oPass;

                // getting the username and pass from db knowing the userId so we could pass them back to the Vault screen

                Cursor c= db_TL_p.getNamePass(userId);

                if(c != null && c.moveToFirst()){

                    db_TL_p.insertWebsite(userId, wName_s, uwName_s, uwPass_s, wNote_s);
                    db_TL_p.close();
                    Toast.makeText(AddWebsite_Activity.this, "Website added!", Toast.LENGTH_LONG).show();

                    // Values to be passed back to the vault
                    oName = c.getString(c.getColumnIndex("KEY_USERNAME"));
                    oPass = c.getString(c.getColumnIndex("KEY_PASS"));

                    Intent vault_screen = new Intent(AddWebsite_Activity.this, Vault.class);
                    vault_screen.putExtra("username",oName);
                    vault_screen.putExtra("pass", oPass);
                    startActivity(vault_screen);
                    finish();
                    // Add to DB
                }
                else
                {
                    Toast.makeText(AddWebsite_Activity.this, "Bleep Blop... something went wrong", Toast.LENGTH_LONG).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                finish();
            }
        });
    }


}
