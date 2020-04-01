package com.kosbaship.ecommerce.Sellers;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kosbaship.ecommerce.R;

public class SellerRegistrationActivity extends AppCompatActivity {

    //(23 - A - 5 - a)
    Button sellerLoginBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        //(23 - A - 5 - b)
        sellerLoginBegin = findViewById(R.id.seller_already_have_account_btn);


        //(23 - A - 5 - c)
        // send the user to Seller Login Activity
        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
