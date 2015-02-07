package com.tianruiguo.checkr.helpers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by tianrui on 2/3/15.
 */
public class Assignment {
    private final String GRADEBOOK_NUMBER = "gradebookNumber";
    private final String ASSIGNMENT_NUMBER = "assignmentNumber";
    private final String DESCRIPTION = "description";
    private final String TYPE = "type";
    private final String IS_GRADED = "isGraded";
    private final String IS_SCORE_VISIBLE_TO_PARENTS = "isScoreVisibleToParents";
    private final String IS_SCORE_VALUE_A_CHECK_MARK = "isScoreValueACheckMark";
    private final String NUMBER_CORRECT = "numberCorrect";
    private final String NUMBER_POSSIBLE = "numberPossible";
    private final String MARK = "mark";
    private final String SCORE = "score";
    private final String MAX_SCORE = "maxScore";
    private final String PERCENT = "percent";
    private final String DATE_ASSIGNED = "dateAssigned";
    private final String DATE_DUE = "dateDue";
    private final String DATE_COMPLETED = "dateCompleted";

    private int gradebookNumber;
    private int assignmentNumber;
    private String description;
    private String type;
    private boolean isGraded;
    private boolean isScoreVisibleToParents;
    private boolean isScoreValueACheckMark;
    private int numberCorrect;
    private int numberPossible;
    private String mark;
    private int score;
    private int maxScore;
    private int percent;
    private Date dateAssigned;
    private Date dateDue;
    private Date dateCompleted;

    public Assignment() {
    }

    public Assignment(JSONObject json) throws JSONException {
        gradebookNumber = json.getInt(GRADEBOOK_NUMBER);
        assignmentNumber = json.getInt(ASSIGNMENT_NUMBER);
        description = json.getString(DESCRIPTION);
        type = json.getString(TYPE);
        isGraded = json.getBoolean(IS_GRADED);
        isScoreVisibleToParents = json.getBoolean(IS_SCORE_VISIBLE_TO_PARENTS);
        isScoreValueACheckMark = json.getBoolean(IS_SCORE_VALUE_A_CHECK_MARK);
        numberCorrect = json.getInt(NUMBER_CORRECT);
        numberPossible = json.getInt(NUMBER_POSSIBLE);
        mark = json.getString(MARK);
        score = json.getInt(SCORE);
        maxScore = json.getInt(MAX_SCORE);
        percent = json.getInt(PERCENT);

        String date1 = json.getString(DATE_ASSIGNED);
        dateAssigned = new Date(Long.parseLong(date1.substring(date1.indexOf("(") + 1, date1.indexOf(")"))));
        String date2 = json.getString(DATE_DUE);
        dateDue = new Date(Long.parseLong(date2.substring(date2.indexOf("(") + 1, date2.indexOf(")"))));
        String date3 = json.getString(DATE_COMPLETED);
        try {
            dateCompleted = new Date(Long.parseLong(date3.substring(date3.indexOf("(") + 1, date3.indexOf(")"))));
        } catch (StringIndexOutOfBoundsException e) {
            // Sometimes date is "null"
            // TODO: properly handle this
            Log.e("JSON", date3);
        }

    }

    public int getGradebookNumber() {
        return gradebookNumber;
    }

    public void setGradebookNumber(int gradebookNumber) {
        this.gradebookNumber = gradebookNumber;
    }

    public int getAssignmentNumber() {
        return assignmentNumber;
    }

    public void setAssignmentNumber(int assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isGraded() {
        return isGraded;
    }

    public void setGraded(boolean isGraded) {
        this.isGraded = isGraded;
    }

    public boolean isScoreVisibleToParents() {
        return isScoreVisibleToParents;
    }

    public void setScoreVisibleToParents(boolean isScoreVisibleToParents) {
        this.isScoreVisibleToParents = isScoreVisibleToParents;
    }

    public boolean isScoreValueACheckMark() {
        return isScoreValueACheckMark;
    }

    public void setScoreValueACheckMark(boolean isScoreValueACheckMark) {
        this.isScoreValueACheckMark = isScoreValueACheckMark;
    }

    public int getNumberCorrect() {
        return numberCorrect;
    }

    public void setNumberCorrect(int numberCorrect) {
        this.numberCorrect = numberCorrect;
    }

    public int getNumberPossible() {
        return numberPossible;
    }

    public void setNumberPossible(int numberPossible) {
        this.numberPossible = numberPossible;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public Date getDateAssigned() {
        return dateAssigned;
    }

    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String printSimple() {
        return getAssignmentNumber() + ". " + getDescription() + " - " +
                getType() + " - " + getNumberCorrect() + "/" + getNumberPossible() +
                " (" + getPercent() + "%)";
    }
}
