package app.sandoval.com.flightpuntos.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import app.sandoval.com.flightpuntos.HelperUtils.HelperUtilities;
import app.sandoval.com.flightpuntos.R;
import app.sandoval.com.flightpuntos.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MY_PREFS";
    public static final String EMAIL = "EMAIL";
    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String LOGIN_STATUS = "LOGGED_IN";
    public static SharedPreferences sharedPreferences;
    private EditText inputEmail;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText inputPassword;
    private TextView txtLoginError;
    private boolean isValid;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private int accountID;
    private int clientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, 0);
        Boolean loggedIn = sharedPreferences.getBoolean(LOGIN_STATUS, false);//login status
        if (loggedIn) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        TextView linkRegister = (TextView) findViewById(R.id.linkRegister);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        txtLoginError = (TextView) findViewById(R.id.txtLoginError);

        mAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                isValid = isValidUserInput();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                if (isValid) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                            LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        attemptLogin();
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.failed_login), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                    );
                } else progressBar.setVisibility(View.INVISIBLE);
            }
        });


        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void attemptLogin() {

        try {

            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            String filteredEmail = HelperUtilities.filter(inputEmail.getText().toString());
            String filteredPassword = HelperUtilities.filter(inputPassword.getText().toString());


            cursor = DatabaseHelper.login(db, filteredEmail, filteredPassword);

            if (cursor != null && cursor.getCount() == 1) {
                cursor.moveToFirst();

                String email = cursor.getString(1);
                clientID = cursor.getInt(3);
                sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt(CLIENT_ID, clientID);
                editor.putString(EMAIL, email);
                editor.putBoolean(LOGIN_STATUS, true);

                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            } else {

                txtLoginError.setText(R.string.invalid_email_password);
            }


        } catch (
                SQLiteException ex) {
            ex.printStackTrace();
        }

    }

    public boolean isValidUserInput() {
        if (HelperUtilities.isEmptyOrNull(inputEmail.getText().toString())) {
            txtLoginError.setText(R.string.invalid_email_password);
            return false;
        }
        if (HelperUtilities.isEmptyOrNull(inputPassword.getText().toString())) {
            txtLoginError.setText(R.string.invalid_email_password);
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (cursor != null) {
                cursor.close();
            }

            if (db != null) {
                db.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
    }
}
