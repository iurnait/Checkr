package com.tianruiguo.checkr.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
 * Created by tianrui on 2/4/15.
 */
public class FetchStuffTask extends AsyncTask<Void, Void, String> {

    protected static final String LOG_TAG = "ASYNC_TASK";
    
    // Strings for the api
    private static final String API_URL = "https://aeries-grade-check.herokuapp.com/testing.php";
    private static final String EMAIL = "user_email";
    private static final String PASSWORD = "user_password";

    private String email, password;
    private List<NameValuePair> post;
    private Activity callingActivity;

    public FetchStuffTask(Activity callingActivity) {
        this.callingActivity = callingActivity;
        post = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences sharedPreferences = callingActivity.getSharedPreferences("com.tianruiguo.checkr.AUTH", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL", "none");
        password = sharedPreferences.getString("PASSWORD", "none");

        addPost(new BasicNameValuePair(EMAIL, email));
        addPost(new BasicNameValuePair(PASSWORD, password));
    }

    protected void addPost(BasicNameValuePair pair) {
        post.add(pair);
    }
    
    @Override
    protected String doInBackground(Void... params) {

        String json = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

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
            json = buffer.toString();
            
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
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
        
        return json;
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
