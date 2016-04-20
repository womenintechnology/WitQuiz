package com.example.witquiz.entities;

public class Question {

    private int id;
    private int categoryId;
    private String question;
    private Answer[] answers;
    private int answerId;

    public Question() {
    }

    public Question(int id, int categoryId, String question, Answer[] answers, int answerId) {
        this.id = id;
        this.categoryId = categoryId;
        this.question = question;
        this.answerId = answerId;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }
}
