package fr.unice.polytech.iam;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unice.polytech.iam.contact.Contact;
import fr.unice.polytech.iam.contact.ContactPhone;
import fr.unice.polytech.iam.contact.PhoneCall;

public class ContactFetcher {

    private final Context context;
    private ContentResolver contentResolver;

    public ContactFetcher(Context c,ContentResolver cr) {
        this.context = c;
        this.contentResolver = cr;
    }

    public List<Contact> fetchAll() {
        String[] projectionFields = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };
        List<Contact> listContacts = new ArrayList<>();
        CursorLoader cursorLoader = new CursorLoader(context,
                ContactsContract.Contacts.CONTENT_URI,
                projectionFields,   // the columns to retrieve
                null,               // the selection criteria (none)
                null,               // the selection args (none)
                null);              // the sort order (default)

        Cursor c = cursorLoader.loadInBackground();

        final Map<String, Contact> contactsMap = new HashMap<>(c.getCount());

        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            do {
                String contactId = c.getString(idIndex);
                String contactDisplayName = c.getString(nameIndex);
                Contact contact = new Contact(contactId, contactDisplayName);
                contactsMap.put(contactId, contact);
                listContacts.add(contact);
            } while (c.moveToNext());
        }

        c.close();

        matchContactNumbers(contactsMap);
        matchContactSms(contactsMap);
        matchContactEmails(contactsMap);
        matchContactPhoneCalls(contactsMap);

        return listContacts;
    }

    private void matchContactNumbers(Map<String, Contact> contactsMap) {
        // Get numbers
        final String[] numberProjection = new String[] {
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

        Cursor phone = new CursorLoader(context,
                Phone.CONTENT_URI,
                numberProjection,
                null,
                null,
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(Phone.CONTACT_ID);

            while (!phone.isAfterLast()) {
                String number = phone.getString(contactNumberColumnIndex);
                final String contactId = phone.getString(contactIdColumnIndex);
                Contact contact = contactsMap.get(contactId);

                if (null == contact) {
                    continue;
                }

                String c = number.concat("");

                c = c.replace(" ", "");
                c = c.replace("-", "");

                if (c.charAt(0) == '0') {
                    c = c.substring(1);
                    c = "+33".concat(c);
                }

                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = Phone.getTypeLabel(
                        context.getResources(),
                        type,
                        customLabel);
                contact.addNumber(c, phoneType.toString());
                phone.moveToNext();
            }
        }

        phone.close();
    }

    private void matchContactPhoneCalls(Map<String, Contact> contactsMap) {
        // Get phone history
        final String[] phoneCallProjection = new String[] {
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
        };

        Cursor call = new CursorLoader(context,
                CallLog.Calls.CONTENT_URI,
                phoneCallProjection,
                null,
                null,
                null).loadInBackground();

        if (call.moveToFirst()) {
            final int callNumberColumnIndex = call.getColumnIndex(CallLog.Calls.NUMBER);
            final int callTypeColumnIndex = call.getColumnIndex(CallLog.Calls.TYPE);
            final int callDateColumnIndex = call.getColumnIndex(CallLog.Calls.DATE);
            final int callDurationColumnIndex = call.getColumnIndex(CallLog.Calls.DURATION);

            while (!call.isAfterLast()) {
                final String number = call.getString(callNumberColumnIndex);
                final String type = call.getString(callTypeColumnIndex);
                final String date = call.getString(callDateColumnIndex);
                final Date callDate = new Date(Long.valueOf(date));
                final String callDuration = call.getString(callDurationColumnIndex);
                final int duration = Integer.parseInt(callDuration);

                Contact contact = null;

                for (Contact aContact : contactsMap.values()) {
                    for (ContactPhone phone : aContact.getNumbers()) {
                        if (number.equals(phone.getNumber())) {
                            contact = aContact;
                            break;
                        }
                        if (null != contact) {
                            break;
                        }
                    }
                }

                if (null == contact) {
                    call.moveToNext();
                    continue;
                } else {
                    Log.w("PhoneCall : ", contact.getName());
                }

                PhoneCall.PhoneCallType phoneCallType = null;
                switch (Integer.parseInt(type)) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        phoneCallType = PhoneCall.PhoneCallType.OUTGOING;
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        phoneCallType = PhoneCall.PhoneCallType.INCOMING;
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        phoneCallType = PhoneCall.PhoneCallType.MISSED;
                        break;
                }

                PhoneCall phoneCall = new PhoneCall(phoneCallType, callDate, duration);
                contact.addPhoneCall(phoneCall);

                call.moveToNext();
            }
        }

        call.close();
    }

    private void matchContactEmails(Map<String, Contact> contactsMap) {
        // Get email
        final String[] emailProjection = new String[] {
                Email.DATA,
                Email.TYPE,
                Email.CONTACT_ID,
        };

        Cursor email = new CursorLoader(context,
                Email.CONTENT_URI,
                emailProjection,
                null,
                null,
                null).loadInBackground();

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);
            final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);
            final int contactIdColumnsIndex = email.getColumnIndex(Email.CONTACT_ID);

            while (!email.isAfterLast()) {
                final String address = email.getString(contactEmailColumnIndex);
                final String contactId = email.getString(contactIdColumnsIndex);
                final int type = email.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                Contact contact = contactsMap.get(contactId);

                if (contact == null) {
                    continue;
                }

                CharSequence emailType = ContactsContract.CommonDataKinds.Email.getTypeLabel(
                        context.getResources(),
                        type,
                        customLabel);
                contact.addEmail(address, emailType.toString());
                email.moveToNext();
            }
        }

        email.close();
    }

    private void matchContactSms(Map<String, Contact> contactsMap) {
        for(Map.Entry<String, Contact> entry : contactsMap.entrySet()){
            entry.getValue().addSms(getAllSms(entry.getValue().getNumbers()));
        }
    }

    private List<String> getAllSms(List<ContactPhone> cp){
        List<String> numbers = new ArrayList<>();
        for(ContactPhone c : cp){
            String traitementPhone = c.getNumber();

            traitementPhone.replace(" ",""); //pour traiter le cas de : +33 6 19 62 08 41
            traitementPhone.replace("-",""); // pour traiter le cas de : 063-099-1237

            if(traitementPhone.charAt(0) == '0'){ // pour remplacer le premier 0 par +33
                traitementPhone = traitementPhone.substring(1);
                traitementPhone = "+33"+traitementPhone;
            }

            numbers.add(traitementPhone);
        }
        Uri uriSMSURI = Uri.parse("content://sms/");
        Cursor cur = contentResolver.query(uriSMSURI, null, null, null, null);

        List<String> sms = new ArrayList<>();
        while(cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
//            Log.w("DEBUGTEL : ", numbers.toString());
            address = convertToPlusFormat(address);
            if(numbers.contains(address)) {
                String body = cur.getString(cur.getColumnIndexOrThrow("body"));
//                Log.w("DEBUGSMS : ", "Number: " + address + " .Message : " + body); // on affiche tous les sms dans le debug
//                Log.w("DEBUGTEL : ", address);
                String stamp = cur.getString(cur.getColumnIndex("date"));
                long timestamplong = Long.parseLong(stamp);
                Date d = new Date(timestamplong);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                Log.w("affichage date ", c.toString());
                //Log.w("DEBUG TIMESTAMP", cur.getString(cur.getColumnIndex("date")));
                //sms.add("Number: " + address + " .Message : " + body);
                sms.add(body);
            }
        }

        cur.close();
//
//        uriSMSURI = Uri.parse("content://sms/sent");
//        cur = contentResolver.query(uriSMSURI, null, null, null, null);
//
//        while(cur.moveToNext()) {
//            String address = cur.getString(cur.getColumnIndex("address"));
////            Log.w("DEBUGTEL : ", numbers.toString());
//            /*if(address.contains("783632")){
//                Log.w("SALMERON",address);
//            }*/
//            address = convertToPlusFormat(address);
//            if(numbers.contains(address)) {
//                String body = cur.getString(cur.getColumnIndexOrThrow("body"));
//                //Log.w("DEBUGSMS SENT : ", "Number: " + address + " .Message : " + body); // on affiche tous les sms dans le debug
//                //Log.w("DEBUGTEL SENT : ", address);
//                //sms.add("Number: " + address + " .Message : " + body);
//                sms.add(body);
//            }
//        }
//
//        cur.close();

        return sms;
    }

    private String convertToPlusFormat(String number){
        number.replace(" ",""); //pour traiter le cas de : +33 6 19 62 08 41
        number.replace("-",""); // pour traiter le cas de : 063-099-1237

        if(number.charAt(0) == '0'){ // pour remplacer le premier 0 par +33
            number = number.substring(1);
            number = "+33"+number;
        }
        return number;
    }

}
