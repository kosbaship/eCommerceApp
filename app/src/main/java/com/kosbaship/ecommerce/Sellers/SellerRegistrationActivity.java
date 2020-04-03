package com.kosbaship.ecommerce.Sellers;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kosbaship.ecommerce.Buyers.MainActivity;
import com.kosbaship.ecommerce.R;

import java.sql.Ref;
import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    //(23 - A - 5 - a)
    //(23 - B - 1)
    // declare the views on the screen
    EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    Button registerButton, loginBeginButton;

    //(23 - B - 5 - b - 3)
    private FirebaseAuth mAuth;

    //(23 - B - 5 - b - 6)
    // declare this to show it when the user email is created
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        //(23 - B - 5 - b - 4)
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

        //(23 - B - 5 - b - 7)
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
         final String sName = nameInput.getText().toString();
         final String sPhone = phoneInput.getText().toString();
         final String sEmail = emailInput.getText().toString();
         String sPassword = passwordInput.getText().toString();
         final String sAddress = addressInput.getText().toString();

        //(23 - B - 5 - b - 1)
        //(23 - B - 5 - b - 2) Go to build.gradle
        // check the fields not empty
        if (!sName.equals("") && !sPhone.equals("") && !sEmail.equals("") && !sPassword.equals("") && !sAddress.equals("")){

            //(23 - B - 5 - b - 8)
            //(24)create BottomNavigationActivity starts from build.gradle
            // show this dialog bar to the user while
            // checking the data validation
            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //(23 - B - 5 - b - 5)
            // create user with the email and password
            mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                // create a database reference
                                final DatabaseReference rootRef;
                                        rootRef = FirebaseDatabase.getInstance().getReference();

                                // get this Seller Id
                                String sUID = mAuth.getCurrentUser().getUid();
                                // put the data inside the firebase database with hash map
                                HashMap<String, Object> sellerDataMap = new HashMap<>();
                                sellerDataMap.put("sid", sUID);
                                sellerDataMap.put("name", sName);
                                sellerDataMap.put("phone", sPhone);
                                sellerDataMap.put("email", sEmail);
                                sellerDataMap.put("address", sAddress);

                                // create the Sellers Node
                                rootRef.child("Sellers")
                                        .child(sUID)
                                        .updateChildren(sellerDataMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {// listen to this task
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //remove the loading bar
                                                loadingBar.dismiss();
                                                Toast.makeText(SellerRegistrationActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                                //(24 - G - 2)
                                                //(24 - G - 3) Goto Main Activity.java
                                                // send the user to SellerHomePage  activity
                                                Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            } else {
                                //remove the loading bar
                                loadingBar.dismiss();
                                Toast.makeText(SellerRegistrationActivity.this,
                                        "The Account Not Created With Email and Password",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "Please Complete The Registration Form", Toast.LENGTH_SHORT).show();
        }
    }


}