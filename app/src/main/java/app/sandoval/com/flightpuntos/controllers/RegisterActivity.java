package app.sandoval.com.flightpuntos.controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.sandoval.com.flightpuntos.HelperUtils.HelperUtilities;
import app.sandoval.com.flightpuntos.R;
import app.sandoval.com.flightpuntos.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private int clientID;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
    private EditText inputCreditCard;
    private EditText inputPhone;
    private EditText inputConfirmPassword;
    private EditText inputPassword;
    private boolean isValid;
    private SQLiteOpenHelper registerDatabaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = (Button) findViewById(R.id.btnRegister);
        TextView linkLogin = (TextView) findViewById(R.id.linkLogin);


        inputFirstName = (EditText) findViewById(R.id.txtFirstName);
        inputLastName = (EditText) findViewById(R.id.txtLastName);
        inputEmail = (EditText) findViewById(R.id.txtEmail);
        inputPhone = (EditText) findViewById(R.id.txtPhone);
        inputCreditCard = (EditText) findViewById(R.id.txtCreditCard);
        inputPassword = (EditText) findViewById(R.id.txtPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerNewUser();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        RegisterActivity.this.finish();
    }

    public void registerNewUser() {
        try {
            registerDatabaseHelper = new DatabaseHelper(getApplicationContext());
            db = registerDatabaseHelper.getWritableDatabase();

            cursor = DatabaseHelper.selectAccount(db, HelperUtilities.filter(inputEmail
                    .getText().toString()));

            isValid = isValidUserInput();
            if (isValid) {
                if (cursor != null && cursor.getCount() > 0) {
                    accountExistsAlert().show();
                } else {
                    DatabaseHelper.insertClient(db,
                            inputFirstName.getText().toString(),
                            inputLastName.getText().toString(),
                            inputPhone.getText().toString(),
                            inputCreditCard.getText().toString());

                    cursor = DatabaseHelper.selectClientID(db,
                            inputFirstName.getText().toString(),
                            inputLastName.getText().toString(),
                            inputPhone.getText().toString(),
                            inputCreditCard.getText().toString());

                    if (cursor != null && cursor.getCount() == 1) {
                        cursor.moveToFirst();

                        DatabaseHelper.insertAccount(db,
                                inputEmail.getText().toString(),
                                inputPassword.getText().toString(),
                                cursor.getInt(0));

                        registrationSuccessDialog().show();
                    }
                }
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public Dialog accountExistsAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.account_exist)
                .setCancelable(false)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public Dialog registrationSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.succesful_register)
                .setCancelable(false)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        return builder.create();
    }


    public boolean isValidUserInput() {
        if (HelperUtilities.isEmptyOrNull(inputFirstName.getText().toString())) {
            inputFirstName.setError(getString(R.string.enter_first_name));
            return false;
        } else if (!HelperUtilities.isString(inputFirstName.getText().toString())) {
            inputFirstName.setError(getString(R.string.enter_valid_first_name));
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputLastName.getText().toString())) {
            inputLastName.setError(getString(R.string.enter_last_name));
            return false;
        } else if (!HelperUtilities.isString(inputLastName.getText().toString())) {
            inputLastName.setError(getString(R.string.enter_valid_last_name));
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputEmail.getText().toString())) {
            inputEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!HelperUtilities.isValidEmail(inputEmail.getText().toString())) {
            inputEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputPhone.getText().toString())) {
            inputPhone.setError(getString(R.string.enter_phone));
            return false;
        } else if (!HelperUtilities.isValidPhone(inputPhone.getText().toString())) {
            inputPhone.setError(getString(R.string.enter_valid_phone));
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputCreditCard.getText().toString())) {
            inputCreditCard.setError(getString(R.string.enter_credit_card));
            return false;
        } else if (!HelperUtilities.isValidCreditCard(inputCreditCard.getText().toString())) {
            inputCreditCard.setError(getString(R.string.enter_valid_credit_card));
            return false;
        }
        if (HelperUtilities.isEmptyOrNull(inputPassword.getText().toString())) {
            inputPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (HelperUtilities.isShortPassword(inputPassword.getText().toString())) {
            inputPassword.setError(getString(R.string.enter_valid_pasword));
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputConfirmPassword.getText().toString())) {
            inputConfirmPassword.setError(getString(R.string.confirm_password));
            return false;
        }

        if (!(inputConfirmPassword.getText().toString().equals(inputPassword.getText().toString()))) {

            inputConfirmPassword.setError(getString(R.string.password_not_match));
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
