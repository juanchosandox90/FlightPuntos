package app.sandoval.com.flightpuntos.controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import app.sandoval.com.flightpuntos.HelperUtils.HelperUtilities;
import app.sandoval.com.flightpuntos.R;
import app.sandoval.com.flightpuntos.database.DatabaseHelper;

public class EditProfileActivity extends AppCompatActivity {

    private Button btnUpdateProfile;
    private EditText clientFirstName;
    private EditText clientLastName;
    private EditText clientEmail;
    private EditText clientPhone;
    private int clientID;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnUpdateProfile = (Button) findViewById(R.id.btnSaveProfile);

        clientFirstName = (EditText) findViewById(R.id.txtFirstNameEdit);
        clientLastName = (EditText) findViewById(R.id.txtLastNameEdit);
        clientEmail = (EditText) findViewById(R.id.txtEmailEdit);
        clientPhone = (EditText) findViewById(R.id.txtPhoneEdit);

        loadProfileInfo();

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();

            }
        });

    }

    public void loadProfileInfo() {

        try {

            clientID = clientID();

            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getWritableDatabase();


            cursor = DatabaseHelper.selectClientJoinAccount(db, clientID);


            if (cursor.moveToFirst()) {

                String firstName = cursor.getString(0);
                String lastName = cursor.getString(1);
                String phone = cursor.getString(2);
                String creditCard = cursor.getString(3);
                String email = cursor.getString(4);

                clientFirstName.setText(firstName);
                clientLastName.setText(lastName);
                clientEmail.setText(email);
                clientPhone.setText(phone);

            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }

    }

    public void updateProfile() {
        try {

            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getWritableDatabase();

            clientID = clientID();
            isValid = isValidUserInput();

            if (isValid) {
                DatabaseHelper.updateClient(db,
                        clientFirstName.getText().toString(),
                        clientLastName.getText().toString(),
                        clientPhone.getText().toString(),
                        clientID
                );

                DatabaseHelper.updateAccount(db,
                        clientEmail.getText().toString(),
                        clientID);


                updateProfileDialog().show();
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }

    }

    public int clientID() {
        LoginActivity.sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        clientID = LoginActivity.sharedPreferences.getInt(LoginActivity.CLIENT_ID, 0);
        return clientID;
    }

    public Dialog updateProfileDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.profile_updated_successfull)
                .setCancelable(false)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }
                });

        return builder.create();
    }

    public boolean isValidUserInput() {
        if (HelperUtilities.isEmptyOrNull(clientFirstName.getText().toString())) {
            clientFirstName.setError(getString(R.string.enter_first_name));
            return false;
        } else if (!HelperUtilities.isString(clientFirstName.getText().toString())) {
            clientFirstName.setError(getString(R.string.enter_valid_first_name));
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(clientLastName.getText().toString())) {
            clientLastName.setError(getString(R.string.enter_last_name));
            return false;
        } else if (!HelperUtilities.isString(clientLastName.getText().toString())) {
            clientLastName.setError(getString(R.string.enter_valid_last_name));
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(clientEmail.getText().toString())) {
            clientEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!HelperUtilities.isValidEmail(clientEmail.getText().toString())) {
            clientEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(clientPhone.getText().toString())) {
            clientPhone.setError(getString(R.string.enter_phone));
            return false;
        } else if (!HelperUtilities.isValidPhone(clientPhone.getText().toString())) {
            clientPhone.setError(getString(R.string.enter_valid_phone));
            return false;
        }

        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            cursor.close();
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
