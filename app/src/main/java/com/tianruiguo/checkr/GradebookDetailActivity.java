package com.tianruiguo.checkr;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.tianruiguo.checkr.helpers.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class GradebookDetailFragment extends Fragment {

        private static final String LOG_TAG = "GRADE_DETAILS";
        private ArrayList<Assignment> mGradebookDetails;
        private ArrayAdapter<String> mGradebookDetailsAdapter;

        public GradebookDetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gradebook_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(GradebooksOverviewFragment.GRADEBOOK_NUM)) {
                int gradebookId = intent.getIntExtra(GradebooksOverviewFragment.GRADEBOOK_NUM, 0);

                mGradebookDetailsAdapter =
                        new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.list_item_class,
                                R.id.list_item_class_textview,
                                new ArrayList<String>()
                        );

                ListView listViewGradebooks = (ListView) rootView.findViewById(R.id.listview_assignments);
                listViewGradebooks.setAdapter(mGradebookDetailsAdapter);

                FetchGradebookDetailsTask gradebooksFetchr = new FetchGradebookDetailsTask();
                gradebooksFetchr.execute("" + gradebookId);
            }
            return rootView;
        }


        // TODO: This is very similar to the AsyncTask in MainActivity
        // Maybe we can refactor and combine
        public class FetchGradebookDetailsTask extends AsyncTask<String, Void, ArrayList<Assignment>> {

            private static final String LOG_TAG = "GRADE_DETAILS";
            private final String API_URL = "https://aeries-grade-check.herokuapp.com/testing.php";
            private final String EMAIL = "user_email";
            private final String PASSWORD = "user_password";
            private final String TYPE = "type";
            private final String GRADEBOOK = "gradebook_number";

            @Override
            protected ArrayList<Assignment> doInBackground(String... params) {

                String gradesJson;

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                String type = "class_detail";
                String email = Auth.EMAIL;
                String password = Auth.PASSWORD;
                String gradebook = params[0];

                try {
                    URL url = new URL(API_URL);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000);
                    // Heroku takes a to start up when dyno goes to sleep
                    urlConnection.setConnectTimeout(20000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                    urlConnection.setRequestProperty("Accept", "*/*");

                    List<NameValuePair> post = new ArrayList<NameValuePair>();
                    post.add(new BasicNameValuePair(TYPE, type));
                    post.add(new BasicNameValuePair(EMAIL, email));
                    post.add(new BasicNameValuePair(PASSWORD, password));
                    post.add(new BasicNameValuePair(GRADEBOOK, gradebook));

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(post));
                    writer.flush();
                    writer.close();
                    os.close();

                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    gradesJson = buffer.toString();

                    mGradebookDetails = JsonParser.parseDetail(gradesJson);
                    return mGradebookDetails;
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the gradebook data, there's no point in attemping
                    // to parse it.
                    return null;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error ", e);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }
                return null;

            }

            @Override
            protected void onPostExecute(ArrayList<Assignment> assignments) {
                if (assignments != null) {
                    mGradebookDetailsAdapter.clear();
                    for (Assignment a : assignments) {
                        mGradebookDetailsAdapter.add(a.printSimple());
                    }
                }

            }

            // http://stackoverflow.com/a/13486223
            private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
                StringBuilder result = new StringBuilder();
                boolean first = true;

                for (NameValuePair pair : params) {
                    if (first)
                        first = false;
                    else
                        result.append("&");

                    result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
                }

                return result.toString();
            }
        }
    }
}
