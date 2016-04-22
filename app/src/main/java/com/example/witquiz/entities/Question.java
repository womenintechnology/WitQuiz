package com.example.witquiz.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable{

    private int id;
    private int categoryId;
    private String question;
    private Answer[] answers;
    private int answerId;

    public Question() {
        this.answers = new Answer[4];
    }

    public Question(int id, int categoryId, String question, Answer[] answers, int answerId) {
        this.id = id;
        this.categoryId = categoryId;
        this.question = question;
        this.answers = answers;
        this.answerId = answerId;
    }

    public Question(Parcel in) {

        this.id = in.readInt();
        this.categoryId = in.readInt();
        this.question = in.readString();
        this.answers = in.createTypedArray(Answer.CREATOR);
        this.answerId = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.categoryId);
        dest.writeString(this.question);
        dest.writeTypedArray(this.answers, flags);
        dest.writeInt(this.answerId);

    }

    public static final Parcelable.Creator<Question> CREATOR =
            new Parcelable.Creator<Question>() {

                public Question createFromParcel(Parcel in) {
                    return new Question(in);
                }

                public Question[] newArray(int size) {
                    return new Question[size];
                }
            };
}
