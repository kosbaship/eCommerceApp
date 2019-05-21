package com.kosbaship.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
//(9 - E - 2)
// creation of this activity

public class AdminCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Toast.makeText(AdminCategoryActivity.this, "Hello Admin, Welcome...", Toast.LENGTH_LONG).show();
    }
}
