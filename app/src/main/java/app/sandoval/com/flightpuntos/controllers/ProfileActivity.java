package app.sandoval.com.flightpuntos.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;

import app.sandoval.com.flightpuntos.HelperUtils.HelperUtilities;
import app.sandoval.com.flightpuntos.R;
import app.sandoval.com.flightpuntos.database.DatabaseHelper;
import app.sandoval.com.flightpuntos.models.Account;
import app.sandoval.com.flightpuntos.models.Client;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private Intent intent;
    private int clientID;
    private String TAG;
    private ImageButton uploadImage;
    private ImageView profileImage;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private TextView clientFirstname;
    private TextView clientLastName;
    private TextView clientEmail;
    private TextView clientPhone;
    private TextView fullName;
    private ImageButton editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = (ImageView) findViewById(R.id.profileImage);
        uploadImage = (ImageButton) findViewById(R.id.btnEditProfilePicture);
        editProfile = (ImageButton) findViewById(R.id.btnEditProfile);

        clientID = clientID();

        getProfileInformation(clientID);
        loadImage(clientID);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(uploadImageIntent, REQUEST_CODE);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getProfileInformation(int employeeID) {
        try {

            clientFirstname = (TextView) findViewById(R.id.txtClientFirstName);
            clientLastName = (TextView) findViewById(R.id.txtClientLastName);
            clientEmail = (TextView) findViewById(R.id.txtClientEmail);
            clientPhone = (TextView) findViewById(R.id.txtClientPhone);
            fullName = (TextView) findViewById(R.id.txtFullName);


            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            cursor = DatabaseHelper.selectClientJoinAccount(db, clientID);


            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                String fName = cursor.getString(0);
                String lName = cursor.getString(1);
                String phone = cursor.getString(2);
                String creditCard = cursor.getString(3);
                String email = cursor.getString(4);

                Client client = new Client(fName, lName, phone, creditCard);
                Account account = new Account(email);

                clientFirstname.setText(getString(R.string.profile_first_name_label) + " " + client.getFirstName());
                clientLastName.setText(getString(R.string.profile_last_name_label) + " " + client.getLastName());
                clientPhone.setText(getString(R.string.profile_phone_label) + " " + client.getPhone());
                clientEmail.setText(getString(R.string.profile_email_label) + " " + account.getEmail());

                fullName.setText(client.getFirstName() + " " + client.getLastName());
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK && data != null) {

                        Uri selectedImage = data.getData();

                        Bitmap bitmap = getBitmap(this.getContentResolver(), selectedImage);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                        byte[] byteArray = stream.toByteArray();

                        try {
                            databaseHelper = new DatabaseHelper(getApplicationContext());
                            db = databaseHelper.getWritableDatabase();

                            DatabaseHelper.updateClientImage(db,
                                    byteArray,
                                    String.valueOf(clientID));

                            db = databaseHelper.getReadableDatabase();

                            cursor = DatabaseHelper.selectImage(db, clientID);

                            if (cursor.moveToFirst()) {
                                byte[] image = cursor.getBlob(0);

                                profileImage.setImageBitmap(HelperUtilities.decodeSampledBitmapFromByteArray(image, 300, 300));

                            }

                        } catch (SQLiteException ex) {
                            ex.printStackTrace();
                        }

                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(int clientID) {
        try {
            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();


            cursor = DatabaseHelper.selectImage(db, clientID);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                if (cursor.getBlob(0) != null) {
                    byte[] image = cursor.getBlob(0);

                    profileImage.setImageBitmap(HelperUtilities.decodeSampledBitmapFromByteArray(image, 300, 300));

                }

            }

        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }

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
