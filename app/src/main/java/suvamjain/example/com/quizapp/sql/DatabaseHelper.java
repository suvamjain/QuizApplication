package suvamjain.example.com.quizapp.sql;

/**
 * Created by SUVAM JAIN on 21-04-2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import suvamjain.example.com.quizapp.modal.QuesInfo;
import suvamjain.example.com.quizapp.modal.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "QuizManager.db";

    /**
     * Constructor
     */
    private static final String TABLE_USER = "user";
    private static final String TABLE_QUIZ = "quiz";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_SECURITY = "user_security";

    // Quiz Table Columns names
    private static final String COLUMN_QUIZ_CATEGORY = "category";
    private static final String COLUMN_QUIZ_QUESID = "ques_id";
    private static final String COLUMN_QUIZ_QUES = "ques";
    private static final String COLUMN_QUIZ_OP1 = "op1";
    private static final String COLUMN_QUIZ_OP2 = "op2";
    private static final String COLUMN_QUIZ_OP3 = "op3";
    private static final String COLUMN_QUIZ_OP4 = "op4";
    private static final String COLUMN_QUIZ_OP5 = "op5";
    private static final String COLUMN_QUIZ_ANS = "ans";


    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_SECURITY + " TEXT)";


    private String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ + "(" + COLUMN_QUIZ_QUESID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_QUIZ_CATEGORY + " TEXT NOT NULL,"+ COLUMN_QUIZ_QUES + " TEXT NOT NULL,"
            + COLUMN_QUIZ_OP1 + " TEXT,"  + COLUMN_QUIZ_OP2 + " TEXT,"
            + COLUMN_QUIZ_OP3 + " TEXT,"  + COLUMN_QUIZ_OP4 + " TEXT,"
            + COLUMN_QUIZ_OP5 + " TEXT,"  + COLUMN_QUIZ_ANS +
            " INTEGER, UNIQUE(" + COLUMN_QUIZ_QUES + "))";


    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_QUIZ_TABLE = "DROP TABLE IF EXISTS " + TABLE_QUIZ;

    /**
     * Constructor
     * @param context
     */

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_QUIZ_TABLE);

        //insert admin user in user table
        db.execSQL(" insert into  " + TABLE_USER + " values(1,'admin','ADMIN','admin123','admin')");

        // insert default values into quiz table
        db.execSQL(" insert into  " + TABLE_QUIZ + " values(1,'General','National sport of India?','Cricket','Football','Kabaddi','Hockey','Basketball',4)");
        db.execSQL(" insert into  " + TABLE_QUIZ + " values(2,'General','Grand Central Terminal, Park Avenue, New York is the worlds?','largest railway station'," +
                "'highest railway station','longest railway station','broadest railway station','None of the above',1)");
        db.execSQL(" insert into  " + TABLE_QUIZ + " values(3,'General','Who is the PM of India?','Rajiv Gandhi','Manmohan Singh','Narendra Modi','Mamta Banerjee','Arvind Kejriwal',3)");
        db.execSQL(" insert into  " + TABLE_QUIZ + " values(4,'General','Hitler party which came into power in 1933 is known as?','Labour Party'," +
                "'Nazi Party','Ku-Klux-Klan','Democratic Party','None of the above',2)");
        db.execSQL(" insert into  " + TABLE_QUIZ + " values(5,'General','India got independent in the year?','1675','1940','1947','2000','None of the above',3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_QUIZ_TABLE);

       // Create tables again
        onCreate(db);
    }

    /**
     * This method is to create user record
     * @param user
     */

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_SECURITY,user.getSecurity());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to add quiz questions
     */
    public long addques(String catg, String ques, String op1, String op2, String op3, String op4, String op5, int ans) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_QUES, ques);
        values.put(COLUMN_QUIZ_CATEGORY, catg);
        values.put(COLUMN_QUIZ_OP1, op1);
        values.put(COLUMN_QUIZ_OP2, op2);
        values.put(COLUMN_QUIZ_OP3, op3);
        values.put(COLUMN_QUIZ_OP4, op4);
        values.put(COLUMN_QUIZ_OP5, op5);
        values.put(COLUMN_QUIZ_ANS, ans);

        // Inserting Row
        long check = db.insert(TABLE_QUIZ, null, values);
        db.close();
        return check;
    }

    public Cursor getDetails()
    {   SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_USER;
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    /**
     * This method to update user record
     */
    public void updateUser(int id, String name, String email, String password, String security) {
        //Toast.makeText(this,"Inside db value is \n " + name + "-" + dob + "-" +yoa +"-" + sem ,Toast.LENGTH_LONG).show();
        Log.e(TAG,"Inside db value is \n " + id + "-" + name + "-" + password+ "-" + security);
        SQLiteDatabase db = this.getWritableDatabase();
        deleteUser(id);

        Log.e(TAG,"Items Deleted Now inserting");

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL,email);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_SECURITY,security);

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to delete user record
     */

    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * This method is to delete user record
     */
    public void deleteQues(String QuesID) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_QUIZ, COLUMN_QUIZ_QUESID + " = ?",
                new String[]{QuesID});
        db.close();
    }

    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jainsuvam1@gmail.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'admin' AND user_password = 'admin123';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    // Here is the portion for security hint

    public boolean checkSecurity(String email, String security) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_SECURITY + " = ?";

        // selection arguments
        String[] selectionArgs = {email, security};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = '16BCE1138' AND security = 'mno';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public Cursor search(String reg) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(TABLE_USER, null, COLUMN_USER_EMAIL + "=?", new String[]{reg}, null, null, null);
        return res;
    }

    public Integer getLastQuesID(){
        String selectQuery = "SELECT " + COLUMN_QUIZ_QUESID + " FROM " + TABLE_QUIZ
                + " ORDER BY " + COLUMN_QUIZ_QUESID + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        return Integer.parseInt(cursor.getString(0));
    }

    public List<QuesInfo> getAllQues() {
        List<QuesInfo> questions = new ArrayList<QuesInfo>();
        String selectQuery = "SELECT * FROM " + TABLE_QUIZ
                + " ORDER BY " + COLUMN_QUIZ_QUESID;

        Log.d("::TABLE ENTRY: ", "Table created");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() == 0)
            Log.d("::cursor count: ", "Cursor empty " + cursor.getCount());

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuesInfo q_tmp = new QuesInfo();
                ArrayList<String> allOpt = new ArrayList<>();
                q_tmp.setQ_id("" + cursor.getInt(cursor.getColumnIndex(COLUMN_QUIZ_QUESID)));
                q_tmp.setCategory("" + cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_CATEGORY)));
                q_tmp.setQuestion("" + cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_QUES)));
                q_tmp.setOpt1("" + cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP1)));
                q_tmp.setOpt2("" + cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP2)));
                q_tmp.setOpt3("" + cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP3)));
                q_tmp.setOpt4("" + cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP4)));
                q_tmp.setOpt5("" + cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP5)));
                q_tmp.setAns("" + cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_ANS)));

                allOpt.add(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP1)));
                allOpt.add(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP2)));
                allOpt.add(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP3)));
                allOpt.add(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP4)));
                allOpt.add(cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_OP5)));

                q_tmp.setOptions(allOpt);

                Log.d("::TABLE ENTRY: ",q_tmp.getQuestion() + " " + q_tmp.getAns());

                questions.add(q_tmp); //adding temporary object to actual ques list.

            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return questions;
    }
}
