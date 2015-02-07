package com.tianruiguo.checkr.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianruiguo.checkr.R;

import java.util.ArrayList;

/**
 * Created by tianrui on 2/4/15.
 */
public class GradebookAdapter extends ArrayAdapter<Gradebook> {
    public GradebookAdapter(Context context, ArrayList<Gradebook> gradebooks) {
        super(context, 0, gradebooks);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gradebook gradebook = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_gradebook, parent, false);
        }

        TextView className = (TextView) convertView.findViewById(R.id.text_view_class_name);
        TextView percentageGrade = (TextView) convertView.findViewById(R.id.text_view_class_percentage_grade);
        TextView mark = (TextView) convertView.findViewById(R.id.text_view_class_mark);
        TextView lastUpdate = (TextView) convertView.findViewById(R.id.text_view_last_updated);

        ImageView trend = (ImageView) convertView.findViewById(R.id.image_view_trend);
        String trendDir = gradebook.getTrendDirection();

        switch (trendDir) {
            case "UP":
                trend.setImageResource(R.drawable.ic_action_trending_up);
                break;
            case "DOWN":
                trend.setImageResource(R.drawable.ic_action_trending_down);
                break;
            default:
                trend.setImageResource(R.drawable.ic_action_trending_neutral);
        }
        className.setText(gradebook.getPeriod() + ". " + gradebook.getClassName());
        percentageGrade.setText(gradebook.getPercentGrade() + "%");
        mark.setText(gradebook.getMark());
        lastUpdate.setText(gradebook.printSimpleDate());
        return convertView;
    }
}
