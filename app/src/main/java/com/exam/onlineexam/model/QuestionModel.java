package com.exam.onlineexam.model;

import java.io.Serializable;

public class QuestionModel implements Serializable{

    private String question;
    private int seleectedAnswerPosition;
    public String option1 = "";
    public String option2 = "";
    public String option3 = "";
    public String option4 = "";
    public String ans = "";
    private boolean op1Sel, op2Sel, op3Sel, op4Sel; // options

    public boolean isOp1Sel() {
        return op1Sel;
    }

    public void setOp1Sel(boolean op1Sel) {
        this.op1Sel = op1Sel;
        if (op1Sel) { // To make sure only one option is selected at a time
            setOp2Sel(false);
            setOp3Sel(false);
            setOp4Sel(false);
        }
    }

    public boolean isOp2Sel() {
        return op2Sel;
    }

    public void setOp2Sel(boolean op2Sel) {
        this.op2Sel = op2Sel;
        if (op2Sel) {
            setOp1Sel(false);
            setOp3Sel(false);
            setOp4Sel(false);
        }
    }

    public boolean isOp3Sel() {
        return op3Sel;
    }

    public void setOp3Sel(boolean op3Sel) {
        this.op3Sel = op3Sel;
        if (op3Sel) {
            setOp2Sel(false);
            setOp1Sel(false);
            setOp4Sel(false);
        }
    }

    public boolean isOp4Sel() {
        return op4Sel;
    }

    public void setOp4Sel(boolean op4Sel) {
        this.op4Sel = op4Sel;
        if (op4Sel) {
            setOp2Sel(false);
            setOp1Sel(false);
            setOp3Sel(false);
        }
    }

    public int getSeleectedAnswerPosition() {
        return seleectedAnswerPosition;
    }

    public void setSeleectedAnswerPosition(int seleectedAnswerPosition) {
        this.seleectedAnswerPosition = seleectedAnswerPosition;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
