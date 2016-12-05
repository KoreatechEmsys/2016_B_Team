package com.pelkan.tab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
/**
 * 선택한 시험의 문제를 뿌려주는 fragment
 * Created by JangLab on 2016-09-17.
 */
public class TestSolveFragment1 extends Fragment {
    private String json;
    private String eid;
    private TextView exam_number;
    private ImageView q_image;
    private FancyButton before_exam;
    private FancyButton response_add;
    private FancyButton next_exam;
    private AQuery aq;
    private int temp_start = 0;
    public static int currentQIndex;
    public static ArrayList<String> q_id_array = new ArrayList<String>();
//    public static ArrayList<Integer> is_solve_array = new ArrayList<Integer>();
    public static ArrayList<ExamQuestion> question_array = new ArrayList<ExamQuestion>();
    public static ArrayList<ExamResponse> response_array = new ArrayList<ExamResponse>();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String url_qustion = "http://218.150.182.58:2041/mas/selected_exam.php";
    private String url_add = "http://218.150.182.58:2041/mas/add_exam_response.php";

    public TestSolveFragment1() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SolveExamActivity.PlaceholderFragment newInstance(int sectionNumber) {
        SolveExamActivity.PlaceholderFragment fragment = new SolveExamActivity.PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_solve_exam2, container, false);
        currentQIndex = 0;
        if (getArguments() != null) {                       //클릭한 pid 저장
            eid = getArguments().getString("eid");
        }

        exam_number = (TextView) rootView.findViewById(R.id.exam_number);
        q_image = (ImageView) rootView.findViewById(R.id.q_image);
        before_exam = (FancyButton) rootView.findViewById(R.id.before_exam);
        response_add = (FancyButton) rootView.findViewById(R.id.response_add);
        next_exam = (FancyButton) rootView.findViewById(R.id.next_exam);
        aq = new AQuery(getActivity(), rootView);
        before_exam.setVisibility(View.INVISIBLE);

        new MyExamListTask().execute(url_qustion);

        before_exam.setOnClickListener(new View.OnClickListener() {     //다음 시험문제

            @Override
            public void onClick(View view) {
                if(currentQIndex == 1)
                    before_exam.setVisibility(View.INVISIBLE);
                next_exam.setText("다음");
                currentQIndex--;
                exam_number.setText("문제 " + (currentQIndex + 1) + ". " + question_array.get(currentQIndex).getTitle());                  //제일 먼저 꺼내온 문제가 1번문제
                aq.id(q_image).image(question_array.get(currentQIndex).getImg_url());
            }
        });

