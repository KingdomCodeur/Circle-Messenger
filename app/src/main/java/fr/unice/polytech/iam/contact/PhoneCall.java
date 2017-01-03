package fr.unice.polytech.iam.contact;

import java.util.Calendar;

public class PhoneCall {

    private final PhoneCallType type;
    private final Calendar date;
    private final int duration;


    public PhoneCall(PhoneCallType phoneCallType, Calendar callDate, int duration) {
        this.type = phoneCallType;
        this.date = callDate;
        this.duration = duration;
    }

    public PhoneCallType getType() {
        return type;
    }

    public Calendar getDate() {
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
