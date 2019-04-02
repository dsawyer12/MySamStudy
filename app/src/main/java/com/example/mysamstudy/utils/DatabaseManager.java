package com.example.mysamstudy.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mysamstudy.objects.Card;
import com.example.mysamstudy.objects.Set;
import com.example.mysamstudy.objects.User;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String TAG = "TAG";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mySamStudy.db";

    /* Users table */
    private static final String USERS_TABLE = "users";
    private static final String user_id = "user_id";
    private static final String first_name = "first_name";
    private static final String last_name = "last_name";
    private static final String username = "username";
    private static final String password = "password";
    private static final String email = "email";
    private static final String register_date = "register_date";

    /* Cards table */
    private static final String CARDS_TABLE = "cards";
    private static final String card_id = "card_id";
    private static final String question = "question";
    private static final String answer = "answer";

    /* Sets table */
    private static final String SETS_TABLE = "sets";
    private static final String set_id = "set_id";
    private static final String set_name = "set_name";
    private static final String set_size = "set_size";
    private static final String show_answers = "show_answers";
    private static final String loop_set = "loop_set";
    private static final String share_set = "share_set";

    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: DBMS");
        createUsersTable(db);
        createSetsTable(db);
        createCardsTable(db);
    }

    private void createUsersTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " + USERS_TABLE + "("
                + user_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + first_name + " TEXT NOT NULL, "
                + last_name + " TEXT NOT NULL, "
                + username + " TEXT NOT NULL, "
                + password + " TEXT NOT NULL, "
                + email + " TEXT NOT NULL, "
                + register_date + " DATETIME NOT NULL)";
        db.execSQL(query);
    }

    private void createSetsTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " +
                SETS_TABLE + "(" +
                set_id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                set_name + " TEXT NOT NULL, " +
                set_size + " INTEGER NOT NULL DEFAULT (0), " +
                show_answers + " BIT NOT NULL DEFAULT (0), " +
                loop_set + " BIT NOT NULL DEFAULT (0), " +
                share_set + " BIT NOT NULL DEFAULT (0), " +
                user_id + " INTEGER NOT NULL REFERENCES " + USERS_TABLE + "(" + user_id + "))";
        db.execSQL(query);
    }

    private void createCardsTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " +
                CARDS_TABLE + "(" +
                card_id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                question + " TEXT NOT NULL, " +
                answer + " TEXT NOT NULL, " +
                set_id + " INTEGER NOT NULL REFERENCES " +
                SETS_TABLE + "(" + set_id + "))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SETS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CARDS_TABLE);
        onCreate(db);
    }

    public void getUserById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + " FROM " + "USER_TABLE";

    }

    public User getUser(String mUsername, String mPassword){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + username + " = '" + mUsername +
                "' AND " + password + " = '" + mPassword + "'";
        Cursor c = database.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0){
            Log.d(TAG, "getUser: NULL");
            return null;
        }
        User user = new User();
        c.moveToFirst();
        do {
            user.setUser_id(c.getInt(c.getColumnIndex(user_id)));
            user.setFirst_name(c.getString(c.getColumnIndex(first_name)));
            user.setLast_name(c.getString(c.getColumnIndex(last_name)));
            user.setEmail(c.getString(c.getColumnIndex(email)));
            user.setPassword(c.getString(c.getColumnIndex(password)));
            user.setUsername(c.getString(c.getColumnIndex(username)));
            user.setRegister_date(c.getString(c.getColumnIndex(register_date)));
        }
        while(c.moveToNext());
        c.close();
        return user;
    }

    public boolean checkUsername(String mUsername){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + username +
                " FROM " + USERS_TABLE +
                " WHERE " + username + " = '" + mUsername + "'";
        Cursor c = db.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public long addUser(User user){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(first_name, user.getFirst_name());
        values.put(last_name, user.getLast_name());
        values.put(username, user.getUsername());
        values.put(email, user.getEmail());
        values.put(password, user.getPassword());
        values.put(register_date, user.getRegister_date());
        return database.insert(USERS_TABLE, null, values);
    }

    public void updateUserFirstName(User user, String Fname){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE " + USERS_TABLE + "SET " + first_name + " = " + Fname
                + " WHERE " + user_id + " = '" + user.getUser_id() + "'";
        database.execSQL(query);
    }

    public void updateUserLastName(User user, String Lname){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "";
    }

    public void updateUserUsername(User user, String username){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "";
    }

    public void updateUserEmail(User user, String email){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "";
    }

    public void removeUser(){

    }

    public ArrayList<Set> getSets(int userId){
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<Set> setsList = new ArrayList<>();
        String query = "SELECT * FROM " + SETS_TABLE + " WHERE " + user_id + " = '" + userId + "'";
        Cursor c = database.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0){
            c.close();
            return null;
        }
        c.moveToFirst();
        do {
            setsList.add(new Set(
                    c.getInt(c.getColumnIndex(set_id)),
                    c.getString(c.getColumnIndex(set_name)),
                    c.getInt(c.getColumnIndex(set_size)),
                    (c.getInt(c.getColumnIndex(show_answers)) != 0),
                    (c.getInt(c.getColumnIndex(loop_set)) != 0),
                    (c.getInt(c.getColumnIndex(share_set)) != 0),
                    c.getInt(c.getColumnIndex(user_id))
            ));
        }
        while (c.moveToNext());
        c.close();
        return setsList;
    }

    public long addSet(Set set){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(set_name, set.getSetName());
        values.put(set_size, 0);
        values.put(show_answers, 0);
        values.put(loop_set, 0);
        values.put(share_set, 0);
        values.put(user_id, set.getFK());
        return database.insert(SETS_TABLE, null, values);
    }

    public void updateSetSize(Set set){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE " + SETS_TABLE + " SET " + set_size + " = " + set.getSetSize() + " WHERE " + set_id + " = " + set.getSetId();
        database.execSQL(query);
    }

    public void deleteSet(int setId){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " + SETS_TABLE + " WHERE " + set_id + " = " + setId;
        database.execSQL(query);
    }

    public ArrayList<Card> getCards(int setID){
        ArrayList<Card> cardList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + CARDS_TABLE + " WHERE " + set_id + " = '" + setID + "'";
        Cursor c = database.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0){
            c.close();
            return null;
        }
        c.moveToFirst();
        do {
            cardList.add(new Card(
                    c.getInt(c.getColumnIndex(card_id)),
                    c.getString(c.getColumnIndex(question)),
                    c.getString(c.getColumnIndex(answer)),
                    c.getInt(c.getColumnIndex(set_id))
            ));
        }
        while(c.moveToNext());
        c.close();
        return cardList;
    }

    public long addCard(Card card){
        Log.d(TAG, "FK : " + String.valueOf(card.getFK()));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(question, card.getCardQuestion());
        values.put(answer, card.getCardAnswer());
        values.put(set_id, card.getFK());
        return db.insert(CARDS_TABLE,null, values);
    }

    public  void deleteCard(int cardId){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " + CARDS_TABLE + " WHERE " + card_id + " = " + cardId;
        database.execSQL(query);
    }

}