        next_exam.setOnClickListener(new View.OnClickListener() {       //이전 시험문제

            @Override
            public void onClick(View view) {
                before_exam.setVisibility(View.VISIBLE);
                int flag = 1;
                if(next_exam.getText().toString().equals("제출")) {         //이거가 실행될떄 시험 종료
                    String questionNum = "";
                    for(int i = 0; i < question_array.size(); i++) {
                        if(SolveExamActivity.is_solve_array.get(i) == 0) {
                            flag = 0;
                            questionNum += String.valueOf(i+1) + ",";
                        }
                    }

                    if(flag == 1) {
                        new AddExamResponse().execute(url_add);
                        getActivity().finish();
                    } else {
                        String temp;
                        temp = questionNum.substring(0, questionNum.length() - 1);
                        Toast.makeText(getActivity().getApplicationContext(),
                                temp + "번 문제에 대한 풀이를 등록해주세요", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    if(question_array.size() - 2 == currentQIndex) {        //끝에 가기 직전이면
                        next_exam.setText("제출");
                    }
                    currentQIndex++;
                    exam_number.setText("문제 " + (currentQIndex + 1) + ". " + question_array.get(currentQIndex).getTitle());                  //제일 먼저 꺼내온 문제가 1번문제
                    aq.id(q_image).image(question_array.get(currentQIndex).getImg_url());
                }
            }
        });


        response_add.setOnClickListener(new View.OnClickListener() {    //문제에 대한 답안 제출

            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                TestSolveFragment2 temp = new TestSolveFragment2();
                trans.replace(R.id.root_frame, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });
        return rootView;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    private class AddExamResponse  extends AsyncTask<String, Void, Void> {


        protected void onPreExecute() {

        }

        // onPreExecute 메소드 이후 호출
        protected Void doInBackground(String... urls) {//클릭이벤트 실행시 파라미터값 urls= LongOperation().execute(serverURL);

            System.out.println("연결 준비");
            BufferedReader reader = null;

            // 데이터 전송
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setChunkedStreamingMode(0);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("size", String.valueOf(response_array.size())));           //배열의 길이를 먼저 보냄
                for(int i = 0; i < response_array.size(); i++) {                        //내용, eid, imgurl, m_id, ori_qid, q_id 순
                    param.add(new BasicNameValuePair("arrayNum" + i, response_array.get(i).getContent() + "," + eid + ","
                            + response_array.get(i).getImg_url() + "," + response_array.get(i).getM_id() + "," + response_array.get(i).getOri_qid() + ","
                            + response_array.get(i).getQid() + ","));
                }
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                System.out.println("연결 준비끝");
                wr.write(getQuery(param));//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                wr.flush();//OutputStreamWriter 버퍼 메모리 비우기

                //PHP 서버 응답값을 변수에 저장
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                //서버 응답변수 reader 에서 내용을 라인단위 문자열로 만듬
                while ((line = reader.readLine()) != null) {
                    //서버응답값을 String 형태로 추가함
                    sb.append(line + "\n");
                }
                json = sb.toString();
            } catch (Exception ex) {
            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }

            /*****************************************************/
            return null;
        }

        //doInBackground POST전송 후 자동실행 코드
        protected void onPostExecute(Void unused) {
            response_array.clear();
            System.out.println("튜플 받아옴");
            System.out.println("데이터는 " + json);
//            question_array.clear();
//
//            try{
//                JSONObject jsonResponse = new JSONObject(json);
//                JSONArray jsonMainNode = jsonResponse.optJSONArray("question_list");
//                for (int i = 0; i < jsonMainNode.length(); i++) {
//                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
//                    String q_id = jsonChildNode.optString("q_id");
//                    String title = jsonChildNode.optString("title");
//                    String img_url = jsonChildNode.optString("q_img_url");
//
//                    System.out.println("온 문제는");
//                    System.out.println(q_id + " " + title+ " " + img_url);
//                    ExamQuestion temp = new ExamQuestion();
//                    temp.setImg_url(img_url);
//                    temp.setQid(q_id);
//                    temp.setTitle(title);
//                    q_id_array.add(q_id);
//
//                    question_array.add(temp);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            exam_number.setText("문제 1. " + question_array.get(0).getTitle());                  //제일 먼저 꺼내온 문제가 1번문제
//            aq.id(q_image).image(question_array.get(0).getImg_url());
//            if(temp_start++ == 0) {
//                System.out.println("실행1");
//                for(int i = 0; i < question_array.size(); i++) {
//                    ExamResponse temp = new ExamResponse();
//                    response_array.add(temp);
//                    System.out.println("길이는 " + response_array.size());
//                }
//            }
        }
    }

    private class MyExamListTask  extends AsyncTask<String, Void, Void> {


        protected void onPreExecute() {

        }

        // onPreExecute 메소드 이후 호출
        protected Void doInBackground(String... urls) {//클릭이벤트 실행시 파라미터값 urls= LongOperation().execute(serverURL);

            System.out.println("연결 준비");
            BufferedReader reader = null;

            // 데이터 전송
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setChunkedStreamingMode(0);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("eid", eid));
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                System.out.println("연결 준비끝");
                wr.write(getQuery(param));//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                wr.flush();//OutputStreamWriter 버퍼 메모리 비우기

                //PHP 서버 응답값을 변수에 저장
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                //서버 응답변수 reader 에서 내용을 라인단위 문자열로 만듬
                while ((line = reader.readLine()) != null) {
                    //서버응답값을 String 형태로 추가함
                    sb.append(line + "\n");
                }
                json = sb.toString();
            } catch (Exception ex) {
            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }

            /*****************************************************/
            return null;
        }

        //doInBackground POST전송 후 자동실행 코드
        protected void onPostExecute(Void unused) {
            System.out.println("튜플 받아옴");
            System.out.println("데이터는 " + json);
            question_array.clear();

            try{
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("question_list");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String q_id = jsonChildNode.optString("q_id");
                    String title = jsonChildNode.optString("title");
                    String img_url = jsonChildNode.optString("q_img_url");

                    System.out.println("온 문제는");
                    System.out.println(q_id + " " + title+ " " + img_url);
                    ExamQuestion temp = new ExamQuestion();
                    temp.setImg_url(img_url);
                    temp.setQid(q_id);
                    temp.setTitle(title);
                    q_id_array.add(q_id);

                    question_array.add(temp);
                    SolveExamActivity.is_solve_array.add(0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            exam_number.setText("문제 1. " + question_array.get(0).getTitle());                  //제일 먼저 꺼내온 문제가 1번문제
            aq.id(q_image).image(question_array.get(0).getImg_url());
            if(temp_start++ == 0) {
                System.out.println("실행1");
                for(int i = 0; i < question_array.size(); i++) {
                    ExamResponse temp = new ExamResponse();
                    response_array.add(temp);
                    System.out.println("길이는 " + response_array.size());
                }
            }

            if(response_array.size() == 1) {
                next_exam.setText("제출");
            }
        }
    }
}
