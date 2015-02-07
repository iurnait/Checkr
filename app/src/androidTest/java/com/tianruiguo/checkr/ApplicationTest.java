package com.tianruiguo.checkr;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;

import com.tianruiguo.checkr.helpers.datastuff.GradebookDbHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public static final String LOG_TAG = ApplicationTest.class.getSimpleName();

    public ApplicationTest() {
        super(Application.class);
    }

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(GradebookDbHelper.DB_NAME);
        SQLiteDatabase db = new GradebookDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
}