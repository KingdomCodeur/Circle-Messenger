package fr.unice.polytech.iam.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static String ipServer = "http://colombet-aoechat.rhcloud.com/";

    public static List<MyVector> createVectors(Contact contact) {
        List<Communication> communicationList = new ArrayList<>();
        communicationList.addAll(contact.getSMS());
        communicationList.addAll(contact.getPhoneCalls());
        List<MyVector> vectors = new ArrayList<>();



        Collections.sort(communicationList, new Comparator<Communication>() {
            @Override
            public int compare(Communication o1, Communication o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        int start = 0;
        int end = 0;

        Log.w("Macumba :", "List size = " + communicationList.size());

        while (start < communicationList.size()) {
            end = getIndexLastElementSameHour(start, communicationList);
            vectors.add(createVector(contact.getContactType(), communicationList, start, end));
            start = end + 1;
        }
/*
*/
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
        float averageCallDuration = 0;
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
        averageCallDuration = nbCalls > 0 ? (float) cumulativeCallDuration / nbCalls : 3;
        timeInDay = whatTimeIsIt(c.get(Calendar.HOUR_OF_DAY));

        vector = new MyVector(nbSms, nbCalls, cumulativeCallDuration, averageCallDuration, isWeekDay,
                            timeInDay, contactType);

        return vector;
    }

    private static boolean isSameHour(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
                && c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY);
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

    public static void writeData(Context context, String filename, String data) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            Log.w("FILE!!!!", "Written: " + data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readData(Context context, String filename) {
        String message = "";
        File file = new File(context.getFilesDir() + "/" + filename);
        Log.w("FILE!!!!", "Read: file.length: " + file.length());

        byte[] bytes = new byte[(int) file.length()];
        try {
            FileInputStream fis = context.openFileInput(filename);
            fis.read(bytes);
            fis.close();
            message = new String(bytes);
            Log.w("FILE!!!!", "Read: " + message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static void setContactTypeFromJSONArray(List<Contact> contacts, JSONArray jsonArray) {
        try {
            List<JSONObject> jsonObjects = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjects.add(jsonArray.getJSONObject(i));
            }

            for (JSONObject jsonObject : jsonObjects) {
                String num = jsonObject.getString("num");
                String type = jsonObject.getString("type");
                for (Contact contact : contacts) {
                    if (contact.hasNumber(num)) {
                        if (type.equals(ContactType.AMI.name())) {
                            contact.setContactType(ContactType.AMI);
                        } else if (type.equals(ContactType.FAMILLE.name())) {
                            contact.setContactType(ContactType.FAMILLE);
                        } else if (type.equals(ContactType.COLLEGUE.name())) {
                            contact.setContactType(ContactType.COLLEGUE);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setContactTypeFile(Context context, List<Contact> contacts) {
        String data = readData(context, "circle messenger");
        try {
            JSONArray jsonArray = new JSONArray(data);
            setContactTypeFromJSONArray(contacts, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
