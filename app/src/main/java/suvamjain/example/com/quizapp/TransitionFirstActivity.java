package suvamjain.example.com.quizapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.transition.Slide;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import suvamjain.example.com.quizapp.modal.QuesInfo;
import suvamjain.example.com.quizapp.sql.DatabaseHelper;

/**
 * Created by SUVAM JAIN on 20-04-2018.
 */

public class TransitionFirstActivity extends Activity {

    private View mFabButton;
    private View mHeader;
    private TextView mNoofQues,HighScore;
    int HighScr = 0;
    private DatabaseHelper db;
    private ArrayList<QuesInfo> allQues;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_first);

        ((GlobalClass) getApplicationContext()).clearPrefs(); //clearing prefereneces

        mFabButton = findViewById(R.id.fab_button);
        mHeader = findViewById(R.id.activity_transition_header);
        mNoofQues = findViewById(R.id.noOfQuestions);
        HighScore = findViewById(R.id.BestScore);
        HighScr = ((GlobalClass) getApplicationContext()).getHighScore();

        HighScore.setText(String.format("%02d",HighScr));
        db = new DatabaseHelper(this);
        allQues = new ArrayList<QuesInfo>();

        getAllQuestionsFromSQLite();

        Slide slideExitTransition = new Slide(Gravity.LEFT); //BOTTOM
        slideExitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        slideExitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideExitTransition.excludeTarget(R.id.activity_transition_header, true);
        // Prevent transitions for overlapping
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        getWindow().setExitTransition(slideExitTransition);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onFabPressed(View view) {

        getAllQuestionsFromSQLite();

        Intent i  = new Intent (TransitionFirstActivity.this,TransitionSecondActivity.class);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                TransitionFirstActivity.this, Pair.create(mFabButton, "fab"), Pair.create(mHeader, "holder1"));

        startActivity(i, transitionActivityOptions.toBundle());
    }


    @SuppressLint("StaticFieldLeak")
    private void getAllQuestionsFromSQLite() {

         if (allQues.size()>0)
             allQues.clear();   //clear the global question array before storing the questions in global array
        // AsyncTask is used so that SQLite operations will not block the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                allQues.addAll(db.getAllQues());
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ((GlobalClass)getApplicationContext()).setQuestions(allQues);
                 mNoofQues.setText("Total " + allQues.size() + " questions");
            }
        }.execute();
    }

    public void goHome(View view) {
//        Intent i = new Intent(this,MainActivity.class);
//        startActivity(i);
        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        Log.d("TransitionFirst", "onClick: navigating back.");
        finishAfterTransition();
    }

    @Override
    public void onResume(){
        super.onResume();
        HighScr = ((GlobalClass) getApplicationContext()).getHighScore(); //getting high score for sync
        HighScore.setText(String.format("%02d",HighScr));

    }
}