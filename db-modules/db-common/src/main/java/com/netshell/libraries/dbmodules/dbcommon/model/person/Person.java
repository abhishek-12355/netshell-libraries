package com.netshell.libraries.dbmodules.dbcommon.model.person;

import com.netshell.libraries.dbmodules.dbcommon.model.address.Address;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Abhishek
 * on 5/22/2016.
 */
public class Person {

    private String id;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dateOfBirth;
    private Sex sex;
    private List<Address> addressList = new ArrayList<>();
    private List<Email> emailList = new ArrayList<>();
    private List<Mobile> mobileList = new ArrayList<>();

    public Optional<String> getMiddleName() {
        return Optional.ofNullable(middleName);
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public List<Email> getEmailList() {
        return emailList;
    }

    public List<Mobile> getMobileList() {
        return mobileList;
    }

    public Optional<Date> getDateOfBirth() {
        return Optional.ofNullable(dateOfBirth);
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Optional<Sex> getSex() {
        return Optional.ofNullable(sex);
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
