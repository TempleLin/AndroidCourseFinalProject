package com.templo.androidcoursefinalproject.custom_recyclerlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.templo.androidcoursefinalproject.R;

import java.util.List;

public class CustomRecyclerListAdapter extends ArrayAdapter<CustomRow> {

    public CustomRecyclerListAdapter(Context context, int resource, List<CustomRow> shapeList) {
        super(context, resource, shapeList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomRow customRow = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_img_txt_row, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.custom_list_row_textview);
        ImageView iv = (ImageView) convertView.findViewById(R.id.custom_list_row_imageview);

        tv.setText(customRow.getTextValue());
        iv.setImageResource(customRow.getImage());


        return convertView;
    }
}