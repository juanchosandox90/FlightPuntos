package app.sandoval.com.flightpuntos.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import app.sandoval.com.flightpuntos.R;
import app.sandoval.com.flightpuntos.controllers.fragments.CheckoutOnewayFragment;
import app.sandoval.com.flightpuntos.controllers.fragments.CheckoutRoundFragment;


public class CheckOutActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private int currentTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        currentTab = sharedPreferences.getInt("CURRENT_TAB", 0);


        if (currentTab == 0) {
            CheckoutOnewayFragment newFragment = new CheckoutOnewayFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (currentTab == 1) {
            CheckoutRoundFragment newFragment = new CheckoutRoundFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }


    }


}
