package com.pelkan.tab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class BestCustomAdapter extends ArrayAdapter<BestQuestion> {

    int groupid;

    ArrayList<BestQuestion> records;

    Context context;
    int loader = R.drawable.ic_la;


    public BestCustomAdapter(Context context, int vg, ArrayList<BestQuestion> records) {

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
            convertView = inflater.inflate(R.layout.best_question, null);

            holder.q_id = (TextView) convertView.findViewById(R.id.q_id);
            holder.title = (TextView) convertView.findViewById(R.id.best_q_title);
            holder.keyword = (TextView) convertView.findViewById(R.id.best_q_keyword);
            holder.level = (TextView) convertView.findViewById(R.id.best_q_level);

            convertView.setTag(holder);

        }else{
            holder = (ListHolder) convertView.getTag();
            //holder.img.setImageResource(R.drawable.def); 임시이미지 def라는 파일로 하기
        }

        BestQuestion food = records.get(position);
        holder.q_id.setText(food.getQ_id());
        holder.title.setText(food.getTitle());
        holder.keyword.setText(food.getKeyword());
        holder.level.setText(food.getLevel());

        return convertView;

    }

}