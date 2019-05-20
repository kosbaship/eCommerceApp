package com.kosbaship.ecommerce.Prevalent;

import com.kosbaship.ecommerce.Model.Users;
//                          ((7 - B)
// ((7 - C) Go back to LoginActivity.java
// create this Prevalent user as a pointer to the
// current logged in user
// and also this class will help us with {Forget Password, Remember Me}
public class Prevalent {
    // common data of the user
    public static Users currentOnlineUser;

    //                      (8 - B)
    // (8 - C) Go to LoginActivity.java
    // this variable to store The current logged user phone and password
    public static final String UserPhoneKey = "UserPhone";
    public static final String UserPasswordKey = "UserPassword";
}
