package com.reactlibrary;

public class RudderTraitsBuilder {
    private String city;

    public RudderTraitsBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    private String country;

    public RudderTraitsBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    private String postalCode;

    public RudderTraitsBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    private String state;

    public RudderTraitsBuilder setState(String state) {
        this.state = state;
        return this;
    }

    private String street;

    public RudderTraitsBuilder setStreet(String street) {
        this.street = street;
        return this;
    }

    private String age;

    public RudderTraitsBuilder setAge(int age) {
        this.age = Integer.toString(age);
        return this;
    }

    private String birthDay;

    public RudderTraitsBuilder setBirthDay(String birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    private String companyName;

    public RudderTraitsBuilder setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    private String companyId;

    public RudderTraitsBuilder setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    private String industry;

    public RudderTraitsBuilder setIndustry(String industry) {
        this.industry = industry;
        return this;
    }

    private String createdAt;

    public RudderTraitsBuilder setCreateAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    private String description;

    public RudderTraitsBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    private String email;

    public RudderTraitsBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    private String firstName;

    public RudderTraitsBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    private String gender;

    public RudderTraitsBuilder setGender(String gender) {
        this.gender = gender;
        return this;
    }

    private String id;

    public RudderTraitsBuilder setId(String id) {
        this.id = id;
        return this;
    }

    private String lastName;

    public RudderTraitsBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    private String name;

    public RudderTraitsBuilder setName(String name) {
        this.name = name;
        return this;
    }

    private String phone;

    public RudderTraitsBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    private String title;

    public RudderTraitsBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    private String userName;

    public RudderTraitsBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RudderTraits build() {
        return new RudderTraits(
                new RudderTraits.Address(
                        this.city,
                        this.country,
                        this.postalCode,
                        this.state,
                        this.street
                ),
                this.age,
                this.birthDay,
                new RudderTraits.Company(
                        this.companyName,
                        this.companyId,
                        this.industry
                ),
                this.createdAt,
                this.description,
                this.email,
                this.firstName,
                this.gender,
                this.id,
                this.lastName,
                this.name,
                this.phone,
                this.title,
                this.userName
        );
    }
}
