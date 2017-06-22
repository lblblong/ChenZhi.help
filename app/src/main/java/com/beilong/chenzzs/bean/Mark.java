package com.beilong.chenzzs.bean;

/**
 * Created by LBL on 2016/11/4.
 */

public class Mark {
    String term;
    String fraction;
    String subject;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "term='" + term + '\'' +
                ", fraction='" + fraction + '\'' +
                ", subject='" + subject + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
