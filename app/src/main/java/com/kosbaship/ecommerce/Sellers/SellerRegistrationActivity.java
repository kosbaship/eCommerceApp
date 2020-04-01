package com.kosbaship.ecommerce.Sellers;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosbaship.ecommerce.Buyers.LoginActivity;
import com.kosbaship.ecommerce.Buyers.RegisterActivity;
import com.kosbaship.ecommerce.R;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    //(23 - A - 5 - a)
    //(23 - B - 1)
    // declare the views on the screen
    EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    Button registerButton, loginBeginButton;

    //(23 - B - 5 - d - 1 - Two)
    private FirebaseAuth mAuth;

    //(23 - B - 5 - b - 1)
    // declare this to show it when the user email is created
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        //(23 - B - 5 - d - 1 - Three)
        mAuth = FirebaseAuth.getInstance();

        //(23 - B - 2)
        // get reference to the views on the screen
        nameInput = findViewById(R.id.seller_name);
        phoneInput = findViewById(R.id.seller_phone);
        emailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        addressInput = findViewById(R.id.seller_address);
        registerButton = findViewById(R.id.seller_register_btn);
        //(23 - A - 5 - b)
        loginBeginButton = findViewById(R.id.seller_already_have_account_btn);

        //(23 - B - 5 - b - 2)
        // get reference to this view
        loadingBar = new ProgressDialog(this);

        //(23 - A - 5 - c)
        // send the user to Seller Login Activity
        loginBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        //(23 - B - 3)
        // set registration process
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(23 - B - 4)
                // call the helper method
                registerSeller();
            }
        });

    }

    //(23 - B - 5)
    // do the work
    private void registerSeller() {
        //(23 - B - 5 - a)
        // get text from the seller on the screen
        String sName = nameInput.getText().toString();
        String sPhone = phoneInput.getText().toString();
        String sEmail = emailInput.getText().toString();
        String sPassword = passwordInput.getText().toString();
        String sAddress = addressInput.getText().toString();

        //(23 - B - 5 - b)
        // check if he trigger the create button without filling the edit texts
        if (TextUtils.isEmpty(sName)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
            nameInput.requestFocus();
        } else if (TextUtils.isEmpty(sPhone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
            phoneInput.requestFocus();
        } else if (TextUtils.isEmpty(sEmail)) {
            Toast.makeText(this, "Please write your Email...", Toast.LENGTH_SHORT).show();
            emailInput.requestFocus();
        }  else if (TextUtils.isEmpty(sPassword)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
            passwordInput.requestFocus();
        } else if (TextUtils.isEmpty(sAddress)) {
            Toast.makeText(this, "Please write your Address...", Toast.LENGTH_SHORT).show();
            addressInput.requestFocus();
        } else {
            //(23 - B - 5 - b - 3)
            // show this dialog bar to the user while
            // checking the data validation
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //(23 - B - 5 - c)
            // check if this a unique phone number into our database
            ValidatephoneNumber(sName, sPhone, sEmail, sPassword, sAddress);
        }
    }
    //(23 - B - 5 - d)
    //(23 - B - 5 - d - 1) Go to build.gradle
    private void ValidatephoneNumber(final String sName, final String sPhone, final String sEmail, final String sPassword, final String sAddress) {
        //(23 - B - 5 - d - 2)
        // create a database reference
        final DatabaseReference Ref;
        Ref = FirebaseDatabase.getInstance().getReference();

        //(23 - B - 5 - d - 3)
        // store the seller into the db
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // check if this seller exist in the db or not
                if(!(dataSnapshot.child("Sellers").child(sPhone).exists())){
                    // put the data inside the firebase database with hash map
                    HashMap<String, Object> sellerDataMap = new HashMap<>();
                    sellerDataMap.put("name", sName);
                    sellerDataMap.put("phone", sPhone);
                    sellerDataMap.put("email", sEmail);
                    sellerDataMap.put("password", sPassword);
                    sellerDataMap.put("address", sAddress);

                    // create the "Sellers" Node
                    Ref.child("Sellers")
                            .child(sPhone)
                            .updateChildren(sellerDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() { // listen to this task
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // if the registration (task) is Successful
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SellerRegistrationActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        //remove the loading bar
                                        loadingBar.dismiss();
                                        // send the user to Seller login activity
                                        Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        //remove the loading bar
                                        loadingBar.dismiss();
                                        Toast.makeText(SellerRegistrationActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SellerRegistrationActivity.this, "This " + sPhone + " already exists.", Toast.LENGTH_SHORT).show();
                    // dismiss the login bar because the user is already exist so we can create account for him
                    loadingBar.dismiss();
                    phoneInput.requestFocus();
                    Toast.makeText(SellerRegistrationActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
