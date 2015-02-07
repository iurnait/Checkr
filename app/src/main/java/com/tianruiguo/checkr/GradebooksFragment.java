package com.tianruiguo.checkr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tianruiguo.checkr.helpers.FetchStuffTask;
import com.tianruiguo.checkr.helpers.GradebookAdapter;
import com.tianruiguo.checkr.helpers.JsonParser;
import com.tianruiguo.checkr.helpers.database.GradebookDataSource;
import com.tianruiguo.checkr.helpers.objects.Gradebook;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class GradebooksFragment extends Fragment {

    public static final String GRADEBOOK_NUM = "gradebook_num";

    private GradebookDataSource mGradebookDataSource;
    private GradebookAdapter mGradebooksAdapter;
    private ArrayList<Gradebook> mGradebooks;

    public GradebooksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gradebooks_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateDb();
            return true;
        } else if (id == R.id.action_login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gradebooks, container, false);

        mGradebookDataSource = new GradebookDataSource(getActivity());
        mGradebookDataSource.open();

        mGradebooksAdapter = new GradebookAdapter(getActivity(), new ArrayList<Gradebook>());

        ListView listViewGradebooks = (ListView) rootView.findViewById(R.id.listview_gradebooks);
        listViewGradebooks.setAdapter(mGradebooksAdapter);

        updateView();
        
        listViewGradebooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int gradebookNumber = mGradebooks.get(position).getGradebookNumber();
                Intent intent = new Intent(getActivity(), GradebookDetailActivity.class);
                intent.putExtra(GRADEBOOK_NUM, gradebookNumber);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateDb() {
        FetchGradebooksTask gradebooksFetchr = new FetchGradebooksTask(getActivity());
        gradebooksFetchr.execute();
    }

    private void updateView() {
        mGradebooksAdapter.clear();
        mGradebooks = mGradebookDataSource.getGradebooks();
        for (Gradebook g : mGradebooks) {
            mGradebooksAdapter.add(g);
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

    public class FetchGradebooksTask extends FetchStuffTask {

        public FetchGradebooksTask(Activity callingActivity) {
            super(callingActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            addPost(new BasicNameValuePair("type", "summary"));
        }

        @Override
        protected void onPostExecute(String json) {
            if (json != null) {

                try {
                    mGradebooks = JsonParser.parseSummary(json);
                    mGradebookDataSource.addGradebooks(mGradebooks);
                    updateView();
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                    Log.d(LOG_TAG, "JSON: " + json);
                }
            }

        }
    }

}