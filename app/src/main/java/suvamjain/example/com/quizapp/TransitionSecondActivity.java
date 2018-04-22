package suvamjain.example.com.quizapp;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.transition.Transition;
import android.util.Pair;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import suvamjain.example.com.quizapp.modal.QuesInfo;
import suvamjain.example.com.quizapp.sql.DatabaseHelper;
import suvamjain.example.com.quizapp.utils.AnimatorAdapter;
import suvamjain.example.com.quizapp.utils.TransitionAdapter;

/**
 * Created by SUVAM JAIN on 20-04-2018.
 */

public class TransitionSecondActivity extends Activity {

    private static final int SCALE_DELAY = 30;
    private LinearLayout rowContainer;
    private View mFabButton;
    private TextView ques;
    private DatabaseHelper db;
    private ArrayList<QuesInfo> allQues,allglobalQues;
    private ArrayList<String> savedAnswers;
    private int q_no;
    private Button qbtn;
    private ArrayAdapter<QuesInfo> quesAdapter;
    private ImageButton next;
    private String ansSelected = "-1"; //initializing with -1 so that if no ans selected we can verify;

    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private CountDownTimer countDownTimer;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_second);

        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        mFabButton = findViewById(R.id.fab_button_second);
        rowContainer = (LinearLayout) findViewById(R.id.row_container2);
        next = (ImageButton) findViewById(R.id.next);
        qbtn = (Button) findViewById(R.id.fab_button_second);
        ques = findViewById(R.id.Ques);
        db = new DatabaseHelper(this);
        allQues = new ArrayList<QuesInfo>();
        savedAnswers = new ArrayList<String>();
        allglobalQues = ((GlobalClass) getApplicationContext()).getQuestions();
        savedAnswers = ((GlobalClass) getApplicationContext()).getSavedAnswers();
        q_no = ((GlobalClass) getApplicationContext()).getQuesNo();

        if (q_no == allglobalQues.size())
            next.setImageResource(R.drawable.ic_panel);

        // Prevent transitions for overlapping
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);

        getWindow().getEnterTransition().setStartDelay(700).addListener(new TransitionAdapter() {

            @Override
            public void onTransitionEnd(Transition transition) {

                super.onTransitionEnd(transition);
                getWindow().getEnterTransition().removeListener(this);

                ques.setVisibility(View.VISIBLE);
                ques.setText(allglobalQues.get(q_no-1).getQuestion()); //using q_no-1 as arraylist is zero-indexed and q_no starts from 1.
                qbtn.setText("Q" + q_no);

                //load options after 1 sec
                Handler handler = new Handler();
                final int[] i = new int[1];
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (i[0] = 0; i[0] < rowContainer.getChildCount(); i[0]++) {
                            View  rowView = rowContainer.getChildAt(i[0]);
                            Button opt = (Button) rowView;
                            opt.setText(i[0] +1 + ".  " + allglobalQues.get(q_no-1).getOptions().get(i[0]));
                            rowView.animate().setStartDelay(i[0] * SCALE_DELAY)
                                    .scaleX(1).scaleY(1);
                        }
                        if(i[0] == rowContainer.getChildCount()) {
                            startStop();
                            next.setVisibility(View.VISIBLE);
                        }
                    }
                }, 700);
            }
        });
    }

    @Override
    public void onBackPressed() {

        final long time = Long.parseLong(textViewTime.getText().toString());
//        Toast.makeText(this, "time " + time, Toast.LENGTH_SHORT).show();
        timerStatus = TimerStatus.STOPPED;
        stopCountDownTimer();

        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(TransitionSecondActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        // Setting Dialog Title
        alertDialog2.setTitle("Quit Quiz");
        // Setting Dialog Message
        alertDialog2.setMessage("Are you sure you want to quit the quiz?\nYour attempt will not be saved");

        // Setting Positive "Yes" Button
        alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < rowContainer.getChildCount(); i++) {

                    View rowView = rowContainer.getChildAt(i);
                    ViewPropertyAnimator propertyAnimator = rowView.animate()
                            .setStartDelay(i * SCALE_DELAY)
                            .scaleX(0).scaleY(0)
                            .setListener(new AnimatorAdapter() {

                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    ques.setVisibility(View.GONE);
//                                    stopCountDownTimer();
                                    progressBarCircle.setVisibility(View.GONE);
                                    textViewTime.setVisibility(View.GONE);
                                    next.setVisibility(View.GONE);
                                    ((GlobalClass) getApplicationContext()).clearPrefs(); //clearing prefereneces
                                    finishAfterTransition();
                                }
                            });
                }
            }
        });

        // Setting Negative "NO" Button
        alertDialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // changing the timer status to started
                timerStatus = TimerStatus.STARTED;
                // call to start the count down timer
                startCountDownTimer(time*1000);
            }
        });

        // Showing Alert Message
        alertDialog2.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onFabPressedSec(View view) {

        if (q_no < allglobalQues.size()){
            ((GlobalClass) getApplicationContext()).setQuesNo(q_no + 1);

            savedAnswers.add(ansSelected);
            ((GlobalClass)getApplicationContext()).setSavedAnswers(savedAnswers); //adding ans to savedAns global array.

            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    TransitionSecondActivity.this, Pair.create(mFabButton, "fab"));
            ques.setVisibility(View.GONE);
            stopCountDownTimer();
            startActivity(getIntent(),transitionActivityOptions.toBundle());
            finishAfterTransition();
        }
        else {  //we have reached the last question
            savedAnswers.add(ansSelected);
            ((GlobalClass)getApplicationContext()).setSavedAnswers(savedAnswers); //adding ans to savedAns global array.
            stopCountDownTimer();

            Intent i = new Intent (TransitionSecondActivity.this,ResultsActivity.class);
            startActivity(i);
            finishAfterTransition();
        }
    }


    private void unselectOptions(LinearLayout root, View except){
        Integer childCount = root.getChildCount();
        for( int i=0; i<childCount; i++){
            Button child = (Button) root.getChildAt(i);
            if (child != except) {
                child.setSelected(false);
            }
            if (child == except) {
                child.setSelected(true);
                ansSelected = "" + child.getTag();
                //Toast.makeText(this, "pressed text " + child.getText() + "Tag " + child.getTag(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void changeColor(View view) {
        unselectOptions(rowContainer,view);
    }

    /**
     * method to start and stop count down timer
     */
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer(timeCountInMilliSeconds);

        } else {
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues() {
        timeCountInMilliSeconds = 20000; //20 sec timer.
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer(long tm) {

        countDownTimer = new CountDownTimer(tm, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
//                setProgressBarValues();
                progressBarCircle.setVisibility(View.GONE);
                textViewTime.setVisibility(View.GONE);
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
                onFabPressedSec(next);
            }
        }.start();
        countDownTimer.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    /**
     * method to convert millisecond to time format
     * @return sec formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {
//        "%02d:%02d:%02d"
        String hms = String.format("%02d",
//                TimeUnit.MILLISECONDS.toHours(milliSeconds),
//                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }
}
