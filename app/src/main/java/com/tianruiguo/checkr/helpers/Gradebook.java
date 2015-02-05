package com.tianruiguo.checkr.helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by tianrui on 2/3/15.
 */
public class Gradebook {
    private final String GRADEBOOK_NUMBER = "gradebookNumber";
    private final String CODE = "code";
    private final String PERIOD = "period";
    private final String MARK = "mark";
    private final String CLASS_NAME = "className";
    private final String MISSING_ASSIGNMENTS = "missingAssignments";
    private final String UPDATED = "updated";
    private final String TREND_DIRECTION = "trendDirection";
    private final String PERCENT_GRADE = "percentGrade";
    private final String COMMENT = "comment";
    private final String IS_USING_CHECK_MARKS = "isUsingCheckMarks";

    private boolean isUsingCheckMarks;

    private Date updated;

    private int gradebookNumber;
    private int missingAssignments;
    private int period;
    private int percentGrade;

    private String code;
    private String mark;
    private String className;
    private String trendDirection;
    private String comment;

    public Gradebook(JSONObject jsonObject) throws JSONException {
        gradebookNumber = jsonObject.getInt(GRADEBOOK_NUMBER);
        code = jsonObject.getString(CODE);
        period = jsonObject.getInt(PERIOD);
        mark = jsonObject.getString(MARK);
        className = jsonObject.getString(CLASS_NAME);
        missingAssignments = jsonObject.getInt(MISSING_ASSIGNMENTS);

        // we get "updated" in the form of "\/Date(1422560175663)\/"
        String date = jsonObject.getString(UPDATED);
        updated = new Date(Long.parseLong(date.substring(date.indexOf("(") + 1, date.indexOf(")"))));

        trendDirection = jsonObject.getString(TREND_DIRECTION);
        percentGrade = jsonObject.getInt(PERCENT_GRADE);
        comment = jsonObject.getString(COMMENT);
        isUsingCheckMarks = jsonObject.getBoolean(IS_USING_CHECK_MARKS);

    }

    public boolean isUsingCheckMarks() {
        return isUsingCheckMarks;
    }

    public Date getUpdated() {
        return updated;
    }

    public int getGradebookNumber() {
        return gradebookNumber;
    }

    public int getMissingAssignments() {
        return missingAssignments;
    }

    public int getPeriod() {
        return period;
    }

    public int getPercentGrade() {
        return percentGrade;
    }

    public String getCode() {
        return code;
    }

    public String getMark() {
        return mark;
    }

    public String getClassName() {
        return className;
    }

    public String getTrendDirection() {
        return trendDirection;
    }

    public String getComment() {
        return comment;
    }

    public String printSimple() {
        return getPeriod() + ". " + getClassName() + " - " + getPercentGrade() + "%";
    }
}
