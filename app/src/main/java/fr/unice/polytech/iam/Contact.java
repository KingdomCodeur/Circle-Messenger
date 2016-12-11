package fr.unice.polytech.iam;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    private String id;
    private String name;
    private List<ContactEmail> emails;
    private List<ContactPhone> numbers;

    public Contact(String contactId, String contactName) {
        this.id = contactId;
        this.name = contactName;
        this.emails = new ArrayList<>();
        this.numbers = new ArrayList<>();
    }

    public void addEmail(String address, String type) {
        emails.add(new ContactEmail(address, type));
    }

    public void addNumber(String number, String type) {
        numbers.add(new ContactPhone(number, type));
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
}
