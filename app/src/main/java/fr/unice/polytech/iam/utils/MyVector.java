package fr.unice.polytech.iam.utils;

import fr.unice.polytech.iam.contact.Contact;

import static fr.unice.polytech.iam.contact.Contact.ContactType;

public class MyVector {

    private int nbSms = 0;
    private int nbCalls = 0;
    private int cumulativeCallDuration = 0;
    private int averageCallDuration = 0;
    private boolean isWeekDay = false;
    private TimeInDay timeInDay = TimeInDay.OTHER;
    private ContactType contactType = ContactType.NONE;

    public MyVector() {}

    public MyVector(int nbSms, int nbCalls, int cumulativeCallDuration, int averageCallDuration,
                    boolean isWeekDay, TimeInDay timeInDay, Contact.ContactType contactType) {
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

    public int getAverageCallDuration() {
        return averageCallDuration;
    }

    public void setAverageCallDuration(int averageCallDuration) {
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
        StringBuilder sb = new StringBuilder();
        sb.append(nbSms);
        sb.append(" ");
        sb.append(nbCalls);
        sb.append(" ");
        sb.append(cumulativeCallDuration);
        sb.append(" ");
        sb.append(averageCallDuration);
        sb.append(" ");
        sb.append(isWeekDay ? 1 : 0);
        sb.append(" ");
        sb.append(timeInDay.name());
        sb.append(" ");
        sb.append(contactType.name());
        return sb.toString();
    }
}
