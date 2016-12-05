package com.pelkan.tab;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * 시험 리스트를 뿌려주는 fragment
 * Created by JangLab on 2016-09-17.
 */
public class TestCheckFragment extends Fragment {
    private static ListView my_exam_list;
    private String json;
    private String eid;
    private String e_id;
    ArrayList<ExamList> question_array = new ArrayList<ExamList>();
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String url_qustion = "http://218.150.182.58:2041/mas/my_check.php";
    static FragmentTransaction trans;

    public TestCheckFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CheckExamActivity.PlaceholderFragment newInstance(int sectionNumber) {
        CheckExamActivity.PlaceholderFragment fragment = new CheckExamActivity.PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_exam1, container, false);
        my_exam_list = (ListView) rootView.findViewById(R.id.my_exam_list);
        setting = getActivity().getSharedPreferences("setting", 0);
        editor= setting.edit();
        new MyExamListTask().execute(url_qustion);

        trans = getActivity().getSupportFragmentManager().beginTransaction();

        my_exam_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {          //문제 리스트 클릭 이벤트
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                TestCheckFragment1 temp = new TestCheckFragment1();

                int touchIndex = (int) arg3;
                // getting values from selected ListItem

                eid = question_array.get(touchIndex).getTarget();
                e_id = question_array.get(touchIndex).getEid();
                Bundle args = new Bundle();
                args.putString("target", eid);
                args.putString("e_id", e_id);
                temp.setArguments(args);
                System.out.println("arg3는 " + arg3);
                System.out.println("pid는 " + eid);

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
                param.add(new BasicNameValuePair("m_id", Login.my_id));
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
            ArrayList<Responser> array = new ArrayList<Responser>();
            System.out.println("튜플 받아옴");
            System.out.println("데이터는 " + json);
            question_array.clear();

            try{
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("question_list");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String e_id = jsonChildNode.optString("e_id");
                    String start_time = jsonChildNode.optString("start_time");
                    String end_time = jsonChildNode.optString("end_time");
                    String e_title = jsonChildNode.optString("e_title");
                    String question_list = jsonChildNode.optString("question_list");
                    String m_id = jsonChildNode.optString("add_user");
                    String target_users = jsonChildNode.optString("target");
                    String[] questionSpit = question_list.split(",");

                    String question_count = String.valueOf(questionSpit.length);

                    ExamList temp = new ExamList();
                    temp.setEid(e_id);
                    temp.setStartTime(start_time);
                    temp.setEndTime(end_time);
                    temp.seteTitle(e_title);
                    temp.setQuestion_count(question_count);
                    temp.setM_id(m_id);
                    temp.setTarget(target_users);

                    question_array.add(temp);
                }
                SelectMyExamAdapter adapter = new SelectMyExamAdapter(getActivity(), R.layout.select_exam, question_array);
                my_exam_list.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
