package app.model;

import org.bson.types.ObjectId;

import java.util.Objects;

/**
 * Address of company, pickup, delivery etc.
 * Used for embedding in other classes.
 */
public class Address {
    public ObjectId _id;
    private String country;
    private String city;
    private String postalCode;
    private String street;

    // for MongoDB serializer
    public Address() {}

    public Address(String country, String city, String postalCode, String street) {
        this._id = new ObjectId();
        this.setCountry(country);
        this.setCity(city);
        this.setPostalCode(postalCode);
        this.setStreet(street);
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Address address = (Address) o;
        return get_id().equals(address.get_id()) &&
                Objects.equals(getCountry(), address.getCountry()) &&
                Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getPostalCode(), address.getPostalCode()) &&
                Objects.equals(getStreet(), address.getStreet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id(), getCountry(), getCity(), getPostalCode(), getStreet());
    }

    @Override
    public String toString() {
        return country + ", " +
                city + " " +
                postalCode + ", " +
                street + " ";
    }
}
