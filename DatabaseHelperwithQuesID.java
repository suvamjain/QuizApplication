package suvamjain.example.com.quizapp.sql;

/**
 * Created by SUVAM JAIN on 29-03-2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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


    private String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ + "("
            + COLUMN_QUIZ_QUESID + " INTEGER," + COLUMN_QUIZ_CATEGORY + " TEXT,"
            + COLUMN_QUIZ_QUES + " TEXT," + COLUMN_QUIZ_OP1 + " TEXT,"
            + COLUMN_QUIZ_OP2 + " TEXT," + COLUMN_QUIZ_OP3 + " TEXT,"
            + COLUMN_QUIZ_OP4 + " TEXT," + COLUMN_QUIZ_OP5 + " TEXT,"
            + COLUMN_QUIZ_ANS + " INTEGER, PRIMARY KEY(" + COLUMN_QUIZ_QUESID + ","
            + COLUMN_QUIZ_CATEGORY + "," + COLUMN_QUIZ_QUES + "))";


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

        // insert default values into quiz table
        db.execSQL(" insert into  " + TABLE_QUIZ + " values(1,'General','India is a?','Country','State','Village','Town','City',1)");
        db.execSQL(" insert into  " + TABLE_QUIZ + " values(2,'Sports','National sport?','Cricket','Football','Kabaadi','Hockey','Basketball',1)");
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
    public void addques(String catg, String ques, String op1, String op2, String op3, String op4, String op5, int ans) {
        SQLiteDatabase db = this.getWritableDatabase();
        int lastQuesID = getLastQuesID();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_QUESID,lastQuesID+1);
        values.put(COLUMN_QUIZ_QUES, ques);
        values.put(COLUMN_QUIZ_CATEGORY, catg);
        values.put(COLUMN_QUIZ_OP1, op1);
        values.put(COLUMN_QUIZ_OP2, op2);
        values.put(COLUMN_QUIZ_OP3, op3);
        values.put(COLUMN_QUIZ_OP4, op4);
        values.put(COLUMN_QUIZ_OP5, op5);
        values.put(COLUMN_QUIZ_ANS, ans);

        // Inserting Row
        db.insert(TABLE_QUIZ, null, values);
        db.close();
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

    /**
     * This method to check user already exist or not
     *
     * @param email
     * @return true/false
     */
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

    /**
     * This method to check user's reg no and password matches or not
     *
     * @param email
     * @param password
     * @return true/false
     */
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
         * SELECT user_id FROM user WHERE user_email = '16BCE1138' AND user_password = 'mno';
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

    public List<String> getAllQues() {
        List<String> questions = new ArrayList<String>();
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
                Log.d("::TABLE ENTRY: ", cursor.getString(1));
                questions.add( cursor.getInt(0) + ".  " + cursor.getString(2)
                + "\nOp1: " + cursor.getString(3) + "\nOp2: " + cursor.getString(4)+ "\nOp3: " + cursor.getString(5)
                                + "\nOp4: " + cursor.getString(6)+ "\nOp5: " + cursor.getString(7)
                                + "\nAns: " + cursor.getString(8)
                );
            } while (cursor.moveToNext());
        }


        // closing connection
        cursor.close();
        db.close();
        return questions;
    }

    //    public List<FillValues> getAllClasses() {
//
//        List<FillValues> userclass = new ArrayList<FillValues>();
//
//        String selectAllClasses = "SELECT " + COLUMN_SLOT_DAY + " , " + COLUMN_SLOT_START_TIME + " , "
//                + COLUMN_SLOT_END_TIME + " , " + COLUMN_TT_SLOT + " , " + COLUMN_TT_COURSE_CODE + " , "
//                + COLUMN_COURSE_NAME + " , " + COLUMN_TT_FACULTY + " , " + COLUMN_TT_ROOM + " FROM (SELECT * FROM "
//                + TABLE_TIMETABLE + " ), " + TABLE_SLOTS + " , " + TABLE_COURSE + " WHERE "
//                + COLUMN_SLOT_NAME + " = " + COLUMN_TT_SLOT + " AND " + COLUMN_COURSE_CODE + " = " + COLUMN_TT_COURSE_CODE
//                + " ORDER BY CASE WHEN "
//                + COLUMN_SLOT_DAY + " = 'SUNDAY' THEN 1 WHEN " + COLUMN_SLOT_DAY + " = 'MONDAY' THEN 2 WHEN "
//                + COLUMN_SLOT_DAY + " = 'TUESDAY' THEN 3 WHEN " + COLUMN_SLOT_DAY + " =  'WEDNESDAY' THEN 4  WHEN "
//                + COLUMN_SLOT_DAY + " = 'THURSDAY' THEN 5 WHEN " + COLUMN_SLOT_DAY + " =  'FRIDAY' THEN 6 WHEN "
//                + COLUMN_SLOT_DAY + " = 'SATURDAY' THEN 7 END ASC," + COLUMN_SLOT_START_TIME;
//
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectAllClasses, null);
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                FillValues v = new FillValues();
//                v.setDay(c.getString(c.getColumnIndex(COLUMN_SLOT_DAY)));
//                v.setStartTime(c.getString(c.getColumnIndex(COLUMN_SLOT_START_TIME)) + " 00 hrs");
//                v.setEndTime(c.getString(c.getColumnIndex(COLUMN_SLOT_END_TIME)) + " 00 hrs");
//                v.setSlot(c.getString(c.getColumnIndex(COLUMN_TT_SLOT)));
//                v.setCourseCode(c.getString(c.getColumnIndex(COLUMN_TT_COURSE_CODE)));
//                v.setCourseName(c.getString(c.getColumnIndex(COLUMN_COURSE_NAME)));
//                v.setFaculty(c.getString(c.getColumnIndex(COLUMN_TT_FACULTY)));
//                v.setRoom(c.getString((c.getColumnIndex(COLUMN_TT_ROOM))));
//
//                // adding to fillvalues list
//                userclass.add(v);
//            } while (c.moveToNext());
//        }
//        c.close();
//        db.close();
//
//        // return user list
//        return userclass;
//    }
}

   // ______________________________________________________________________________________________________________________________________
    /* select slot_name as slot,day,time,tt_course_code as course,faculty_name as faculty,room,faculty_cabin as cabin from faculty1, (select
slot_name,day,time,tt_faculty,tt_course_code,room from (Select * from slots),timetable1 where tt_slot = slot_name) where tt_faculty = faculty_name
and tt_course_code = faculty_course order by case WHEN Day = 'Sunday' THEN 1 WHEN Day = 'Monday' THEN 2 WHEN Day = 'Tuesday' THEN 3
WHEN Day = 'Wednesday' THEN 4  WHEN Day = 'Thursday' THEN 5 WHEN Day = 'Friday' THEN 6 WHEN Day = 'Saturday' THEN 7 end asc,time ;*/
