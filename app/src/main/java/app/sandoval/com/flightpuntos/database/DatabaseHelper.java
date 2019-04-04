package app.sandoval.com.flightpuntos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.sandoval.com.flightpuntos.HelperUtils.HelperUtilities;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FLIGHTAPPPUNTOS";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "CLIENT");
        db.execSQL("DROP TABLE IF EXISTS " + "ACCOUNT");

        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {

            db.execSQL(createClient());
            db.execSQL(createAccount());

            insertClient(db, "Juan", "Sandoval", "3124532091");

            insertAccount(db, "juansandoval222@gmail.com", "123456", 1);
        }
    }

    public String createAccount() {
        return "CREATE TABLE ACCOUNT (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "EMAIL TEXT, " +
                "PASSWORD TEXT, " +
                "ACCOUNT_CLIENT INTEGER, " +
                "FOREIGN KEY (ACCOUNT_CLIENT) REFERENCES CLIENT(_id));";
    }

    public String createClient() {
        return "CREATE TABLE CLIENT (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FIRSTNAME TEXT COLLATE NOCASE, " +
                "LASTNAME TEXT COLLATE NOCASE, " +
                "PHONE TEXT, " +
                "IMAGE BLOB);";
    }


    public static void insertAccount(SQLiteDatabase db, String email, String password, int clientID) {
        ContentValues accountValues = new ContentValues();
        accountValues.put("EMAIL", email);
        accountValues.put("PASSWORD", password);
        accountValues.put("ACCOUNT_CLIENT", clientID);
        db.insert("ACCOUNT", null, accountValues);
    }

    public static void insertClient(SQLiteDatabase db, String firstName, String lastName, String phone) {
        ContentValues clientValues = new ContentValues();
        clientValues.put("FIRSTNAME", HelperUtilities.capitalize(firstName.toLowerCase()));
        clientValues.put("LASTNAME", HelperUtilities.capitalize(lastName.toLowerCase()));
        clientValues.put("PHONE", phone);
        db.insert("CLIENT", null, clientValues);
    }

    public static Cursor selectClientID(SQLiteDatabase db, String firstName, String lastName,
                                        String phone) {
        return db.query("CLIENT", new String[]{"_id"},
                "FIRSTNAME = ? AND LASTNAME = ? AND PHONE = ? ",
                new String[]{firstName, lastName, phone},
                null, null, null, null);
    }


    public static Cursor selectAccount(SQLiteDatabase db, String email) {
        return db.query("ACCOUNT", null, " EMAIL = ? ",
                new String[]{email}, null, null, null, null);
    }


    public static Cursor login(SQLiteDatabase db, String email, String password) {
        return db.query("ACCOUNT", new String[]{"_id", "EMAIL", "PASSWORD", "ACCOUNT_CLIENT"},
                "EMAIL = ? AND PASSWORD = ? ", new String[]{email, password},
                null, null, null, null);
    }


}
