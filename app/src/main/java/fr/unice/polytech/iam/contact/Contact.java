package fr.unice.polytech.iam.contact;

import java.util.ArrayList;
import java.util.List;

public class Contact implements Comparable {

    private final String id;
    private final String name;
    private List<ContactEmail> emails;
    private List<ContactPhone> numbers;
    private List<Sms> sms;
    private List<PhoneCall> calls;

    public Contact(String contactId, String contactName) {
        this.id = contactId;
        this.name = contactName;
        this.emails = new ArrayList<>();
        this.numbers = new ArrayList<>();
        this.sms = new ArrayList<>();
        this.calls = new ArrayList<>();
    }

    public void addEmail(String address, String type) {
        emails.add(new ContactEmail(address, type));
    }

    public void addNumber(String number, String type) {
        numbers.add(new ContactPhone(number, type));
    }

    public void addSms(List<Sms> s){
        sms = s;
    }

    public void addPhoneCall(PhoneCall call) {
        calls.add(call);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ContactEmail> getEmails() {
        return emails;
    }

    public List<ContactPhone> getNumbers() {
        return numbers;
    }

    public List<Sms> getSMS(){return sms;}

    public List<PhoneCall> getPhoneCalls() {
        return calls;
    }

    public boolean hasANumber() {
        return numbers.size() > 0;
    }

    public boolean hasData() {
        return hasANumber() && sms.size() > 0 || calls.size() > 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (numbers.size() > 0) {
            ContactPhone number = numbers.get(0);
            result.append(" ( ");
            result.append(number.getNumber());
            result.append(" - ");
            result.append(number.getType());
            result.append(" ) ");
        }

        if (emails.size() > 0) {
            ContactEmail email = emails.get(0);
            result.append(" ( ");
            result.append(email.getAddress());
            result.append(" - ");
            result.append(email.getType());
            result.append(" ) ");
        }

        return result.toString();
    }

    @Override
    public int compareTo(Object another) {
        if (null == another) {
            return 1;
        }
        return name.compareTo(((Contact) another).getName());
    }
}
