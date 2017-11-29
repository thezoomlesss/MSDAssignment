package com.example.mushy.msd_assignment;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateWebsiteActivity extends AppCompatActivity
{

    final String not_allowed_chars= "\"$&*/;,.:'";
    EditText wName, uName, pass, note;
    DataTaskList db_TL_u;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_website);

        final String websiteName_get = getIntent().getStringExtra("websiteName");
        final String userName_get = getIntent().getStringExtra("userName");
        final String password_get = getIntent().getStringExtra("password");
        final String note_get = getIntent().getStringExtra("note");
        final String userId = getIntent().getStringExtra("userId");
        final String initialWebsiteName_get = websiteName_get;     // websiteName_get actually changes, we keep a copy of the original value

        wName = (EditText) findViewById(R.id.wName1);
        uName = (EditText) findViewById(R.id.userName1);
        pass = (EditText) findViewById(R.id.password1);
        note = (EditText) findViewById(R.id.Notes1);

        // Setting the values that we already have
        wName.setText(websiteName_get);
        uName.setText(userName_get);
        pass.setText(password_get);
        note.setText(note_get);

        wName.requestFocus();

        Button back = (Button) findViewById(R.id.back_b1);
        Button update = (Button) findViewById(R.id.update_button1);
        try{
            db_TL_u = new DataTaskList(this);
            db_TL_u.open();

        }catch (Exception e){
            e.printStackTrace();
        }

        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                String wname = String.valueOf(wName.getText());
                String wuser = String.valueOf(uName.getText());
                String wpass = String.valueOf(pass.getText());
                String wnote = String.valueOf(note.getText());

                // Checking for SQL Injection
                boolean update_condition = true;
                for(int i=0; i< wname.length(); i++){
                    if( not_allowed_chars.contains(String.valueOf(wname.charAt(i)))){
                        update_condition = false;
                        break;
                    }
                }
                for(int i=0; i< wuser.length(); i++){
                    if( not_allowed_chars.contains(String.valueOf(wuser.charAt(i)))){
                        update_condition = false;
                        break;
                    }
                }

                for(int i=0; i< wpass.length(); i++){
                    if( not_allowed_chars.contains(String.valueOf(wpass.charAt(i)))){
                        update_condition = false;
                        break;
                    }
                }

                for(int i=0; i< wnote.length(); i++){
                    if( not_allowed_chars.contains(String.valueOf(wnote.charAt(i)))){
                        update_condition = false;
                        break;
                    }
                }

                if(update_condition){
                    // Update DB

                    long updated= db_TL_u.updateWebsite(Integer.parseInt(userId), initialWebsiteName_get, wname, wuser, wpass, wnote);
                    if (updated >= 1)
                    {
                        Toast.makeText(UpdateWebsiteActivity.this, "Website updated!", Toast.LENGTH_LONG).show();
                        Cursor c = db_TL_u.getNamePass(Integer.parseInt(userId));
                        if(c != null && c.moveToFirst()){
                            Intent i= new Intent(UpdateWebsiteActivity.this, Vault.class);
                            i.putExtra("username",c.getString(c.getColumnIndex("KEY_USERNAME")));
                            i.putExtra("pass", c.getString(c.getColumnIndex("KEY_PASS")));
                            db_TL_u.close();
                            startActivity(i);
                            finish();
                        }else
                        {
                            Toast.makeText(UpdateWebsiteActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                            db_TL_u.close();
                        }
                    }
                    else
                    {
                        Toast.makeText(UpdateWebsiteActivity.this, "Something went wrong! Update: " + updated, Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(UpdateWebsiteActivity.this, "You have entered some characters that are not allowed!", Toast.LENGTH_LONG).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                db_TL_u.close();
                finish();
            }
        });
    }
}
