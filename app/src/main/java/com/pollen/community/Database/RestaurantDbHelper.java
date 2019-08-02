package com.pollen.community.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class RestaurantDbHelper extends  SQLiteOpenHelper {

    private static final String TAG = UserDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "cres";

    private static final String TABLE_RESTAURANT = "restaurants";



    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_STARS = "stars";
    private static final String KEY_LOGO = "logo";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CREATED_AT = "created_at";


    public RestaurantDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESTAURANT_TABLE = "CREATE TABLE "+ TABLE_RESTAURANT +
        "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UID + " INTEGER,"
                + KEY_NAME+" TEXT,"
                + KEY_ADDRESS + "TEXT, "
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_LOGO + "TEXT, "
                + KEY_PHONE + " TEXT,"
                + KEY_STARS + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_RESTAURANT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT);

        onCreate(db);
    }


    public void addRestaurant(String name, String address, String description, String logo , String phone,String stars ,String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME , name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_LOGO, logo);
        values.put(KEY_STARS, stars);
        values.put(KEY_UID, uid);
        values.put(KEY_PHONE, phone);
        values.put(KEY_CREATED_AT, created_at);

        long id = db.insert(TABLE_RESTAURANT, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void UpdateRestaurant(String name, String address, String description, String logo , String phone,String stars ,String uid, String created_at){
       this.deleteRestaurants();
       addRestaurant(name,address,description,logo,phone,stars,uid,created_at);
    }

    public HashMap<String, String> getRestaurants() {
        HashMap<String, String> restaurant = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_RESTAURANT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            restaurant.put("name", cursor.getString(1));
            restaurant.put("address", cursor.getString(2));
            restaurant.put("description", cursor.getString(3));
            restaurant.put("logo", cursor.getString(4));
            restaurant.put("phone", cursor.getString(5));
            restaurant.put("stars", cursor.getString(6));
            restaurant.put("uid", cursor.getString(7));
            restaurant.put("created_at", cursor.getString(8));
        }
        cursor.close();
        db.close();

        return restaurant;
    }

    public int getRestaurantsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RESTAURANT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void deleteRestaurants() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_RESTAURANT, null, null);
        db.close();
    }
}
