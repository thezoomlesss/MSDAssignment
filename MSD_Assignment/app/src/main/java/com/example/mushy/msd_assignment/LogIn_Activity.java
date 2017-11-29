package com.example.mushy.msd_assignment;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn_Activity extends AppCompatActivity
{

    private TextView username, pass_field, go_signup;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_);

        username = (TextView) findViewById(R.id.usr_field);
        pass_field = (TextView) findViewById(R.id.psw_field);
        login = (Button) findViewById(R.id.login1);
        go_signup = (TextView) findViewById(R.id.go_login);

        // String containting the characters we don't want (SQL injection)
        final String not_allowed_chars= "\"$&*/;,.:'";

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // If textviews != empty
                if( !String.valueOf(username.getText()).equals("") &&  !String.valueOf(pass_field.getText()).equals("") )
                {
                    String user= String.valueOf(username.getText());
                    String pass= String.valueOf(pass_field.getText());

                    // Condition used for the while loop that checks for unwanted characters
                    boolean login_condition = true;

                    // Looping through each pos of the String and comparing with the unwanted character String
                    for(int i=0; i< user.length(); i++){
                        if( not_allowed_chars.contains(String.valueOf(user.charAt(i)))){
                            login_condition = false;
                            break;
                        }
                    }

                    for(int i=0; i< pass.length(); i++){
                        if( not_allowed_chars.contains(String.valueOf(pass.charAt(i)))){
                            login_condition = false;
                            break;
                        }
                    }

                    if(login_condition){
                        // condition true so add to db
                        DataTaskList dbTL_i;

                        try{
                            dbTL_i= new DataTaskList(LogIn_Activity.this);
                            dbTL_i.open();

                            // Sending the encrypted data to the vault
                            Crypt c= new Crypt();
                            Crypt_message crypted_user_pass= c.encrypt(user, pass);
                            Cursor login_pass = dbTL_i.loginUser(crypted_user_pass.getMsg1(), crypted_user_pass.getMsg2());

                            if(login_pass != null){
                                dbTL_i.close();
                                Intent vault_screen= new Intent(LogIn_Activity.this, Vault.class);
                                vault_screen.putExtra("username", crypted_user_pass.getMsg1());
                                vault_screen.putExtra("pass", crypted_user_pass.getMsg2());
                                startActivity(vault_screen);
                                finish();
                            }
                            else
                            {

                                dbTL_i.close();
                                Toast.makeText(LogIn_Activity.this, "Wrong Username and Password combination!", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        Toast.makeText(LogIn_Activity.this, "Your username/password contain a character than is now allowed", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(LogIn_Activity.this, "Please make sure fill in all the fields!", Toast.LENGTH_LONG).show();
                }


            }
        });

        go_signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent register_screen= new Intent(LogIn_Activity.this, SignUp_Activity.class);
                startActivity(register_screen);
                finish();
            }
        });

    }

}
