package com.tianruiguo.checkr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ClassesFragment extends Fragment {

    public ClassesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayAdapter<String> classesAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_class,
                        R.id.list_item_class_textview,
                        new String[]{
                                "Math - 95%",
                                "Science - 93%",
                                "History - 90%"}
                );

        ListView listViewClasses = (ListView) rootView.findViewById(R.id.listview_classes);
        listViewClasses.setAdapter(classesAdapter);

        FetchGradesTask gradeFetchr = new FetchGradesTask();
        gradeFetchr.execute();

        return rootView;
    }
}