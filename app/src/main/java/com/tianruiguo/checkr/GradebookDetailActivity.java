package com.tianruiguo.checkr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tianruiguo.checkr.helpers.AssignmentAdapter;
import com.tianruiguo.checkr.helpers.FetchStuffTask;
import com.tianruiguo.checkr.helpers.JsonParser;
import com.tianruiguo.checkr.helpers.database.GradebookDataSource;
import com.tianruiguo.checkr.helpers.objects.Assignment;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;


public class GradebookDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradebook_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AssignmentsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gradebook_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class AssignmentsFragment extends Fragment {

        private static final String LOG_TAG = "GRADE_DETAILS";

        private GradebookDataSource mGradebookDataSource;
        private ArrayList<Assignment> mAssignments;
        private AssignmentAdapter mAssignmentsAdapter;
        private int mGradebookId;

        public AssignmentsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_assignments, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(GradebooksFragment.GRADEBOOK_NUM)) {
                mGradebookId = intent.getIntExtra(GradebooksFragment.GRADEBOOK_NUM, 0);

                mGradebookDataSource = new GradebookDataSource(getActivity());
                mGradebookDataSource.open();

                mAssignmentsAdapter = new AssignmentAdapter(getActivity(), new ArrayList<Assignment>());
                ListView listViewGradebooks = (ListView) rootView.findViewById(R.id.listview_assignments);
                listViewGradebooks.setAdapter(mAssignmentsAdapter);

                updateView();
                updateDb();
            }
            return rootView;
        }

        private void updateDb() {
            FetchAssignmentsTask gradebooksFetchr = new FetchAssignmentsTask(getActivity());
            gradebooksFetchr.execute();
        }

        private void updateView() {
            mAssignmentsAdapter.clear();
            mAssignments = mGradebookDataSource.getAssignments(mGradebookId);
            for (Assignment a : mAssignments) {
                mAssignmentsAdapter.add(a);
            }
        }

        @Override
        public void onPause() {
            mGradebookDataSource.close();
            super.onPause();
        }

        @Override
        public void onResume() {
            mGradebookDataSource.open();
            super.onResume();
        }

        public class FetchAssignmentsTask extends FetchStuffTask {

            public FetchAssignmentsTask(Activity callingActivity) {
                super(callingActivity);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                addPost(new BasicNameValuePair("type", "class_detail"));
                addPost(new BasicNameValuePair("gradebook_number", "" + mGradebookId));
            }

            @Override
            protected void onPostExecute(String json) {
                if (json != null) {
                    try {
                        mAssignments = JsonParser.parseDetail(json);
                        mGradebookDataSource.addAssignments(mAssignments);
                        updateView();
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, e.getLocalizedMessage());
                        Log.d(LOG_TAG, "JSON: " + json);
                    }

                }

            }

        }
    }
}
