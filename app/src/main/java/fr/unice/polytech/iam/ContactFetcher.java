package fr.unice.polytech.iam;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                final String number = phone.getString(contactNumberColumnIndex);
                final String contactId = phone.getString(contactIdColumnIndex);
                Contact contact = contactsMap.get(contactId);

                if (null == contact) {
                    continue;
                }

                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = Phone.getTypeLabel(
                        context.getResources(),
                        type,
                        customLabel);
                contact.addNumber(number, phoneType.toString());
                phone.moveToNext();
            }
        }

        phone.close();
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
            numbers.add(c.getNumber());
        }
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = contentResolver.query(uriSMSURI, null, null, null, null);

        List<String> sms = new ArrayList<>();
        while(cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            if(numbers.contains(address)) {
                String body = cur.getString(cur.getColumnIndexOrThrow("body"));
                Log.w("DEBUGSMS : ", "Number: " + address + " .Message : " + body); // on affiche tous les sms dans le debug
                sms.add("Number: " + address + " .Message : " + body);
            }
        }

        cur.close();

        return sms;
    }

}
