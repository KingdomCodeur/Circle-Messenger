package fr.unice.polytech.iam;

public class ContactEmail {

    private String address;
    private String type;

    public ContactEmail(String address, String type) {
        this.address = address;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }
}
