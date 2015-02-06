package com.tianruiguo.checkr.helpers.datastuff;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tianruiguo.checkr.helpers.datastuff.GradebookContract.Assignment;
import com.tianruiguo.checkr.helpers.datastuff.GradebookContract.GradebookEntry;

/**
 * Created by tianrui on 2/5/15.
 */
public class GradebookDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public static final String DB_NAME = "gradebook.db";

    public GradebookDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GRADEBOOK_TABLE =
                "CREATE TABLE " + GradebookEntry.TABLE_NAME + " (" +
                        GradebookEntry.COLUMN_GRADEBOOK_NUMBER + " INTEGER PRIMARY KEY," +
                        GradebookEntry.COLUMN_CODE + " TEXT," +
                        GradebookEntry.COLUMN_PERIOD + " INTEGER," +
                        GradebookEntry.COLUMN_MARK + " TEXT," +
                        GradebookEntry.COLUMN_CLASS_NAME + " TEXT," +
                        GradebookEntry.COLUMN_MISSING_ASSIGNMENTS + " INTEGER," +
                        GradebookEntry.COLUMN_UPDATED + " INTEGER," +
                        GradebookEntry.COLUMN_TREND_DIRECTION + " TEXT," +
                        GradebookEntry.COLUMN_PERCENT_GRADE + " INTEGER," +
                        GradebookEntry.COLUMN_COMMENT + " TEXT," +
                        GradebookEntry.COLUMN_IS_USING_CHECK_MARKS + " BOOLEAN," +
                        " UNIQUE (" + GradebookEntry.COLUMN_GRADEBOOK_NUMBER + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_ASSIGNMENT_TABLE =
                "CREATE TABLE " + Assignment.TABLE_NAME + " (" +
                        Assignment.COLUMN_ASSIGNMENT_NUMBER + " INTEGER PRIMARY KEY," +
                        Assignment.COLUMN_GRADEBOOK_NUMBER + " INTEGER," +
                        Assignment.COLUMN_DESCRIPTION + " TEXT," +
                        Assignment.COLUMN_TYPE + " TEXT," +
                        Assignment.COLUMN_IS_GRADED + " BOOLEAN," +
                        Assignment.COLUMN_IS_SCORE_VISIBLE_TO_PARENTS + " BOOLEAN," +
                        Assignment.COLUMN_IS_SCORE_VALUE_A_CHECK_MARK + " BOOLEAN," +
                        Assignment.COLUMN_NUMBER_CORRECT + " INTEGER," +
                        Assignment.COLUMN_NUMBER_POSSIBLE + " INTEGER," +
                        Assignment.COLUMN_MARK + " TEXT," +
                        Assignment.COLUMN_SCORE + " INTEGER," +
                        Assignment.COLUMN_MAX_SCORE + " INTEGER," +
                        Assignment.COLUMN_PERCENT + " INTEGER," +
                        Assignment.COLUMN_DATE_ASSIGNED + " INTEGER," +
                        Assignment.COLUMN_DATE_DUE + " INTEGER," +
                        Assignment.COLUMN_DESCRIPTION + " INTEGER," +
                        Assignment.COLUMN_DATE_COMPLETED + " INTEGER," +
                        " FOREIGN KEY (" + Assignment.COLUMN_GRADEBOOK_NUMBER + ") REFERENCES " +
                        GradebookEntry.TABLE_NAME + " (" + GradebookEntry.COLUMN_GRADEBOOK_NUMBER + "), " +
                        " UNIQUE (" + Assignment.COLUMN_ASSIGNMENT_NUMBER + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_GRADEBOOK_TABLE);
        db.execSQL(SQL_CREATE_ASSIGNMENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GradebookEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Assignment.TABLE_NAME);
        onCreate(db);
    }
}
