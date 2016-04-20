package com.example.witquiz.entities;

public class Answer {

    private int id;
    private  int questionId;
    private String answer;

    public Answer() {
    }

    public Answer(int id, int questionId, String answer) {
        this.id = id;
        this.questionId = questionId;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
