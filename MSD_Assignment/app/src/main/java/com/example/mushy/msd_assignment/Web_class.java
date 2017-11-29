package com.example.mushy.msd_assignment;

/**
 * Created by Mushy on 25-Nov-17.
 */

public class Web_class
{
    private String websiteName, userName, password, note;


    public Web_class(int userID, String websiteName, String userName, String password, String note)
    {
        this.websiteName = websiteName;
        this.userName = userName;
        this.password = password;
        this.note = note;
    }

    public String getWebsiteName()
    {
        return websiteName;
    }

    public void setWebsiteName(String websiteName)
    {
        this.websiteName = websiteName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }
}
