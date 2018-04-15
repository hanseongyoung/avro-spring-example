package syhan.avro.client.rest;

public class Address {
    private String zipCode;
    private String street;
    private String address;

    public Address() {
    }

    public Address(String zipCode, String street, String address) {
        this.zipCode = zipCode;
        this.street = street;
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
