package com.example.mushy.msd_assignment;

/*
    Simple class so we can pass the data as a whole

 */
public class Crypt_message{
    private int key;
    private String msg1, msg2;

    public Crypt_message(int key, String msg1, String msg2)
    {
        this.key = key;
        this.msg1 = msg1;
        this.msg2 = msg2;
    }

    public int getKey()
    {
        return key;
    }

    public void setKey(int key)
    {
        this.key = key;
    }

    public String getMsg1()
    {
        return msg1;
    }

    public void setMsg1(String msg1)
    {
        this.msg1 = msg1;
    }

    public String getMsg2()
    {
        return msg2;
    }

    public void setMsg2(String msg2)
    {
        this.msg2 = msg2;
    }
}
