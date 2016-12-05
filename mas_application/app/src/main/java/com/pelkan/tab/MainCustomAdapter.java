package com.pelkan.tab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MainCustomAdapter extends ArrayAdapter<MainProduct> {          //오늘의 문제 어댑터
    int groupid;
    ArrayList<MainProduct> records;
    Context context;

    public MainCustomAdapter(Context context, int vg, ArrayList<MainProduct> records) {
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
            convertView = inflater.inflate(R.layout.main_list, null);

            holder.title = (TextView) convertView.findViewById(R.id.title);           //qid 임ㅇㅇ
            holder.keyword = (TextView) convertView.findViewById(R.id.keywords);           //qid 임ㅇㅇ
            holder.viewCount = (TextView) convertView.findViewById(R.id.view_count);           //qid 임ㅇㅇ
            holder.responseCorrect= (TextView) convertView.findViewById(R.id.response_correct);        //홀더 설정
            holder.addDate = (TextView) convertView.findViewById(R.id.reg_date);
            convertView.setTag(holder);

        }else{
            holder = (ListHolder) convertView.getTag();
        }
        MainProduct food = records.get(position);
        holder.keyword.setText(food.getKeywords());
        holder.title.setText(food.getTitle());
        holder.viewCount.setText("조회수 " + food.getViewCount());
        holder.responseCorrect.setText("풀이 / 정답\n" + food.getResponseCount() + " / " + food.getSuccessCount());
        holder.addDate.setText("등록일 " + food.getAddDate());


        return convertView;

    }

}
