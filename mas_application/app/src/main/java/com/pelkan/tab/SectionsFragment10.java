package com.pelkan.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by JangLab on 2016-09-17.
 * 시험 출제에 대한 것
 */
public class SectionsFragment10 extends Fragment {
    private LinearLayout add_exam;
    private LinearLayout solve_exam;
    private LinearLayout correct_check;
    private LinearLayout score;

    public SectionsFragment10() {

    }
    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment10 newInstance(int SectionNumber){
        SectionsFragment10 fragment = new SectionsFragment10();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page10,
                container, false);
        add_exam = (LinearLayout) rootView.findViewById(R.id.add_exam);
        solve_exam = (LinearLayout) rootView.findViewById(R.id.solve_exam);
        correct_check = (LinearLayout) rootView.findViewById(R.id.correct_check);
        score = (LinearLayout) rootView.findViewById(R.id.score);

        //시험 출제
        add_exam.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), AddExamActivity.class);
                startActivity(intent1);
            }
        });
        //문제 풀기
        solve_exam.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), SolveExamActivity.class);
                startActivity(intent1);
            }
        });

        correct_check.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), CheckExamActivity.class);
                startActivity(intent1);
            }
        });

        score.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), CheckMyScoreActivity.class);
                startActivity(intent1);
            }
        });



        return rootView;
    }
}
