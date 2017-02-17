package fr.unice.polytech.iam.contact;

public class ContactPhone {

    private String number;
    private String type;

    public ContactPhone(String number, String type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }
}
