package com.tianruiguo.checkr.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tianruiguo.checkr.helpers.objects.Assignment;
import com.tianruiguo.checkr.helpers.objects.Gradebook;
import com.tianruiguo.checkr.helpers.database.GradebookContract.AssignmentEntry;
import com.tianruiguo.checkr.helpers.database.GradebookContract.GradebookEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tianrui on 2/6/15.
 */
public class GradebookDataSource {

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;

    public GradebookDataSource(Context context) {
        dbHelper = new GradebookDbHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addGradebook(Gradebook gradebook) {
        ContentValues values = new ContentValues();
        values.put(GradebookEntry.COLUMN_GRADEBOOK_NUMBER, gradebook.getGradebookNumber());
        values.put(GradebookEntry.COLUMN_CODE, gradebook.getCode());
        values.put(GradebookEntry.COLUMN_PERIOD, gradebook.getPeriod());
        values.put(GradebookEntry.COLUMN_MARK, gradebook.getMark());
        values.put(GradebookEntry.COLUMN_CLASS_NAME, gradebook.getClassName());
        values.put(GradebookEntry.COLUMN_MISSING_ASSIGNMENTS, gradebook.getMissingAssignments());
        values.put(GradebookEntry.COLUMN_UPDATED, gradebook.getUpdated().getTime());
        values.put(GradebookEntry.COLUMN_TREND_DIRECTION, gradebook.getTrendDirection());
        values.put(GradebookEntry.COLUMN_PERCENT_GRADE, gradebook.getPercentGrade());
        values.put(GradebookEntry.COLUMN_COMMENT, gradebook.getComment());
        values.put(GradebookEntry.COLUMN_IS_USING_CHECK_MARKS, gradebook.isUsingCheckMarks());
        db.insertWithOnConflict(GradebookEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void addGradebooks(List<Gradebook> gradebooks) {
        for (Gradebook g : gradebooks) {
            addGradebook(g);
        }
    }

    public ArrayList<Gradebook> getGradebooks() {
        ArrayList<Gradebook> gradebooks = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + GradebookEntry.TABLE_NAME +
                " ORDER  BY " + GradebookEntry.COLUMN_PERIOD + " ASC", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Gradebook gradebook = new Gradebook();
                gradebook.setGradebookNumber(cursor.getInt(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_GRADEBOOK_NUMBER)));
                gradebook.setCode(cursor.getString(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_CODE)));
                gradebook.setPeriod(cursor.getInt(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_PERIOD)));
                gradebook.setMark(cursor.getString(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_MARK)));
                gradebook.setClassName(cursor.getString(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_CLASS_NAME)));
                gradebook.setMissingAssignments(cursor.getInt(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_MISSING_ASSIGNMENTS)));
                gradebook.setUpdated(new Date(cursor.getInt(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_UPDATED))));
                gradebook.setTrendDirection(cursor.getString(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_TREND_DIRECTION)));
                gradebook.setPercentGrade(cursor.getInt(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_PERCENT_GRADE)));
                gradebook.setComment(cursor.getString(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_COMMENT)));
                gradebook.setUsingCheckMarks(cursor.getInt(
                        cursor.getColumnIndex(GradebookEntry.COLUMN_IS_USING_CHECK_MARKS)) == 1);
                gradebooks.add(gradebook);
            }
        }

        return gradebooks;
    }

    public void addAssignment(Assignment assignment) {
        ContentValues values = new ContentValues();
        values.put(AssignmentEntry.COLUMN_GRADEBOOK_NUMBER, assignment.getGradebookNumber());
        values.put(AssignmentEntry.COLUMN_ASSIGNMENT_NUMBER, assignment.getAssignmentNumber());
        values.put(AssignmentEntry.COLUMN_DESCRIPTION, assignment.getDescription());
        values.put(AssignmentEntry.COLUMN_TYPE, assignment.getType());
        values.put(AssignmentEntry.COLUMN_IS_GRADED, assignment.isGraded());
        values.put(AssignmentEntry.COLUMN_IS_SCORE_VISIBLE_TO_PARENTS, assignment.isScoreVisibleToParents());
        values.put(AssignmentEntry.COLUMN_IS_SCORE_VALUE_A_CHECK_MARK, assignment.isScoreValueACheckMark());
        values.put(AssignmentEntry.COLUMN_NUMBER_CORRECT, assignment.getNumberCorrect());
        values.put(AssignmentEntry.COLUMN_NUMBER_POSSIBLE, assignment.getNumberPossible());
        values.put(AssignmentEntry.COLUMN_MARK, assignment.getMark());
        values.put(AssignmentEntry.COLUMN_SCORE, assignment.getScore());
        values.put(AssignmentEntry.COLUMN_MAX_SCORE, assignment.getMaxScore());
        values.put(AssignmentEntry.COLUMN_PERCENT, assignment.getPercent());
        values.put(AssignmentEntry.COLUMN_DATE_ASSIGNED, assignment.getDateAssigned().getTime());
        values.put(AssignmentEntry.COLUMN_DATE_DUE, assignment.getDateDue().getTime());
        values.put(AssignmentEntry.COLUMN_DATE_COMPLETED, assignment.getDateCompleted().getTime());

        db.insertWithOnConflict(AssignmentEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void addAssignments(List<Assignment> assignments) {
        for (Assignment a : assignments) {
            addAssignment(a);
        }
    }

    public ArrayList<Assignment> getAssignments(int gradebookId) {
        ArrayList<Assignment> assignments = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + AssignmentEntry.TABLE_NAME +
                " WHERE " + AssignmentEntry.COLUMN_GRADEBOOK_NUMBER + " = " + gradebookId +
                " ORDER BY " + AssignmentEntry.COLUMN_ASSIGNMENT_NUMBER + " DESC", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Assignment assignment = new Assignment();
                assignment.setGradebookNumber(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_GRADEBOOK_NUMBER)));
                assignment.setAssignmentNumber(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_ASSIGNMENT_NUMBER)));
                assignment.setDescription(cursor.getString(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_DESCRIPTION)));
                assignment.setType(cursor.getString(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_TYPE)));
                assignment.setGraded(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_IS_GRADED)) == 1);
                assignment.setScoreVisibleToParents(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_IS_SCORE_VISIBLE_TO_PARENTS)) == 1);
                assignment.setScoreValueACheckMark(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_IS_SCORE_VALUE_A_CHECK_MARK)) == 1);
                assignment.setNumberCorrect(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_NUMBER_CORRECT)));
                assignment.setNumberPossible(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_NUMBER_POSSIBLE)));
                assignment.setMark(cursor.getString(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_MARK)));
                assignment.setScore(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_SCORE)));
                assignment.setMaxScore(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_MAX_SCORE)));
                assignment.setPercent(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_PERCENT)));
                assignment.setDateAssigned(new Date(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_DATE_ASSIGNED))));
                assignment.setDateDue(new Date(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_DATE_DUE))));
                assignment.setDateCompleted(new Date(cursor.getInt(
                        cursor.getColumnIndex(AssignmentEntry.COLUMN_DATE_COMPLETED))));

                assignments.add(assignment);
            }
        }

        return assignments;
    }
}
