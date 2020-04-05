package com.kosbaship.ecommerce.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kosbaship.ecommerce.R;

public class SellerLoginActivity extends AppCompatActivity {

    //(25 - A - 1)
    // declare the views on the screen
    EditText emailInput,  passwordInput;
    Button sellerLoginButton;

    // // (25 - B - 1)
    // declare this to show it when the user email is created
    private ProgressDialog loadingBar;

    // (25 - C - 1)
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        // (25 - C - 2)
        mAuth = FirebaseAuth.getInstance();


        //(25 - A - 2)
        //get reference to them
        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        sellerLoginButton = findViewById(R.id.seller_login_btn);

        // (25 - B - 2)
        // get reference to this view
        loadingBar = new ProgressDialog(this);

        //(25 - A - 3)
        // make function of this btn
        sellerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(25 - A - 3 - One)
                // call this method
                SellerLoginHelper();
            }
        });

    }
    // (25 - A - 3 - Two)
    // get the inputs and check if they are empty
    private void SellerLoginHelper() {
        // get the inputs from the user on the screen
        String sEmail = emailInput.getText().toString();
        String sPassword = passwordInput.getText().toString();

        // check the fields not empty
        if (!sEmail.equals("") && !sPassword.equals("")) {

            // (25 - B - 3)
            // show this dialog bar to the user while
            // checking the data validation
            loadingBar.setTitle("Login Seller Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // (25 - C - 3)
            // (26) this step is to change AdminNewProductActivity
            //          to be belong to the Seller
            //        => then go to SellerAddNewProductActivity
            mAuth.signInWithEmailAndPassword(sEmail, sPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                //remove the loading bar
                                loadingBar.dismiss();

                                // send the user to SellerHomePage  activity
                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                //remove the loading bar
                                loadingBar.dismiss();
                                Toast.makeText(SellerLoginActivity.this,
                                        "Email or Password is wrong",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        } else {
            Toast.makeText(this, "Please, Complete The Login Form",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
