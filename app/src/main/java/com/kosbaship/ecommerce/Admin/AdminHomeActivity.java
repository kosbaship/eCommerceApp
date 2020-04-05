package com.kosbaship.ecommerce.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kosbaship.ecommerce.Buyers.HomeActivity;
import com.kosbaship.ecommerce.Buyers.MainActivity;
import com.kosbaship.ecommerce.R;


// (27 - A - 3)
// (27 - A - 4) Goto Login activity
// get the btns code and put it here
// impelementation
public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // (27 - A - 3) this
        //(18 - A - 2 - a)
        //(18 - B) go to admin_new_order.xml
        // get reference to the logout btn and check order state
        findViewById(R.id.admin_logout_btn).setOnClickListener(this);
        findViewById(R.id.check_orders_btn).setOnClickListener(this);
        //(21 - B - 1)
        // declare and get reference to this btn on the screen
        findViewById(R.id.maintain_btn).setOnClickListener(this);

        // (27 - B - 2)
        findViewById(R.id.check_approve_products_btn).setOnClickListener(this);

    }

    // (27 - A - 3) also this
    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            //(21 - B - 2)
            //(21 - C) Go to HomeActivity.java
            // create function to this btn
            // send data to defrintiate bettwen the admin and the user
            case  R.id.maintain_btn:
                intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                break;
//            (18 - A - 2 - b) create the functions for the two btns (check order & Logout)
            case  R.id.check_orders_btn:
                intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
                break;
            // (27 - B - 3)
            // (27 - B - 4) Go and Create CheckNewProductActivity.java
        //                  then go to the xml code
            case  R.id.check_approve_products_btn:
                intent = new Intent(AdminHomeActivity.this, AdminCheckNewProductActivity.class);
                startActivity(intent);
            break;
            case  R.id.admin_logout_btn:
                intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                // this flag to clear the activity from the previous task
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }

    }
}
