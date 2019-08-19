package p.vikpo.bylocktracker.login;

import java.util.Date;

public class User
{
    private String email, name;
    private Date sessionDate;

    public User()
    {

    }

    public User(String email, String name, Date sessionDate)
    {
        this.email = email;
        this.name = name;
        this.sessionDate = sessionDate;
    }


    public Date getSessionDate()
    {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate)
    {
        this.sessionDate = sessionDate;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
