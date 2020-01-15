package com.kosbaship.ecommerce.Model;
// (7 - A)
// ((7 - B) go and create Prevalent.java
// create this model
public class Users {
    //(14 - B - 3 - c) go back to SittingsActivity.java
    //(14 - B - 4) go back to SittingsActivity.java
    // add here the image and address

    // if this user exist in the data base
    // make sure to create these name like what in the database
    private String name, phone, password, image, address;

    // empty constructor
    public Users()
    {

    }

    // constructor with parameters
    public Users(String name, String phone, String password, String image, String address) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.address = address;
    }

    // setter and getter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
