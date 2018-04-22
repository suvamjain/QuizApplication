package suvamjain.example.com.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import suvamjain.example.com.quizapp.modal.QuesInfo;
import suvamjain.example.com.quizapp.modal.ResultCard;
import suvamjain.example.com.quizapp.sql.DatabaseHelper;

public class ResultsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ArrayList<QuesInfo> allQues;
    private ArrayList<String> savedAnswers;
    private RecyclerView recyclerViewResults;
    private List<ResultCard> resultsList;
    private ResultsInfoRecyclerAdapter ResultsRecyclerAdapter;
    private AppCompatTextView scoreValue;
    int scoreCount = 0;
    int BestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        scoreValue = findViewById(R.id.scoreValue);
        recyclerViewResults = (RecyclerView) findViewById(R.id.recyclerViewResults);
        resultsList = new ArrayList<ResultCard>();
        ResultsRecyclerAdapter = new ResultsInfoRecyclerAdapter(this,resultsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewResults.setLayoutManager(mLayoutManager);
        recyclerViewResults.setItemAnimator(new DefaultItemAnimator());
        recyclerViewResults.setHasFixedSize(true);
        recyclerViewResults.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewResults.setAdapter(ResultsRecyclerAdapter);

        db = new DatabaseHelper(this);
        allQues = new ArrayList<QuesInfo>();
        savedAnswers = new ArrayList<String>();
        allQues = ((GlobalClass) getApplicationContext()).getQuestions();
        savedAnswers = ((GlobalClass) getApplicationContext()).getSavedAnswers();
        BestScore = ((GlobalClass) getApplicationContext()).getHighScore();
        getResults();
    }

    private void getResults() {

        if (resultsList.size()>0)
            resultsList.clear();

        for (int i=0;i<allQues.size();i++){
            ResultCard r = new ResultCard();
            r.setQuestion("Q" + (i+1) + "  " + allQues.get(i).getQuestion());
            int savedAnsNo = Integer.parseInt(savedAnswers.get(i));

            if (savedAnsNo != -1 && savedAnsNo > 0) { //case when ans is selected
                r.setMyAns("Your Answer:  " + allQues.get(i).getOptions().get(savedAnsNo - 1));

                if (allQues.get(i).getAns().equals(savedAnswers.get(i))) {
                    scoreCount++;
                    r.setStatus("1"); // 1 indicates correct ans
                } else
                    r.setStatus("0"); //0 indicates wrong ans
            }
            else {
                r.setMyAns("Your Answer:  ---");
                //case when no ans is selected )
                r.setStatus("0"); //0 indicates wrong ans as nothing selected
            }

            int corrAnsNo = Integer.parseInt(allQues.get(i).getAns());
            r.setCorrAns("Correct Answer:  " + allQues.get(i).getOptions().get(corrAnsNo - 1));


            resultsList.add(r);
        }

        scoreValue.setText("" + scoreCount + " out of " + allQues.size());
        if (scoreCount > BestScore)
            ((GlobalClass) getApplicationContext()).setHighScore(scoreCount);
        ResultsRecyclerAdapter.notifyDataSetChanged();
    }

    public void goHome(View view) {
        ((GlobalClass) getApplicationContext()).clearPrefs();
        finish();
    }


    @Override
    public void onBackPressed() {
        ((GlobalClass) getApplicationContext()).clearPrefs();
        finish();
    }

}
