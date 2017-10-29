package com.pelkan.tab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JangLab on 2016-04-18.
 */
public class ResponseAdapter extends ArrayAdapter<Responser> {

    int groupid;

    ArrayList<Responser> records;

    Context context;
    int loader = R.drawable.ic_la;


    public ResponseAdapter(Context context, int vg, ArrayList<Responser> records) {

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
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.re_list, null);

            holder.q_id = (TextView) convertView.findViewById(R.id.r_id);
            holder.title = (TextView) convertView.findViewById(R.id.r_title);        //홀더 설

            convertView.setTag(holder);

        }else{
            holder = (ListHolder) convertView.getTag();
        }

        Responser food = records.get(position);
        holder.q_id.setText(food.getR_id());
        holder.title.setText(food.getTitle());

        return convertView;

    }

}