package com.tianruiguo.checkr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    public static ArrayList<Gradebook> parseSummary(String jsonString) throws JSONException {
        ArrayList<Gradebook> gradebooks = new ArrayList<>();

        JSONArray gradebookArray = (new JSONObject(jsonString))
                .getJSONObject("d")
                .getJSONArray("results");

        for (int i = 0; i < gradebookArray.length(); i++) {
            JSONObject obj = gradebookArray.getJSONObject(i);
            gradebooks.add(new Gradebook(obj));
        }
        return gradebooks;
    }
}
