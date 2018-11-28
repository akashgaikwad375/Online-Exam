package com.exam.onlineexam.model;

public class Result {
    public String email = "";
    public String testDate = "";
    public int testMarks = 0;
    public int questionAttemted = 0;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public int getTestMarks() {
        return testMarks;
    }

    public void setTestMarks(int testMarks) {
        this.testMarks = testMarks;
    }

    public int getQuestionAttemted() {
        return questionAttemted;
    }

    public void setQuestionAttemted(int questionAttemted) {
        this.questionAttemted = questionAttemted;
    }

    public static Result createResult(String email, String date, int marks, int questionAttemted) {
        Result result = new Result();
        result.email = email;
        result.testDate = date;
        result.testMarks = marks;
        result.questionAttemted = questionAttemted;
        return result;
    }
}
