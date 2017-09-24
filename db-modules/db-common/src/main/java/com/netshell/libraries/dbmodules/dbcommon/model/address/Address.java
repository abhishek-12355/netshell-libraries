package com.netshell.libraries.dbmodules.dbcommon.model.address;

import java.util.Optional;

/**
 * Created by Abhishek
 * on 5/23/2016.
 */
public class Address {
    private String houseNumber;
    private String addressLine1;
    private String addressLine2;
    private PinCode pinCode;
    private Country country;
    private State state;
    private City city;

    public Optional<String> getAddressLine1() {
        return Optional.ofNullable(addressLine1);
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public Optional<String> getAddressLine2() {
        return Optional.ofNullable(addressLine2);
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public Optional<City> getCity() {
        return Optional.ofNullable(city);
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Optional<Country> getCountry() {
        return Optional.ofNullable(country);
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Optional<String> getHouseNumber() {
        return Optional.ofNullable(houseNumber);
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Optional<PinCode> getPinCode() {
        return Optional.ofNullable(pinCode);
    }

    public void setPinCode(PinCode pinCode) {
        this.pinCode = pinCode;
    }

    public Optional<State> getState() {
        return Optional.ofNullable(state);
    }

    public void setState(State state) {
        this.state = state;
    }
}
