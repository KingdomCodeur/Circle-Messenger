package fr.unice.polytech.iam.contact;

import java.util.Date;

public class PhoneCall {

    private PhoneCallType type;
    private Date date;
    private int duration;


    public PhoneCall(PhoneCallType phoneCallType, Date callDate, int duration) {
        this.type = phoneCallType;
        this.date = callDate;
        this.duration = duration;
    }

    public PhoneCallType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    public enum PhoneCallType {
        OUTGOING,
        INCOMING,
        MISSED
    }
}
