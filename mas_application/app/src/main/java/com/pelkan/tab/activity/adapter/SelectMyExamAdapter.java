package com.pelkan.tab;

/**
 * Created by JangLab on 2016-09-17.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SelectMyExamAdapter extends ArrayAdapter<ExamList> {       //메인화면에서 문제리스트 어댑터
    int groupid;
    ArrayList<ExamList> records;
    Context context;

    public SelectMyExamAdapter(Context context, int vg, ArrayList<ExamList> records) {
        super(context, vg,  records);
        this.context = context;
        this.groupid = vg;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size() ;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int checkBoxPosition = position;
        View v = null;
        ListHolderExam holder = null;

        if(convertView==null){
            holder = new ListHolderExam();
            LayoutInflater inflater =( (Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.select_exam, null);
            holder.start_time = (TextView) convertView.findViewById(R.id.start_time);
            holder.end_time = (TextView) convertView.findViewById(R.id.limit_time);
            holder.question_count = (TextView) convertView.findViewById(R.id.question_count);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.m_id = (TextView) convertView.findViewById(R.id.m_id);
            convertView.setTag(holder);

        }else{
            holder = (ListHolderExam) convertView.getTag();
        }

        ExamList food = records.get(position);
        holder.m_id.setText(food.getM_id());
        holder.title.setText(food.geteTitle());
        holder.start_time.setText("시작시간 : " + food.getStartTime());
        holder.end_time.setText(" 종료시간 : " + food.getEndTime());
        holder.question_count.setText("총 문항 : " + food.getQuestion_count() + " 문제");


        return convertView;
    }
}
