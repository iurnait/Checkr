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
import com.tianruiguo.checkr.helpers.objects.Gradebook;
import com.tianruiguo.checkr.helpers.GradebookAdapter;
import com.tianruiguo.checkr.helpers.JsonParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class GradebooksOverviewFragment extends Fragment {

    public static final String GRADEBOOK_NUM = "gradebook_num";

    private GradebookAdapter mGradebooksAdapter;
    private ArrayList<Gradebook> mGradebooks;

    public GradebooksOverviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gradebook_overview_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            FetchGradebooksTask fetchGradebooksTask = new FetchGradebooksTask(getActivity());
            fetchGradebooksTask.execute();
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
        View rootView = inflater.inflate(R.layout.fragment_gradebooks_overview, container, false);

        mGradebooksAdapter = new GradebookAdapter(getActivity(), new ArrayList<Gradebook>());

        ListView listViewGradebooks = (ListView) rootView.findViewById(R.id.listview_gradebooks);
        listViewGradebooks.setAdapter(mGradebooksAdapter);

        FetchGradebooksTask gradebooksFetchr = new FetchGradebooksTask(getActivity());
        gradebooksFetchr.execute();

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
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                    Log.d(LOG_TAG, "JSON: " + json);
                }

                mGradebooksAdapter.clear();
                for (Gradebook g : mGradebooks) {
                    mGradebooksAdapter.add(g);
                }
            }

        }
    }

}