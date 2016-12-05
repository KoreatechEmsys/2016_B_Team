package com.pelkan.tab;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * 선택한 시험의 문제를 뿌려주는 fragment
 * Created by JangLab on 2016-09-17.
 *
 * 정답맞으면 correct = 1, 틀리면 = 2, 미채점이면 0
 */
public class TestScoreFragment2 extends Fragment {
    private String json;
    private String eid;
    private String m_id;
    private TextView exam_number;
    private TextView r_content;
    private ImageView q_image;
    private ImageView r_image;
    private ImageView is_correct;
    private FancyButton before_exam;
    private FancyButton next_exam;
    private AQuery aq;
    private AQuery aq_r;
    LinearLayout videoView_layout;
    private int isCorrect = 0;
    private int temp_start = 0;
    public static int currentQIndex;
    JCVideoPlayerStandard jcVideoPlayerStandard;
    public static ArrayList<String> q_id_array = new ArrayList<String>();
    public ArrayList<ExamQuestion> question_array = new ArrayList<ExamQuestion>();
    public static ArrayList<ExamResponse> response_array = new ArrayList<ExamResponse>();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String url_check = "http://218.150.182.58:2041/mas/check_correct.php";
    private String url_add = "http://218.150.182.58:2041/mas/check_exam.php";

    public TestScoreFragment2() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CheckMyScoreActivity.PlaceholderFragment newInstance(int sectionNumber) {
        CheckMyScoreActivity.PlaceholderFragment fragment = new CheckMyScoreActivity.PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score_exam3, container, false);
        currentQIndex = 0;
        if (getArguments() != null) {                       //클릭한 pid 저장
            m_id = getArguments().getString("m_id");
            eid = getArguments().getString("e_id");
        }

        exam_number = (TextView) rootView.findViewById(R.id.exam_number);
        r_content = (TextView) rootView.findViewById(R.id.r_content);
        q_image = (ImageView) rootView.findViewById(R.id.q_image);
        r_image = (ImageView) rootView.findViewById(R.id.r_image);
        is_correct = (ImageView) rootView.findViewById(R.id.is_correct);
        before_exam = (FancyButton) rootView.findViewById(R.id.before_exam);
        next_exam = (FancyButton) rootView.findViewById(R.id.next_exam);
        videoView_layout = (LinearLayout) rootView.findViewById(R.id.videoView_layout);
        jcVideoPlayerStandard = (JCVideoPlayerStandard) rootView.findViewById(R.id.videoView);
        aq = new AQuery(getActivity(), rootView);
        aq_r = new AQuery(getActivity(), rootView);
        before_exam.setVisibility(View.INVISIBLE);
        new GetExamAndResponse().execute(url_add);

//        new MyExamListTask().execute(url_qustion);

