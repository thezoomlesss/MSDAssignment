package com.example.mushy.msd_assignment;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Vault extends ListActivity
{

    private String username, pass, userId;


    ArrayList<Web_class> websites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        //ListView list;
        Button back, add_website;
        Web_class single_website;

        back= (Button) findViewById(R.id.log_out_button);
        add_website = (Button) findViewById(R.id.add_website);

        // Passed values
        username= getIntent().getStringExtra("username");
        pass= getIntent().getStringExtra("pass");

        DataTaskList db_TL_o= new DataTaskList(this);
        try{db_TL_o.open();}
        catch (Exception e){
            e.printStackTrace();
        }
        Cursor c= db_TL_o.getUserId(username, pass);
        if(c != null && c.moveToFirst()){

            userId = c.getString(c.getColumnIndex("_id"));
        }

        // Getting the websites
        Cursor wCurs = db_TL_o.getWebsites(Integer.parseInt(userId));
        String wName, uwName, uwPass, uwNote;

        if(wCurs!=null && wCurs.moveToFirst()){
            db_TL_o.close();

            while (wCurs.moveToNext()) {

                wName = wCurs.getString(wCurs.getColumnIndex("KEY_WEBSITE"));
                uwName = wCurs.getString(wCurs.getColumnIndex("KEY_W_USERNAME"));
                uwPass = wCurs.getString(wCurs.getColumnIndex("KEY_W_PASS"));
                uwNote = wCurs.getString(wCurs.getColumnIndex("KEY_W_Notes"));
                single_website = new Web_class(Integer.parseInt(userId), wName, uwName, uwPass, uwNote);
                websites.add(single_website);

            }

        }
        else{
            db_TL_o.close();
        }



        setListAdapter(new WebsiteAdapter
                (this,
                        R.layout.row,
                        websites)
        );

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent main_screen= new Intent(Vault.this, MainActivity.class);
                startActivity(main_screen);
                finish();
            }
        });
        add_website.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent add_w= new Intent(Vault.this, AddWebsite_Activity.class);
                add_w.putExtra("userId", userId );
                startActivity(add_w);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        Intent I =new Intent(Vault.this, SingleWebsiteActivity.class);
        I.putExtra("websiteName", websites.get(position).getWebsiteName());
        I.putExtra("userName", websites.get(position).getUserName());
        I.putExtra("password", websites.get(position).getPassword());
        I.putExtra("note", websites.get(position).getNote());
        I.putExtra("userId", userId); // used for update/delete screens
        startActivity(I);
        finish();

    }

    public class WebsiteAdapter extends ArrayAdapter<String>
    {
        public WebsiteAdapter(Context context, int rowLayoutId, ArrayList myArrayData){
            super(context, rowLayoutId, myArrayData);
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent)
        {
            View row;
            LayoutInflater inflater= getLayoutInflater();
            row=inflater.inflate(R.layout.row, parent, false);


            TextView websiteName= (TextView) row.findViewById(R.id.websiteName);
            websiteName.setText(websites.get(position).getWebsiteName());

            return row;
        }

    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return pass;
    }
}
