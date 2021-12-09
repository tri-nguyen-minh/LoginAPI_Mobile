package dev.source.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Grade implements Serializable {
    private String username, subject;
    private double grade;

    public Grade() {
    }

    public Grade(String username, String subject, double grade) {
        this.username = username;
        this.subject = subject;
        this.grade = grade;
    }

    public static Grade getGradeFromJSON(JSONObject jsonObject) throws Exception {
        String username = jsonObject.getString("username");
        String subject = jsonObject.getString("subject");
        double grade = jsonObject.getDouble("grade");
        return new Grade(username, subject, grade);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
