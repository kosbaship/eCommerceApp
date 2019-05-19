package com.kosbaship.ecommerce;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //                                            (2)
    // (3) Go and create activity_login.xml
    // (2 - A)
    // declare the button views on the screen
    private Button joinNowButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // (2 - B)
        // get reference the the button views on the screen
        joinNowButton = findViewById(R.id.main_join_now_btn);
        loginButton = findViewById(R.id.main_login_btn);


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


    }
}
