package com.kosbaship.ecommerce.Sellers.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kosbaship.ecommerce.R;

public class HomeFragment extends Fragment {

    //(24 - E - 1) Override this method
    //(24 - F) Go to SellerHomeActivity.java
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // (24 - E -2)
        // change the return to this
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
