package com.example.scheduleservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ScheduleDataAdapter extends ArrayAdapter<ScheduleItemData> {
    Context mContext;
    ArrayList<ScheduleItemData> scheduleItemDataList = new ArrayList<ScheduleItemData>();



    public ScheduleDataAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ScheduleItemData> objects) {
        super(context, resource, objects);
        scheduleItemDataList = objects;
    }


    @Override
    public int getCount() {
        return scheduleItemDataList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.schedule_item,null);
        TextView scheduleCategory = (TextView)view.findViewById(R.id.schedule_item_category);
        TextView scheduleFileName = (TextView)view.findViewById(R.id.schedule_item_fileName);
        scheduleCategory.setText(scheduleItemDataList.get(position).categoryName);
        scheduleFileName.setText(scheduleItemDataList.get(position).getFileName());
        return view;
        //return super.getView(position, convertView, parent);
    }

    @Nullable
    @Override
    public ScheduleItemData getItem(int position) {
        return scheduleItemDataList.get(position);
    }
}
