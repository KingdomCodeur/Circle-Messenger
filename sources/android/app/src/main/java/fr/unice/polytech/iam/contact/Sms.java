package fr.unice.polytech.iam.contact;

import java.util.Calendar;

public class Sms implements Communication {
    private String body;
    private Calendar date;

    public Sms(String body, Calendar date){
        this.body = body;
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public Calendar getDate() {
        return date;
    }
}
