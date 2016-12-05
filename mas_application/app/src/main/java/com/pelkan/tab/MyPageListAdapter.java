package com.pelkan.tab;

/**
 * Created by admin on 2016-02-04.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;


public class MyPageListAdapter extends ArrayAdapter<String> {

    int groupid;

    ArrayList<String> records;

    Context context;
    int loader = R.drawable.ic_la;


    public MyPageListAdapter(Context context, int vg, ArrayList<String> records) {

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
            convertView = inflater.inflate(R.layout.my_page_list, null);

            holder.mypqge_list_name = (TextView) convertView.findViewById(R.id.menu_name);
            holder.detailBtn = (FancyButton) convertView.findViewById(R.id.detailBtn);
            convertView.setTag(holder);

        }else{
            holder = (ListHolder) convertView.getTag();
            //holder.img.setImageResource(R.drawable.def); 임시이미지 def라는 파일로 하기
        }
        String food = records.get(position);
        holder.mypqge_list_name.setText(food);

        return convertView;

    }

}