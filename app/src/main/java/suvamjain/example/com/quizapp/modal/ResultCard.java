package suvamjain.example.com.quizapp.modal;

/**
 * Created by SUVAM JAIN on 20-04-2017.
 */

public class ResultCard {

    private String Question;
    private String myAns;
    private String corrAns;
    private String status;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getMyAns() {
        return myAns;
    }

    public void setMyAns(String myAns) {
        this.myAns = myAns;
    }

    public String getCorrAns() {
        return corrAns;
    }

    public void setCorrAns(String corrAns) {
        this.corrAns = corrAns;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
