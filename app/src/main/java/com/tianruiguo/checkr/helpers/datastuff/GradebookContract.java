package com.tianruiguo.checkr.helpers.datastuff;

import android.provider.BaseColumns;

/**
 * Created by tianrui on 2/5/15.
 */
public class GradebookContract {
    public static final class GradebookEntry implements BaseColumns {
        public static final String TABLE_NAME = "gradebook";

        public static final String COLUMN_GRADEBOOK_NUMBER = "gradebookNumber";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_PERIOD = "period";
        public static final String COLUMN_MARK = "mark";
        public static final String COLUMN_CLASS_NAME = "className";
        public static final String COLUMN_MISSING_ASSIGNMENTS = "missingAssignments";
        public static final String COLUMN_UPDATED = "updated";
        public static final String COLUMN_TREND_DIRECTION = "trendDirection";
        public static final String COLUMN_PERCENT_GRADE = "percentGrade";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_IS_USING_CHECK_MARKS = "isUsingCheckMarks";
    }

    public static final class AssignmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "assignment";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_GRADEBOOK_NUMBER = "gradebookNumber";
        public static final String COLUMN_ASSIGNMENT_NUMBER = "assignmentNumber";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IS_GRADED = "isGraded";
        public static final String COLUMN_IS_SCORE_VISIBLE_TO_PARENTS = "isScoreVisibleToParents";
        public static final String COLUMN_IS_SCORE_VALUE_A_CHECK_MARK = "isScoreValueACheckMark";
        public static final String COLUMN_NUMBER_CORRECT = "numberCorrect";
        public static final String COLUMN_NUMBER_POSSIBLE = "numberPossible";
        public static final String COLUMN_MARK = "mark";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_MAX_SCORE = "maxScore";
        public static final String COLUMN_PERCENT = "percent";
        public static final String COLUMN_DATE_ASSIGNED = "dateAssigned";
        public static final String COLUMN_DATE_DUE = "dateDue";
        public static final String COLUMN_DATE_COMPLETED = "dateCompleted";
    }
}
