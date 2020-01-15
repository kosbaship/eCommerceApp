package com.kosbaship.ecommerce.Model;


//(18 - C - 3 - a)
//(18 - C - 3 - b) go back to AdminNewOrdersActivity.java
// create this model class for the orders node
// we will get the data from the server and store it here in these variables
// then we will use this class method to get the data and user it inside the app
// then reassign the variables again will the next order and this is the concept
// the variable function itself it will erase the last value and put all the new data
// (the recycler view adaptor who will regulate this this whole process order by order)

public class AdminOrders {

    // make sure to match these name with the name the user
    // stored in the database you can go and loo for it
    private String name, phone, address, city, state, date, time, totalAmount;

    public AdminOrders() {
    }

    public AdminOrders(String name, String phone, String address, String city, String state, String date, String time, String totalAmount) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.date = date;
        this.time = time;
        this.totalAmount = totalAmount;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
