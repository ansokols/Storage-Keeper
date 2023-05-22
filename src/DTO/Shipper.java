package DTO;

public class Shipper {
    private Integer shipperId;
    private String name;
    private String address;
    private String phoneNumber;
    private String contactPerson;

    public Shipper(Integer shipperId, String name, String address, String phoneNumber, String contactPerson) {
        this.shipperId = shipperId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.contactPerson = contactPerson;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public String toString()  {
        return getName();
    }
}
