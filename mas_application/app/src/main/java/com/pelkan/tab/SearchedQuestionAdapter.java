package com.pelkan.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SearchedQuestionAdapter extends ArrayAdapter<QuestionList> {       //메인화면에서 문제리스트 어댑터
    int groupid;
    ArrayList<QuestionList> records;
    Context context;

    public SearchedQuestionAdapter(Context context, int vg, ArrayList<QuestionList> records) {
        super(context, vg,  records);
        this.context = context;
        this.groupid = vg;
        this.records = records;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ListHolderMain holder = null;

        if(convertView==null){
            holder = new ListHolderMain();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.main_question, null);
            holder.keyword = (TextView) convertView.findViewById(R.id.keywords);           //qid 임ㅇㅇ
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.view_count = (TextView) convertView.findViewById(R.id.view_count);
            holder.response_correct = (TextView) convertView.findViewById(R.id.response_correct);
            holder.addDate = (TextView) convertView.findViewById(R.id.reg_date);
            convertView.setTag(holder);

        }else{
            holder = (ListHolderMain) convertView.getTag();
        }
        QuestionList food = records.get(position);
        holder.keyword.setText(food.getKeywords());
        holder.title.setText(food.getTitle());
        holder.view_count.setText("조회수 " + food.getViewCount());
        holder.response_correct.setText("풀이 / 정답\n" + food.getResponseCount() + " / " + food.getSuccess_count());
        holder.addDate.setText("등록일 " + food.getAddDate());

        return convertView;
    }
}
