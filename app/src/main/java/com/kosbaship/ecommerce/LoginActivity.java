package com.kosbaship.ecommerce;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosbaship.ecommerce.Admin.AdminCategoryActivity;
import com.kosbaship.ecommerce.Model.Users;
import com.kosbaship.ecommerce.Prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    //                                  (6)
    //(6 - A) initialize all the views on the screen
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    //                                  (9)
    // (9 - A - 1)
    // initialize the views to the admin link
    ////(22 - A)
    // after adding the text to the login screen as a btn
    // declare the var for it
    private TextView AdminLink, NotAdminLink, ForgetPasswordLink;

    // (6 - C - 4 - c)
    // this is the parent db name (table name)
    private String parentDbName = "Users";

    //                      (8 - C)
    // define and initialize Remember ME checkbox
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //(6 - B) create reference to the views on the screen
        LoginButton = findViewById(R.id.login_btn);
        InputPassword = findViewById(R.id.login_password_input);
        InputPhoneNumber = findViewById(R.id.login_phone_number_input);
        // (9 - A - 2)
        // create reference to admin link
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
        ////(22 - B)
        // get reference to the view on the screen
        ForgetPasswordLink = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);

        // (8 - D)
        // get reference to Remember ME checkbox
        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        // initialize the library to be able to use it
        Paper.init(this);


        // (6 - C - 1)
        // when the user hit LoginButton login him
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // (6 - C - 2)
                LoginUser();
            }
        });

        //(22 - C)
        //(22 - D) Go to  activity_reset_password.xml
        // create this activity to direct the user to new activity to reset his password
        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                // we add this data to notify the reset password acitvity
                // show the settings that belongs to the login acticity
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });


        //(9 - B)
        //  when the user click on Iam Admin do this
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //change the login button text
                LoginButton.setText("Login Admin");
                // hide the admin text
                AdminLink.setVisibility(View.INVISIBLE);
                //show the not admin text
                NotAdminLink.setVisibility(View.VISIBLE);
                chkBoxRememberMe.setVisibility(View.INVISIBLE);
                // change the database parent (table)
                // to the one that store  Admins to deal with it
                parentDbName = "Admins";
            }
        });
        //(9 - C)
        //(9 - D) go to the website and create one admin user manually
        //        and also create the parent node (table)
        //        Until we create a separated panel for the admins (or company) creation
        //        the company can add approve admin so that to keep eye on the users
        // when the user click on Iam not Admin do this
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //change the login button text
                LoginButton.setText("Login");
                // show the admin text
                AdminLink.setVisibility(View.VISIBLE);
                // hide the not admin text
                NotAdminLink.setVisibility(View.INVISIBLE);
                chkBoxRememberMe.setVisibility(View.VISIBLE);
                // change the database parent (table)
                // to the one that store  Users to deal with it
                parentDbName = "Users";
            }
        });


    }
    // (6 - C - 3)
    // login user helper method
    private void LoginUser()
    {
        // get the inputs from the user on the screen
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            InputPhoneNumber.requestFocus();
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            InputPassword.requestFocus();
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // show the dialog bar until the login successful
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // (6 - C - 3)
            // get the permission to open with ur account
            AllowAccessToAccount(phone, password);
        }
    }


    // (6 - C - 4)
    // check if this account already register into the database
    private void AllowAccessToAccount(final String phone, final String password) {

        //                      (8 - E)
        // (8 - F) Go to MainActivity.java
        // if the check box is checked
        // store the user phone number and password inside the prevalent class variables
        // to be able to use them to let the user stayed logged in
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        // (6 - C - 4 - a)
        // create a database reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        // (6 - C - 4 - b)
        // here we will see if the user available or not
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                // (6 - C - 4 - d)
                // check if the user phone number  exists
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                    //                          (7)
                    // (8) Go Build.gradle
                    // (7 - A) Go create Users.java
                    // ((7 - C)
                    // get the phone and the password from the database and store them
                    // inside an instance of the model class Users
                    // getValue() perform getting data from the snapshot children and
                    // store it into a model class so u can refer to ur data later by
                    // setter and getter methods
                    // ((7 - C - 1)
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    // ((7 - C - 2)
                    // compare the data comes from the database with the data comes from the user in
                    // the LoginActivity
                    if (usersData.getPhone().equals(phone)){
                        // if the phone is correct then check password
                        if (usersData.getPassword().equals(password)){
                            //(9 - E)
                            // open AdminActivity.java Or HomeActivity.java
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                //(9 - E - 1)
                                //(9 - E - 2) Go and Create AdminCategoryActivity.java
                                // open the AdminCategoryActivity.java
                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                // (13 - C - 2)
                                // (13 - C - 3) go back to HomeActivity.java
                                //                          (This step also inside MainActivity.java)
                                // save the user data into prevalent class because we will need it after here being login
                                // to get fis info and this is one of the power of using the model class it will save
                                // the logged in user data instead of retrieving it again and again
                                // usersData :
                                //      is referencing to the User.class which in code means it
                                //      containing all the current user data
                                Prevalent.currentOnlineUser = usersData;
                                // to close all the existing activities we need to do some flags in this Intent
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                // ((7 - C - 2 - a) Go and create HomeActivity.java
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                // (13 - C - 2)
                                // (13 - C - 3) go back to HomeActivity.java
                                //                          (This step also inside MainActivity.java)
                                // save the user data into prevalent class because we will need it after here being login
                                // to get fis info and this is one of the power of using the model class it will save
                                // the logged in user data instead of retrieving it again and again
                                // usersData :
                                //      is referencing to the User.class which in code means it
                                //      containing all the current user data
                                Prevalent.currentOnlineUser = usersData;
                                // to close all the existing activities we need to do some flags in this Intent
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        } else {
                            loadingBar.dismiss();
                            InputPassword.requestFocus();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    // (6 - C - 4 - d - 1)
                    // if the user phone number do not exist
                    InputPhoneNumber.requestFocus();
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }


}
