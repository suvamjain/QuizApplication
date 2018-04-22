package suvamjain.example.com.quizapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import suvamjain.example.com.quizapp.sql.DatabaseHelper;

/**
 * Created by SUVAM JAIN on 22-04-2018.
 */

public class addQues extends AppCompatActivity {

    private static final String TAG = "AddQuestionActivity";

    //widgets
    private Button mSave;
    private DatabaseHelper db;
    private EditText Question, option1, option2,option3,option4,option5;
    private ImageView mBackArrow;
    private RadioGroup radioGroup;
    private RadioButton ans;
    private int checkFields=-1;

    //action bar
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getSupportActionBar();
//        actionBar.hide();

        setContentView(R.layout.activity_filters);
        mSave = (Button) findViewById(R.id.btnSave);
        Question = (EditText) findViewById(R.id.quest);
        option1 = (EditText) findViewById(R.id.option1);
        option2 = (EditText) findViewById(R.id.option2);
        option3 = (EditText) findViewById(R.id.option3);
        option4 = (EditText) findViewById(R.id.option4);
        option5 = (EditText) findViewById(R.id.option5);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mBackArrow = (ImageView) findViewById(R.id.backArrow);
        db = new DatabaseHelper(this);
        init();
    }

    private void init(){

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: saving...");

                if (Question.getText().toString().trim().equals("")) {
                    Toast.makeText(addQues.this, "Please Enter a question", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (option1.getText().toString().trim().equals("")) {
                    Toast.makeText(addQues.this, "Please Enter option 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (option2.getText().toString().trim().equals("")) {
                    Toast.makeText(addQues.this, "Please Enter option 2", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (option3.getText().toString().trim().equals("")) {
                    Toast.makeText(addQues.this, "Please Enter option 3", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (option4.getText().toString().trim().equals("")) {
                    Toast.makeText(addQues.this, "Please Enter option 4", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (option5.getText().toString().trim().equals("")) {
                    Toast.makeText(addQues.this, "Please Enter option 5", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    checkFields = 1;

                int selectedAns = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                ans = (RadioButton) findViewById(selectedAns);

                if (checkFields == 1){
                    long check = db.addques("General",
                            "" + Question.getText().toString().trim(),
                            "" + option1.getText().toString().trim(),
                            "" + option2.getText().toString().trim(),
                            "" + option3.getText().toString().trim(),
                            "" + option4.getText().toString().trim(),
                            "" + option5.getText().toString().trim(),
                            Integer.parseInt(ans.getText().toString()));

                    if(check == -1)
                        Toast.makeText(addQues.this, "Ques already exists", Toast.LENGTH_SHORT).show();
                    Toast.makeText(addQues.this,"Question Saved",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back.");
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onClick: navigating back.");
        finish();
    }
}
