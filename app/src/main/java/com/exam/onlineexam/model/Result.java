package com.exam.onlineexam.model;

public class Result {
    public String testName = "";
    public String testDate = "";
    public int testMarks = 0;
    public int questionAttemted = 0;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
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

    public static Result createResult(String testName, String date, int marks, int questionAttemted) {
        Result result = new Result();
        result.testName = testName;
        result.testDate = date;
        result.testMarks = marks;
        result.questionAttemted = questionAttemted;
        return result;
    }
}
