package com.kosbaship.ecommerce.Buyers;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosbaship.ecommerce.Sellers.SellerCategoryActivity;
import com.kosbaship.ecommerce.Model.Users;
import com.kosbaship.ecommerce.Prevalent.Prevalent;
import com.kosbaship.ecommerce.R;
import com.kosbaship.ecommerce.Sellers.SellerHomeActivity;
import com.kosbaship.ecommerce.Sellers.SellerRegistrationActivity;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    //                                            (2)
    // (3) Go and create activity_login.xml
    // (2 - A)
    // declare the button views on the screen
    private Button joinNowButton, loginButton;

    //(23 - A - 4 - a)
    // declare seller begin btn
    private TextView sellerBeginButton;

    // (8 - F - 5 - g part one )
    private ProgressDialog loadingBar;

    // (8 - F - 5 - d )
    // this is the parent db name (table name)
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // (2 - B)
        // get reference the the button views on the screen
        joinNowButton = findViewById(R.id.main_join_now_btn);
        loginButton = findViewById(R.id.main_login_btn);
        //(23 - A - 4 - b)
        // get reference to seller begin btn
        sellerBeginButton = findViewById(R.id.seller_begin);

        // (8 - F - 5 - g part two )
        loadingBar = new ProgressDialog(this);


        //                                  (8 - F - 1)
        // (9) Go to LoginActivity.java
        // initialize this library to use it
        // why in the main activity?
        // because if the user was checked the Remember Me box in the
        // login activity we have to know that when we first start the app
        // and we will know that by see the value that stored at the
        // Prevalent class by the help if this library
        Paper.init(this);



        //(3 - C)
        //(4) Go to activity_register.xml
        // when the user click login open the login activity for him
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        // (4 - C)
        // (5) Go to RegisterActivity.java
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // (8 - F - 2)
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        // (8 - F - 3)
        // if the user phone and password are not empty
        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            // this is the same check above
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                // (8 - F - 4)
                AllowAccess(UserPhoneKey, UserPasswordKey);

                // (8 - F - 5 - j )
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

        //(23 - A - 4 - b)
        //(23 - A - 5) Go To SellerRegistrationActivity
        // send the user to Seller Activity
        sellerBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    //(24 - G - 3)
    //(25) Go TO SellerLoginActivity.java
    //if the seller already logged in send him to his home activity
    @Override
    protected void onStart() {
        super.onStart();
        // we have to check that
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            // send the Seller to MainActivity  activity
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    // (8 - F - 5)
    private void AllowAccess(final String userPhoneKey, final String userPasswordKey) {
        // (8 - F - 5 - a )
        // create a database reference
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        // (8 - F - 5 - b )
        // here we will see if the user available or not
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // (8 - F - 5 - c )
                // check if the user phone number  exists
                if (dataSnapshot.child("Users").child(userPhoneKey).exists()) {

                    // get the phone and the password from the database and store them
                    // inside an instance of the model class Users
                    // getValue() perform getting data from the snapshot children and
                    // store it into a model class so u can refer to ur data later by
                    // setter and getter methods

                    // (8 - F - 5 - e )
                    Users usersData = dataSnapshot.child("Users").child(userPhoneKey).getValue(Users.class);

                    // (8 - F - 5 - f )
                    // compare the data comes from the database with the data comes from the user in
                    // the LoginActivity
                    if (usersData.getPhone().equals(userPhoneKey)){
                        // (8 - F - 5 - h )
                        // if the phone is correct then check password
                        if (usersData.getPassword().equals(userPasswordKey)){
                            Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
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
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    // also modified the welcome activity to open the saved admin
                } else if (dataSnapshot.child("Admins").child(userPhoneKey).exists()) {

                    // get the phone and the password from the database and store them
                    // inside an instance of the model class Users
                    // getValue() perform getting data from the snapshot children and
                    // store it into a model class so u can refer to ur data later by
                    // setter and getter methods
                    Users usersData = dataSnapshot.child("Admins").child(userPhoneKey).getValue(Users.class);

                    // compare the data comes from the database with the data comes from the user in
                    // the LoginActivity
                    if (usersData.getPhone().equals(userPhoneKey)){

                        // if the phone is correct then check password
                        if (usersData.getPassword().equals(userPasswordKey)){
                            Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, SellerCategoryActivity.class);
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
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    // (8 - F - 5 - i )
                    // if the user phone number do not exist
                    Toast.makeText(MainActivity.this, "Account with this " + userPhoneKey + " number do not exists. in " , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
