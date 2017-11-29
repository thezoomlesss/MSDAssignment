package com.example.mushy.msd_assignment;

/**
 * This class will hold the two functions which will encrypt and decrypt data
 */

public class Crypt
{



    // Decrypting is not actually being used here
    // Initially it was used then it became obsolete
    // Keeping it for the code
    public Crypt_message decrypt(String a, String b, int c){

        Crypt_message d = new Crypt_message(c, a, b);

        int cta;
        String decrypted_msg1="";
        String decrypted_msg2="";

        for(int i=0; i < d.getMsg1().length(); i++)
        {
            // getting the ascii code of the character
            cta= (int) d.getMsg1().charAt(i);
            // getting the ascii of the crypted char
            cta-= d.getKey();
            // Adding it to the final string
            decrypted_msg1 += (char) cta;
        }
        for(int i=0; i < d.getMsg2().length(); i++)
        {
            // getting the ascii code of the character
            cta= (int) d.getMsg2().charAt(i);
            // getting the ascii of the crypted char
            cta-= d.getKey();
            // Adding it to the final string
            decrypted_msg2 += (char) cta;
        }

        return d;
    }
    public Crypt_message encrypt(String a, String b){

        // I removed the random key because it had few problems
        int key = 3;
        Crypt_message d = new Crypt_message(key, a, b);
        int cta;
        String crypted_msg1= "";
        String crypted_msg2= "";

        for(int i=0; i < d.getMsg1().length(); i++)
        {
            // getting the ascii code of the character
            cta= (int) d.getMsg1().charAt(i);
            // getting the ascii of the crypted char
            cta+= d.getKey();
            // Adding it to the final string
            crypted_msg1 += (char) cta;
        }
        for(int i=0; i < d.getMsg2().length(); i++)
        {
            // getting the ascii code of the character
            cta= (int) d.getMsg2().charAt(i);
            // getting the ascii of the crypted char
            cta+= d.getKey();
            // Adding it to the final string
            crypted_msg2 += (char) cta;
        }
        d.setMsg1(crypted_msg1);
        d.setMsg2(crypted_msg2);

        return d;
    }



}
