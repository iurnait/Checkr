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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tianruiguo.checkr.helpers.Assignment;
import com.tianruiguo.checkr.helpers.FetchStuffTask;
import com.tianruiguo.checkr.helpers.JsonParser;

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
                    .add(R.id.container, new GradebookDetailFragment())
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


    public static class GradebookDetailFragment extends Fragment {

        private static final String LOG_TAG = "GRADE_DETAILS";
        private ArrayList<Assignment> mAssignments;
        private ArrayAdapter<String> mAssignmentsAdapter;

        public GradebookDetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gradebook_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(GradebooksOverviewFragment.GRADEBOOK_NUM)) {
                int gradebookId = intent.getIntExtra(GradebooksOverviewFragment.GRADEBOOK_NUM, 0);

                mAssignmentsAdapter =
                        new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.list_item_class,
                                R.id.list_item_class_textview,
                                new ArrayList<String>()
                        );

                ListView listViewGradebooks = (ListView) rootView.findViewById(R.id.listview_assignments);
                listViewGradebooks.setAdapter(mAssignmentsAdapter);

                FetchGradebookDetailsTask gradebooksFetchr = new FetchGradebookDetailsTask(getActivity(), gradebookId);
                gradebooksFetchr.execute();
            }
            return rootView;
        }


        public class FetchGradebookDetailsTask extends FetchStuffTask {

            private int gradebookId;

            public FetchGradebookDetailsTask(Activity callingActivity, int gradebookId) {
                super(callingActivity);
                this.gradebookId = gradebookId;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                addPost(new BasicNameValuePair("type", "class_detail"));
                addPost(new BasicNameValuePair("gradebook_number", "" + gradebookId));
            }

            @Override
            protected void onPostExecute(String json) {
                if (json != null) {
                    try {
                        mAssignments = JsonParser.parseDetail(json);
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, e.getLocalizedMessage());
                        Log.d(LOG_TAG, "JSON: " + json);
                    }

                    mAssignmentsAdapter.clear();
                    for (Assignment a : mAssignments) {
                        mAssignmentsAdapter.add(a.printSimple());
                    }
                }

            }

        }
    }
}
