package com.pelkan.tab;

/**
 * Created by JangLab on 2016-09-17.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class SelectMyQuestionAdapter extends ArrayAdapter<QuestionList> {       //메인화면에서 문제리스트 어댑터
    int groupid;
    ArrayList<QuestionList> records;
    Context context;

    public SelectMyQuestionAdapter(Context context, int vg, ArrayList<QuestionList> records) {
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
        ListHolderMain holder = null;

        if(convertView==null){
            holder = new ListHolderMain();
            LayoutInflater inflater =( (Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.select_question, null);
            holder.keyword = (TextView) convertView.findViewById(R.id.keywords);           //qid 임ㅇㅇ
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.view_count = (TextView) convertView.findViewById(R.id.view_count);
            holder.response_correct = (TextView) convertView.findViewById(R.id.response_correct);
            holder.addDate = (TextView) convertView.findViewById(R.id.reg_date);
            holder.ckbox = (CheckBox) convertView.findViewById(R.id.ckbox);
            convertView.setTag(holder);

        }else{
            holder = (ListHolderMain) convertView.getTag();
        }

        holder.ckbox.setChecked(false);

        holder.ckbox.setChecked(((ListView)parent).isItemChecked(position));




        QuestionList food = records.get(position);
        holder.keyword.setText(food.getKeywords());
        holder.title.setText(food.getTitle());
        holder.view_count.setText("조회수 " + food.getViewCount());
        holder.response_correct.setText("풀이 / 정답\n" + food.getResponseCount() + " / " + food.getSuccess_count());
        holder.addDate.setText("등록일 " + food.getAddDate());

        //체크박스 클릭 이벤트
        holder.ckbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.d("AutoCallService", "checkClick");
                QuestionList checkViewItem = (QuestionList)getItem(checkBoxPosition);
                Log.d("AutoCallService", "checkItem : " + checkViewItem.getTitle());

                //체크 안되어 있는 경우
                if(checkViewItem.isChecked()){
                    records.get(checkBoxPosition).setChecked(false);
                    //체크가 되어 있는 경우
                }else{
                    System.out.println("하이하이");
                    records.get(checkBoxPosition).setChecked(true);
                }
                //데이터 변경 알림
                notifyDataSetChanged();
            }


        });

        //로우별 isChecked 값에 따른 체크상태를 표시
        holder.ckbox.setChecked(records.get(position).isChecked());

        TestFragment.is_checked_list.clear();
        TestFragment.qidArr.clear();
        for(int j = 0; j < records.size(); j++) {
            TestFragment.is_checked_list.add(j, records.get(j).isChecked());
            TestFragment.qidArr.add(j, records.get(j).getQid());
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public QuestionList getItem(int position) {
        return records.get(position);
    }
}
