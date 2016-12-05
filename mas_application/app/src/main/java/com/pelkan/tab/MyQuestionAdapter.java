package com.pelkan.tab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyQuestionAdapter extends ArrayAdapter<QuestionList> {       //메인화면에서 문제리스트 어댑터
    int groupid;
    ArrayList<QuestionList> records;
    Context context;

    public MyQuestionAdapter(Context context, int vg, ArrayList<QuestionList> records) {
        super(context, vg,  records);
        this.context = context;
        this.groupid = vg;
        this.records = records;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ListHolder holder = null;

        if(convertView==null){
            holder = new ListHolder();
            LayoutInflater inflater =( (Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.my_question, null);
            holder.addDate = (TextView) convertView.findViewById(R.id.reg_date);
            holder.title = (TextView) convertView.findViewById(R.id.question_title);
            holder.responseCount = (TextView) convertView.findViewById(R.id.response_count);
            convertView.setTag(holder);

        }else{
            holder = (ListHolder) convertView.getTag();
        }
        QuestionList food = records.get(position);
        holder.addDate.setText("  " + food.getAddDate());
        holder.title.setText(food.getTitle());
        holder.responseCount.setText("풀이 " + food.getResponseCount());

        return convertView;
    }
}
