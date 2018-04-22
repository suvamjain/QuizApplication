package suvamjain.example.com.quizapp;

/**
 * Created by SUVAM JAIN on 22-04-2018.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import suvamjain.example.com.quizapp.helpers.InputValidation;
import suvamjain.example.com.quizapp.helpers.SessionManager;
import suvamjain.example.com.quizapp.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkForget;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private Cursor noOfUser;
    int userRegistered;

    private ProgressBar login;

    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
//        Toast.makeText(this,"" + userRegistered,Toast.LENGTH_SHORT).show();

        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        // Setting Dialog Title
        alertDialog2.setTitle("Login Credintials - ");
        // Setting Dialog Message
        alertDialog2.setMessage("Email - admin\nPassword - admin123");
        alertDialog2.setCancelable(true);
        // Setting Positive "Yes" Button
        alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // Showing Alert Message
        alertDialog2.show();

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            Intent intent = new Intent(this, LoginActivity.class);
            Toast.makeText(activity, "Activity not executed", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            ed.commit();
        }
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkForget = (AppCompatTextView) findViewById(R.id.textViewLinkForget);

        login = (ProgressBar) findViewById(R.id.progressBar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkForget.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        userRegistered = 0;
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
        noOfUser = databaseHelper.getDetails();
        userRegistered = noOfUser.getCount();

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkForget:
                Toast.makeText(activity, "Username: admin\nPassword: admin123", Toast.LENGTH_LONG).show();
                emptyInputEditText();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
                if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, "Enter Username")) {
                    return;
                }
                if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, "Enter Valid Username")) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, "Enter Valid Password")) {
                    return;
                }
                if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim().toUpperCase()
                        , textInputEditTextPassword.getText().toString().trim()))
                {
                    session.createLoginSession(textInputEditTextEmail.getText().toString().trim().toUpperCase(),
                            textInputEditTextPassword.getText().toString().trim());
                    new SyncData().execute();

                } else {
                    // Snack Bar to show success message that record is wrong
                    if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().toUpperCase().trim())) {
                        Snackbar.make(nestedScrollView, "Username doesn't exists",Snackbar.LENGTH_LONG).show();
                    } else
                    Snackbar.make(nestedScrollView, "Incorrect Username or Password", Snackbar.LENGTH_LONG).show();
                }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }

    /**
     * Async task to load the data from server
     * **/
    private class SyncData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // set the progress bar view.
            login.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // not making real request in this demo
            // for now we use a timer to wait for sometime
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            login.setVisibility(View.GONE);
            Cursor res = databaseHelper.search(textInputEditTextEmail.getText().toString().trim().toUpperCase());
            StringBuffer buffer = new StringBuffer();
            if (res.moveToFirst()) {
                do{
                    buffer.append(res.getString(1));
                }while (res.moveToNext());
            }
            finish();
            Toast.makeText(getApplicationContext(),"You are Logged in Succesfully", Toast.LENGTH_SHORT).show();
            Intent accountsIntent = new Intent(activity, MainActivity.class);
            emptyInputEditText();
            startActivity(accountsIntent);


        }
    }

    @Override
    public void onBackPressed() {
        Snackbar.make(nestedScrollView,"You have reached the Login page", Snackbar.LENGTH_SHORT).show();
    }

}
