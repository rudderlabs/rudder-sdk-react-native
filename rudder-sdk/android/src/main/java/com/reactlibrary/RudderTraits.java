package com.reactlibrary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rudderlabs.android.sdk.core.util.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RudderTraits {
    @SerializedName("anonymousId")
    private String anonymousId;
    @SerializedName("address")
    private Address address;
    @SerializedName("age")
    private String age;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("company")
    private Company company;
    @SerializedName("createdat")
    private String createdAt;
    @SerializedName("description")
    private String description;
    @SerializedName("email")
    private String email;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("gender")
    private String gender;
    @SerializedName("id")
    private String id;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("title")
    private String title;
    @SerializedName("username")
    private String userName;
    @Expose(serialize = false)
    private transient Map<String, Object> extras;

    public RudderTraits() {
        RudderContext rudderContext = RudderElementCache.getCachedContext();
        if (rudderContext != null) this.anonymousId = rudderContext.getDeviceId();
    }

    RudderTraits(String anonymousId) {
        this.anonymousId = anonymousId;
    }

    public RudderTraits(Address address, String age, String birthday, Company company, String createdAt, String description, String email, String firstName, String gender, String id, String lastName, String name, String phone, String title, String userName) {
        this.address = address;
        this.age = age;
        this.birthday = birthday;
        this.company = company;
        this.createdAt = createdAt;
        this.description = description;
        this.email = email;
        this.firstName = firstName;
        this.gender = gender;
        this.id = id;
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
        this.title = title;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void putAddress(Address address) {
        this.address = address;
    }

    public void putAge(String age) {
        this.age = age;
    }

    public void putBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void putBirthday(Date birthday) {
        this.birthday = Utils.toDateString(birthday);
    }

    public void putCompany(Company company) {
        this.company = company;
    }

    public void putCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void putDescription(String description) {
        this.description = description;
    }

    public void putEmail(String email) {
        this.email = email;
    }

    public void putFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void putGender(String gender) {
        this.gender = gender;
    }

    public void putId(String id) {
        this.id = id;
    }

    public void putLastName(String lastName) {
        this.lastName = lastName;
    }

    public void putName(String name) {
        this.name = name;
    }

    public void putPhone(String phone) {
        this.phone = phone;
    }

    public void putTitle(String title) {
        this.title = title;
    }

    public void putUserName(String userName) {
        this.userName = userName;
    }

    public void put(String key, Object value) {
        if (this.extras == null) {
            this.extras = new HashMap<>();
        }
        this.extras.put(key, value);
    }

    public static class Address {
        @SerializedName("city")
        private String city;
        @SerializedName("country")
        private String country;
        @SerializedName("postalcode")
        private String postalCode;
        @SerializedName("state")
        private String state;
        @SerializedName("street")
        private String street;

        public Address() {
        }

        public void putCity(String city) {
            this.city = city;
        }

        public void putCountry(String country) {
            this.country = country;
        }

        public void putPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public void putState(String state) {
            this.state = state;
        }

        public void putStreet(String street) {
            this.street = street;
        }

        Address(String city, String country, String postalCode, String state, String street) {
            this.city = city;
            this.country = country;
            this.postalCode = postalCode;
            this.state = state;
            this.street = street;
        }
    }

    public static class Company {
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private String id;
        @SerializedName("industry")
        private String industry;

        Company(String name, String id, String industry) {
            this.name = name;
            this.id = id;
            this.industry = industry;
        }
    }
}