        q_image.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), ImageDetailActivity.class);
                intent1.putExtra("img_url", question_array.get(currentQIndex).getImg_url());
                startActivity(intent1);
            }
        });

        r_image.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), ImageDetailActivity.class);
                intent1.putExtra("img_url", response_array.get(currentQIndex).getImg_url());
                startActivity(intent1);
            }
        });

        before_exam.setOnClickListener(new View.OnClickListener() {     //다음 시험문제

            @Override
            public void onClick(View view) {
                if(currentQIndex == 1)
                    before_exam.setVisibility(View.INVISIBLE);
                next_exam.setText("다음");
                currentQIndex--;
                exam_number.setText("문제 " + (currentQIndex + 1) + ". " + question_array.get(currentQIndex).getTitle());                  //제일 먼저 꺼내온 문제가 1번문제
                aq.id(q_image).image(question_array.get(currentQIndex).getImg_url());
                r_content.setText(response_array.get(currentQIndex).getContent());
                String tempString = response_array.get(currentQIndex).getImg_url();
                if(tempString.substring(tempString.length()-3, tempString.length()).equals("mp4")) {        //동영상일경우
                    r_image.setVisibility(View.GONE);
                    videoView_layout.setVisibility(View.VISIBLE);
                    jcVideoPlayerStandard.setUp(tempString, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "test");
                } else {                                                                                        //이미지일 경우
                    aq_r.id(r_image).image(response_array.get(currentQIndex).getImg_url());
                    videoView_layout.setVisibility(View.GONE);
                    r_image.setVisibility(View.VISIBLE);
                }
                if(response_array.get(currentQIndex).getIs_correct() == 1) {            //정답일시
                    is_correct.setColorFilter(Color.parseColor("#45ACCD"));
                } else if(response_array.get(currentQIndex).getIs_correct() == 2) {     //틀릴경우
                    is_correct.setColorFilter(Color.parseColor("#ff3232"));
                }
            }
        });

        next_exam.setOnClickListener(new View.OnClickListener() {       //이전 시험문제

            @Override
            public void onClick(View view) {
                before_exam.setVisibility(View.VISIBLE);
                if(next_exam.getText().toString().equals("완료")) {         //이거가 실행될떄 시험 종료
                    getActivity().finish();
                } else {
                    if(question_array.size() - 2 == currentQIndex) {        //끝에 가기 직전이면
                        next_exam.setText("완료");
                    }
                    currentQIndex++;
                    exam_number.setText("문제 " + (currentQIndex + 1) + ". " + question_array.get(currentQIndex).getTitle());                  //제일 먼저 꺼내온 문제가 1번문제
                    aq.id(q_image).image(question_array.get(currentQIndex).getImg_url());
                    r_content.setText(response_array.get(currentQIndex).getContent());
                    String tempString = response_array.get(currentQIndex).getImg_url();
                    if(tempString.substring(tempString.length()-3, tempString.length()).equals("mp4")) {        //동영상일경우
                        r_image.setVisibility(View.GONE);
                        videoView_layout.setVisibility(View.VISIBLE);
                        jcVideoPlayerStandard.setUp(tempString, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "test");
                    } else {                                                                                        //이미지일 경우
                        aq_r.id(r_image).image(response_array.get(currentQIndex).getImg_url());
                        videoView_layout.setVisibility(View.GONE);
                        r_image.setVisibility(View.VISIBLE);
                    }
                    if(response_array.get(currentQIndex).getIs_correct() == 1) {            //정답일시
                        is_correct.setColorFilter(Color.parseColor("#45ACCD"));
                    } else if(response_array.get(currentQIndex).getIs_correct() == 2) {     //틀릴경우
                        is_correct.setColorFilter(Color.parseColor("#ff3232"));
                    }
                }
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
    private class GetExamAndResponse  extends AsyncTask<String, Void, Void> {


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
                System.out.println("파라미터는 " + eid + m_id);
                param.add(new BasicNameValuePair("e_id", eid));           //배열의 길이를 먼저 보냄
                param.add(new BasicNameValuePair("m_id", m_id));           //배열의 길이를 먼저 보냄
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
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
                }

                jsonMainNode = jsonResponse.optJSONArray("exam_response");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String q_id = jsonChildNode.optString("q_id");
                    String img_url = jsonChildNode.optString("img_url");
                    String content = jsonChildNode.optString("content");
                    String q_r_id = jsonChildNode.optString("ori_q_id");
                    String target_id = jsonChildNode.optString("m_id");
                    String correct = jsonChildNode.optString("correct");            //0 = 미제출, 1 = 정답, 2 = 틀림

                    ExamResponse temp = new ExamResponse();
                    temp.setIs_correct(Integer.parseInt(correct));
                    temp.setImg_url(img_url);
                    temp.setQid(q_id);
                    temp.setContent(content);
                    temp.setOri_qid(q_r_id);
                    temp.setM_id(target_id);
                    response_array.add(temp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            exam_number.setText("문제 1. " + question_array.get(0).getTitle());                  //제일 먼저 꺼내온 문제가 1번문제
            aq.id(q_image).image(question_array.get(0).getImg_url());
            String tempString = response_array.get(0).getImg_url();
            if(response_array.get(0).getIs_correct() == 1) {            //정답일시
                is_correct.setColorFilter(Color.parseColor("#45ACCD"));
            } else if(response_array.get(0).getIs_correct() == 2) {     //틀릴경우
                is_correct.setColorFilter(Color.parseColor("#ff3232"));
            }
            if(tempString.substring(tempString.length()-3, tempString.length()).equals("mp4")) {        //동영상일경우
                r_image.setVisibility(View.GONE);
                videoView_layout.setVisibility(View.VISIBLE);
                jcVideoPlayerStandard.setUp(tempString, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "test");
            } else {                                                                                        //이미지일 경우
                aq_r.id(r_image).image(response_array.get(0).getImg_url());
                videoView_layout.setVisibility(View.GONE);
                r_image.setVisibility(View.VISIBLE);
            }
            r_content.setText(response_array.get(0).getContent());
            if(question_array.size() == 1) {
                next_exam.setText("완료");
            }
        }
    }

    private class DecisionCorrect extends AsyncTask<String, Void, Void> {


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
                param.add(new BasicNameValuePair("e_id", eid));           //배열의 길이를 먼저 보냄
                param.add(new BasicNameValuePair("m_id", response_array.get(currentQIndex).getM_id()));           //배열의 길이를 먼저 보냄
                param.add(new BasicNameValuePair("q_id", response_array.get(currentQIndex).getOri_qid()));           //배열의 길이를 먼저 보냄
                param.add(new BasicNameValuePair("is_correct", String.valueOf(isCorrect)));           //배열의 길이를 먼저 보냄
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
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

        }
    }
}
