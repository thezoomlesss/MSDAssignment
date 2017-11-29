package com.example.mushy.msd_assignment;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class SignUp_Activity extends AppCompatActivity
{
    private TextView username, password, go_login;
    private Button register;
    //private NfcAdapter nfcA;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        username = (TextView) findViewById(R.id.usr);
        password = (TextView) findViewById(R.id.psw);
        register = (Button) findViewById(R.id.signup);
        go_login = (TextView) findViewById(R.id.go_login);

        // String used to compare input from the user
        final String not_allowed_chars= "\"$&*/;,.:'";

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // Textviews not empty
                if( !String.valueOf(username.getText()).equals("") &&  !String.valueOf(password.getText()).equals("")  )
                {
                    // password not too short/long
                    if( password.getText().length() > 6 && password.getText().length() < 25 )
                    {
                        // Condition used for the while loop that checks for SQL injection
                        boolean register_condition = true;
                        String user= String.valueOf(username.getText());
                        String pass= String.valueOf(password.getText());

                        for(int i=0; i< user.length(); i++){
                            if( not_allowed_chars.contains(String.valueOf(user.charAt(i))) ){
                                register_condition = false;
                                break;
                            }
                        }

                        for(int i=0; i< pass.length(); i++){
                            if( not_allowed_chars.contains(String.valueOf(pass.charAt(i))) ){
                                register_condition = false;
                                break;
                            }
                        }

                        if(register_condition){
                            // true so add to db
                            DataTaskList dbTL_i;

                            try{
                                dbTL_i= new DataTaskList(SignUp_Activity.this);
                                dbTL_i.open();

                                // Encrypting the data
                                // msg1 is user
                                // msg2 is pass
                                Crypt crypt = new Crypt();
                                Crypt_message crypted_user_pass = crypt.encrypt(user, pass);

                                // Inserting user into the database
                                // "1", "1", "1" for the missing extra features :( sorry
                                dbTL_i.insertUser(crypted_user_pass.getMsg1(), crypted_user_pass.getMsg2(), "1", "1", "1");

                                // Getting the newely generated userId from the db
                                Cursor c = dbTL_i.getUserId(crypted_user_pass.getMsg1(), crypted_user_pass.getMsg2());
                                int userId= Integer.parseInt(c.getString(c.getColumnIndex("_id")));

                                // Adding the key into the table used for crypting/decrypting
                                dbTL_i.insertKey(userId ,String.valueOf(crypted_user_pass.getKey()), String.valueOf(crypted_user_pass.getKey()));
                                dbTL_i.close();

                                /*
                                    Going to the Vault screen and passing the crypted username and password as the login credentials
                                */
                                Intent vault_screen= new Intent(SignUp_Activity.this, Vault.class);
                                vault_screen.putExtra("username", crypted_user_pass.getMsg1());
                                vault_screen.putExtra("pass", crypted_user_pass.getMsg2());
                                startActivity(vault_screen);
                                finish();

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(SignUp_Activity.this, "Your username/password contain a character than is now allowed", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SignUp_Activity.this, "Your password has to be at least 6 characters short and no longer than 25 characters long!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(SignUp_Activity.this, "Please make sure fill in all the fields!", Toast.LENGTH_LONG).show();
                }


            }
        });

        go_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent register_screen= new Intent(SignUp_Activity.this, LogIn_Activity.class);
                startActivity(register_screen);
                finish();
            }
        });


    }
}
