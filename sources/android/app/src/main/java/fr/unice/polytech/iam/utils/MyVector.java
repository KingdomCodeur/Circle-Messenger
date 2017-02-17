package fr.unice.polytech.iam.utils;

import org.json.JSONArray;
import org.json.JSONException;

import fr.unice.polytech.iam.contact.Contact;

import static fr.unice.polytech.iam.contact.Contact.ContactType;

public class MyVector {

    private int nbSms = 0;
    private int nbCalls = 0;
    private int cumulativeCallDuration = 0;
    private float averageCallDuration = 0;
    private boolean isWeekDay = false;
    private TimeInDay timeInDay = TimeInDay.OTHER;
    private ContactType contactType = ContactType.NONE;

    public MyVector() {}

    public MyVector(int nbSms, int nbCalls, int cumulativeCallDuration, float averageCallDuration,
                    boolean isWeekDay, TimeInDay timeInDay, ContactType contactType) {
        this.setNbSms(nbSms);
        this.setNbCalls(nbCalls);
        this.setCumulativeCallDuration(cumulativeCallDuration);
        this.setAverageCallDuration(averageCallDuration);
        this.setWeekDay(isWeekDay);
        this.setTimeInDay(timeInDay);
        this.setContactType(contactType);
    }

    public int getNbSms() {
        return nbSms;
    }

    public void setNbSms(int nbSms) {
        this.nbSms = nbSms;
    }

    public int getNbCalls() {
        return nbCalls;
    }

    public void setNbCalls(int nbCalls) {
        this.nbCalls = nbCalls;
    }

    public int getCumulativeCallDuration() {
        return cumulativeCallDuration;
    }

    public void setCumulativeCallDuration(int cumulativeCallDuration) {
        this.cumulativeCallDuration = cumulativeCallDuration;
    }

    public float getAverageCallDuration() {
        return averageCallDuration;
    }

    public void setAverageCallDuration(float averageCallDuration) {
        this.averageCallDuration = averageCallDuration;
    }

    public boolean isWeekDay() {
        return isWeekDay;
    }

    public void setWeekDay(boolean weekDay) {
        isWeekDay = weekDay;
    }

    public TimeInDay getTimeInDay() {
        return timeInDay;
    }

    public void setTimeInDay(TimeInDay timeInDay) {
        this.timeInDay = timeInDay;
    }

    public Contact.ContactType getContactType() {
        return contactType;
    }

    public void setContactType(Contact.ContactType contactType) {
        this.contactType = contactType;
    }

    public String toString() {
        String separator = ",";
        StringBuilder sb = new StringBuilder();
 /*       sb.append(nbSms);
        sb.append(separator);
        sb.append(nbCalls);
        sb.append(separator);
        sb.append(cumulativeCallDuration);
        sb.append(separator);
        sb.append(averageCallDuration);
        sb.append(separator);
        sb.append(isWeekDay ? 1 : 0);
        sb.append(separator);
        sb.append(timeInDay.ordinal());
        sb.append(separator);
        sb.append(contactType.name());*/
        sb.append(toStringWithoutType());
        sb.append(separator);
        sb.append(contactType.name());
        return sb.toString();
    }

    public String toStringWithoutType() {
        String separator = ",";
        StringBuilder sb = new StringBuilder();
        sb.append(nbSms);
        sb.append(separator);
        sb.append(nbCalls);
        sb.append(separator);
        sb.append(cumulativeCallDuration);
        sb.append(separator);
        sb.append(averageCallDuration);
        sb.append(separator);
        sb.append(isWeekDay ? 1 : 0);
        sb.append(separator);
        sb.append(timeInDay.ordinal());
        return sb.toString();
    }

    public JSONArray toJSONArrayWithoutType() {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(nbSms);
            jsonArray.put(nbCalls);
            jsonArray.put(cumulativeCallDuration);
            jsonArray.put(averageCallDuration);
            jsonArray.put(isWeekDay ? 1 : 0);
            jsonArray.put(timeInDay.ordinal());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
