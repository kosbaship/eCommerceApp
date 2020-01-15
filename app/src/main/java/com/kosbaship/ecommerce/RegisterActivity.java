package com.kosbaship.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //                               (4 - B)
    // (4 - C) Go to MainActivity.java
    //    (4 - B - 1)
    // declare this view on the screen
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword;
    //(5 - A - 3)
    // declare this view
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //    (4 - B - 2)
        // get reference to the views on the screen
        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_username_input);
        InputPassword = findViewById(R.id.register_password_input);
        InputPhoneNumber = findViewById(R.id.register_phone_number_input);
        //(5 - A - 3)
        // get reference to this view
        loadingBar = new ProgressDialog(this);

        //                              (5)
        // (6) go to LoginActivity.java
        // (5 - A)
        // when the user hit CreateAccountButton create account for him
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //(5 - A - 2)
                CreateAccount();
            }
        });

    }

    //(5 - A - 2)
    // create account helper method
    private void CreateAccount()
    {

        // get the information from the user
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        // check if he triger the create button without filling the edit texts
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
            InputName.requestFocus();
            InputName.requestFocus();

        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
            InputPhoneNumber.requestFocus();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
            InputPassword.requestFocus();
        }
        else
        {
            //(5 - A - 4)
            // show this dialog bar to the user while
            // checking the data validation
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            //(5 - B - 1)
            // check if this a unique phone number into our database
            ValidatephoneNumber(name, phone, password);
        }
    }


    //(5 - B - 3) Go to build.gradle
    //(5 - B - 2)
    // phone number validation helper method
    private void ValidatephoneNumber(final String name, final String phone, final String password) {
        //(5 - B - 4 - end of five)
        // create a database reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // if the user we creating now his phone number
                // is exist in the database or not because if he is not
                // exist into the database then we will let him
                // to create a new account
                // I think child("Users") Users is the database table
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {   // here is the code of new user creation
                    // put the data inside the firebase database with hash map
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    //store the data that comes from the user in the register
                    // screen into the HashMap as key value pairs
                    // I think also these the columns
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    // create a parent node from the Users
                    // child("Users").child(phone)
                    //      this mean for every user data will be a phone number
                    //      or in other word this is the unique id that we will
                    //      defined the child or the user with then we will call
                    //      updateChildren() method and pass the attributes for this
                    //      phone to be attach with it
                    // updateChildren(userdataMap)
                    //      this is the rest of the child attributes
                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    // if the registration (task) is Successful
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        //remove the loading bar
                                        loadingBar.dismiss();
                                        // send the user to login activity
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        //remove the loading bar
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    // dismiss the login bar because the user is already exist so we can create account for him
                    loadingBar.dismiss();
                    InputPhoneNumber.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    // send him to welcome screen
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
