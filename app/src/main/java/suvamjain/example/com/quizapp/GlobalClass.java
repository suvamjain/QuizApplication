package suvamjain.example.com.quizapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import suvamjain.example.com.quizapp.modal.QuesInfo;

/**
 * Created by SUVAM JAIN on 21-04-2018.
 */

public class GlobalClass extends android.app.Application {

    public ArrayList<QuesInfo> questions = new ArrayList<>();
    public ArrayList<String> savedAnswers = new ArrayList<>();
    public int quesNo=1;
    public int HighScore = 0;
    private Context mContext;
    public SharedPreferences mPrefs;
    public SharedPreferences.Editor prefsEditor;
    public Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mPrefs = mContext.getSharedPreferences("ques", MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        gson = new Gson();
    }

    public void setQuestions(ArrayList<QuesInfo> q){
        String Quesjson = gson.toJson(q);
        prefsEditor.putString("Questions", Quesjson);
        prefsEditor.commit();
    }

    public ArrayList<QuesInfo> getQuestions() {

        //added so that everything is fetched using shared prefs
        String quesInprefs = getSharedPreferences("ques", MODE_PRIVATE).getString("Questions", null);
        Type type = new TypeToken< ArrayList < QuesInfo >>() {}.getType();
        questions = new Gson().fromJson(quesInprefs, type);
        if (questions == null)
            questions = new ArrayList<>();
        return questions;
    }

    public void setSavedAnswers(ArrayList<String> q){
        String ansjson = gson.toJson(q);
        prefsEditor.putString("SavedAnswers", ansjson);
        prefsEditor.commit();
    }

    public ArrayList<String> getSavedAnswers() {

        //added so that everything is fetched using shared prefs
        String ansInprefs = getSharedPreferences("ques", MODE_PRIVATE).getString("SavedAnswers", null);
        Type type = new TypeToken< ArrayList < String >>() {}.getType();
        savedAnswers = new Gson().fromJson(ansInprefs, type);
        if (savedAnswers == null)
            savedAnswers = new ArrayList<>();
        return savedAnswers;
    }

    public void clearPrefs(){
        quesNo = 1;
        prefsEditor.clear();
        prefsEditor.commit();
    }


    public int getQuesNo() { return quesNo;  }
    public void setQuesNo(int qno) {  this.quesNo = qno; }

    public int getHighScore() {
        return HighScore;
    }

    public void setHighScore(int highScore) {
        HighScore = highScore;
    }


}
