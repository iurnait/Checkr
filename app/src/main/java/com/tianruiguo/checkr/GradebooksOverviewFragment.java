package com.tianruiguo.checkr;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tianruiguo.checkr.helpers.Gradebook;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class GradebooksOverviewFragment extends Fragment {

    public static final String GRADEBOOK_NUM = "gradebook_num";
    
    private ArrayAdapter<String> mGradebooksAdapter;
    private ArrayList<Gradebook> mGradebooks;

    public GradebooksOverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gradebooks_overview, container, false);

        mGradebooksAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_class,
                        R.id.list_item_class_textview,
                        new ArrayList<String>()
                );

        ListView listViewGradebooks = (ListView) rootView.findViewById(R.id.listview_gradebooks);
        listViewGradebooks.setAdapter(mGradebooksAdapter);

        FetchGradebooksTask gradebooksFetchr = new FetchGradebooksTask();
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

    public class FetchGradebooksTask extends AsyncTask<Void, Void, ArrayList<Gradebook>> {

        private static final String LOG_TAG = "GRADES";
        private final String API_URL = "https://aeries-grade-check.herokuapp.com/testing.php";
        private final String EMAIL = "user_email";
        private final String PASSWORD = "user_password";
        private final String TYPE = "type";

        @Override
        protected ArrayList<Gradebook> doInBackground(Void... params) {

            String gradesJson;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String type = "summary";
            String email = Auth.EMAIL;
            String password = Auth.PASSWORD;

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

                mGradebooks = JsonParser.parseSummary(gradesJson);
                return mGradebooks;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
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
        protected void onPostExecute(ArrayList<Gradebook> gradebooks) {
            if (gradebooks != null) {
                mGradebooksAdapter.clear();
                for (Gradebook g : gradebooks) {
                    mGradebooksAdapter.add(g.printSimple());
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