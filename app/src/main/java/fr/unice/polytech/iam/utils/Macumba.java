package fr.unice.polytech.iam.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.unice.polytech.iam.contact.Communication;
import fr.unice.polytech.iam.contact.Contact;
import fr.unice.polytech.iam.contact.Contact.ContactType;
import fr.unice.polytech.iam.contact.PhoneCall;
import fr.unice.polytech.iam.contact.Sms;

public class Macumba {

    public static List<MyVector> createVectors(Contact contact) {
        List<Communication> communicationList = new ArrayList<>();
        communicationList.addAll(contact.getSMS());
        communicationList.addAll(contact.getPhoneCalls());

        Collections.sort(communicationList, new Comparator<Communication>() {
            @Override
            public int compare(Communication o1, Communication o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        int start = 0;
        int end = 0;

        List<MyVector> vectors = new ArrayList<>();

        while (end < communicationList.size()) {
            end = getIndexLastElementSameHour(start, communicationList);
            vectors.add(createVector(contact.getContactType(), communicationList, start, end));
        }

        return vectors;
    }

    private static int getIndexLastElementSameHour(int index, List<Communication> communications) {
        int i = index;

        Communication com = communications.get(i);

        while (i <communications.size() - 1 && isSameHour(com.getDate(), communications.get(i + 1).getDate())) {
            i++;
        }

        return i;
    }

    private static MyVector createVector(ContactType contactType, List<Communication> communications, int start, int end) {
        MyVector vector = new MyVector();
        vector.setContactType(contactType);

        int nbSms = 0;
        int nbCalls = 0;
        int cumulativeCallDuration = 0;
        int averageCallDuration = 0;
        boolean isWeekDay;
        TimeInDay timeInDay;
        Calendar c = communications.get(start).getDate();


        for (int i = start; i <= end; i++) {
            if (communications.get(i) instanceof Sms) {
                nbSms++;
            }
            else if (communications.get(i) instanceof PhoneCall) {
                PhoneCall pc = (PhoneCall) communications.get(i);
                nbCalls++;
                cumulativeCallDuration += pc.getDuration();
            }
        }

        isWeekDay = isWeekDay(c.get(Calendar.DAY_OF_WEEK));
        averageCallDuration = cumulativeCallDuration / nbCalls;
        timeInDay = whatTimeIsIt(c.get(Calendar.HOUR_OF_DAY));

        vector = new MyVector(nbSms, nbCalls, cumulativeCallDuration, averageCallDuration, isWeekDay,
                            timeInDay, contactType);

        return vector;
    }

    private static boolean isSameHour(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c1.get(Calendar.YEAR)
                || c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                || c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
                || c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY);
    }

    private static boolean isWeekDay(int dayOfWeek) {
        return (dayOfWeek > 1 && dayOfWeek < 7);
    }

    private static TimeInDay whatTimeIsIt(int hour) {
        if (hour >= 11 && hour < 14) {
            return TimeInDay.MIDDAY;
        }
        else if (hour > 8 && hour < 18) {
            return TimeInDay.WORK_HOUR;
        }
        else {
            return TimeInDay.OTHER;
        }
    }

}
