package com.kosbaship.ecommerce.Model;
// (16 - B - 2)
// (16 - B - 3) Go Back to CartActivity.java
// create this model class and assign the variable needed for every
// single item in the cart list
public class Cart {
    // make sure to match these name with the name the user
    // stored in the database you can go and loo for it
    private String pid, pname, price, quantity, discount;

    public Cart() {
    }

    public Cart(String pid, String pname, String price, String quantity, String discount) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
