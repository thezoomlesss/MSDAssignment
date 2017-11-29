package com.example.mushy.msd_assignment;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SingleWebsiteActivity extends AppCompatActivity
{

    private DataTaskList db_TL_y;
    private String websiteName_get, userName_get, password_get, note_get, userId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_website);

        // Getting the passed content
        websiteName_get = getIntent().getStringExtra("websiteName");
        userName_get = getIntent().getStringExtra("userName");
        password_get = getIntent().getStringExtra("password");
        note_get = getIntent().getStringExtra("note");
        userId = getIntent().getStringExtra("userId");

        Button update =(Button) findViewById(R.id.update_button);
        Button delete =(Button) findViewById(R.id.delete_button);
        Button back =(Button) findViewById(R.id.back_b);


        TextView wName = (TextView) findViewById(R.id.wName);
        TextView uName = (TextView) findViewById(R.id.userName);
        TextView pass = (TextView) findViewById(R.id.password);
        TextView note = (TextView) findViewById(R.id.Notes);

        // Setting the textviews to display the passed content
        wName.setText(websiteName_get);
        uName.setText(userName_get);
        pass.setText(password_get);
        note.setText(note_get);
        db_TL_y= new DataTaskList(SingleWebsiteActivity.this);

        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent update_screen= new Intent(SingleWebsiteActivity.this, UpdateWebsiteActivity.class);
                update_screen.putExtra("websiteName", websiteName_get);
                update_screen.putExtra("userName", userName_get);
                update_screen.putExtra("password", password_get);
                update_screen.putExtra("note", note_get);
                update_screen.putExtra("userId", userId);
                startActivity(update_screen);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(SingleWebsiteActivity.this, "Hold down to delete the website!", Toast.LENGTH_LONG).show();
            }
        });

        // On long hold -> delete
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try{

                    db_TL_y.open();
                    if(db_TL_y.deleteWebsite(Integer.parseInt(userId), websiteName_get))
                    {
                        Toast.makeText(SingleWebsiteActivity.this, "Website Deleted!", Toast.LENGTH_LONG).show();
                        db_TL_y.open();
                        Intent vault_screen= new Intent(SingleWebsiteActivity.this, Vault.class);
                        Cursor c = db_TL_y.getNamePass(Integer.parseInt(userId));
                        String v_user= c.getString(c.getColumnIndex("KEY_USERNAME"));
                        String v_pass= c.getString(c.getColumnIndex("KEY_USERNAME"));
                        vault_screen.putExtra("username", v_user);
                        vault_screen.putExtra("pass", v_pass);
                        db_TL_y.close();
                        startActivity(vault_screen);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SingleWebsiteActivity.this, "Bruh!", Toast.LENGTH_LONG).show();

                        db_TL_y.close();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                db_TL_y.open();
                Intent vault_screen= new Intent(SingleWebsiteActivity.this, Vault.class);
                Cursor c = db_TL_y.getNamePass(Integer.parseInt(userId));
                String v_user= c.getString(c.getColumnIndex("KEY_USERNAME"));
                String v_pass= c.getString(c.getColumnIndex("KEY_USERNAME"));
                vault_screen.putExtra("username", v_user);
                vault_screen.putExtra("pass", v_pass);
                db_TL_y.close();
                startActivity(vault_screen);
                finish();
            }
        });
    }
}
