package com.kosbaship.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    //                               (4 - B)
    // (4 - C) Go to MainActivity.java
    //    (4 - B - 1)
    // declare this view on the screen
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword;

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




    }
}
