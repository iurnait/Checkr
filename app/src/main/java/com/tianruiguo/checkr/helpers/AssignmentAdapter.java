package com.tianruiguo.checkr.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tianruiguo.checkr.R;
import com.tianruiguo.checkr.helpers.objects.Assignment;

import java.util.ArrayList;

/**
 * Created by tianrui on 2/4/15.
 */
public class AssignmentAdapter extends ArrayAdapter<Assignment> {
    public AssignmentAdapter(Context context, ArrayList<Assignment> assignment) {
        super(context, 0, assignment);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Assignment assignment = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_assignment, parent, false);
        }

        TextView assignmentName = (TextView) convertView.findViewById(R.id.text_view_assignment_name);
        TextView category = (TextView) convertView.findViewById(R.id.text_view_assignment_category);
        TextView percentGrade = (TextView) convertView.findViewById(R.id.text_view_assignment_percent_grade);
        TextView score = (TextView) convertView.findViewById(R.id.text_view_assignment_score);

        assignmentName.setText(assignment.getAssignmentNumber() + ". " + assignment.getDescription());
        category.setText(assignment.getType());
        percentGrade.setText(assignment.getPercent() + "%");
        score.setText(assignment.getNumberCorrect() + "/" + assignment.getNumberPossible());

        return convertView;
    }
}
