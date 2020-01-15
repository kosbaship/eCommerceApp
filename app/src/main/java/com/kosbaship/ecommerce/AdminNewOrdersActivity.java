package com.kosbaship.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//(18)
//(18 - A) go  to activity_admin_category.xml
// create this activity and then go to activity_admin_category.xml
// to create th btn which will open this activity
public class AdminNewOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
    }
}
