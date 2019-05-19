package com.kosbaship.ecommerce;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.kosbaship.ecommerce.Model.Users;
import com.kosbaship.ecommerce.Prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

public class LoginActivity extends AppCompatActivity {

    //                                  (6)
    //(6 - A) initialize all the views on the screen
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink, ForgetPasswordLink;

    // (6 - C - 4 - c)
    // this is the parent db name (table name)
    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //(6 - B) create reference to the views on the screen
        LoginButton = findViewById(R.id.login_btn);
        InputPassword = findViewById(R.id.login_password_input);
        InputPhoneNumber = findViewById(R.id.login_phone_number_input);
        loadingBar = new ProgressDialog(this);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
        ForgetPasswordLink = findViewById(R.id.forget_password_link);

        // (6 - C - 1)
        // when the user hit LoginButton login him
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // (6 - C - 2)
                LoginUser();
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

        // (6 - C - 4 - a)
        // create a database reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        // (6 - C - 4 - b)
        // here we will see if the user avilible or not
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // (6 - C - 4 - d)
                // check if the user phone number  exists
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                    //                          (7)
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
                            Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            // ((7 - C - 2 - a) Go and create HomeActivity.java
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
