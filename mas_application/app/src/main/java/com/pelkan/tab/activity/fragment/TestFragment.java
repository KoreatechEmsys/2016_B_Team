package com.pelkan.tab;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 시험 출제에서 문제 리스트 뿌려주는 fragment
 * Created by JangLab on 2016-09-17.
 */
public class TestFragment extends Fragment {
    private static ListView my_question_list;
    private String json;
    private static int list_size;
    ArrayList<QuestionList> question_array = new ArrayList<QuestionList>();
    public static ArrayList<Boolean> is_checked_list = new ArrayList<Boolean>();
    public static ArrayList<String> qidArr = new ArrayList<String>();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String url_qustion = "http://218.150.182.58:2041/mas/my_questions.php";
    static FragmentTransaction trans;

    public TestFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AddExamActivity.PlaceholderFragment newInstance(int sectionNumber) {
        AddExamActivity.PlaceholderFragment fragment = new AddExamActivity.PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_exam1, container, false);
        my_question_list = (ListView) rootView.findViewById(R.id.my_question_list);
        new MyQuestionListTask().execute(url_qustion);

        trans = getActivity().getSupportFragmentManager().beginTransaction();
        my_question_list.isItemChecked(0);
        return rootView;
    }

    public static void nextStap() {
        boolean[] checkedArr = new boolean[is_checked_list.size()];
        for(int i = 0; i < is_checked_list.size(); i++) {
            checkedArr[i] = is_checked_list.get(i);
        }
        TestFragment1 temp = new TestFragment1();
        Bundle args = new Bundle();
        args.putStringArrayList("qid", qidArr);
        args.putBooleanArray("checkd", checkedArr);
        temp.setArguments(args);
        trans.replace(R.id.root_frame, temp);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);

        trans.commit();
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
    private class MyQuestionListTask  extends AsyncTask<String, Void, Void> {


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
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.KOREA );
            Date currentTime = new Date( );
            Date compareDate = null;
            String mTime = mSimpleDateFormat.format ( currentTime );
            ArrayList<Responser> array = new ArrayList<Responser>();
            System.out.println("튜플 받아옴");
            System.out.println("데이터는 " + json);
            question_array.clear();
            qidArr.clear();

            try{
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("question_list");
                currentTime = mSimpleDateFormat.parse(mTime);
                list_size = jsonMainNode.length();
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String q_id = jsonChildNode.optString("q_id");
                    String title = jsonChildNode.optString("title");
                    String keyword = jsonChildNode.optString("keyword");
                    String viewCount = jsonChildNode.optString("view_count");
                    String responseCount = jsonChildNode.optString("response_count");
                    String successCount = jsonChildNode.optString("success_count");
                    String addDate = jsonChildNode.optString("reg_date");
                    String[] keywordspit = keyword.split(",");
                    keyword = keywordspit[0];

                    compareDate = mSimpleDateFormat.parse(addDate.substring(0, 10));        //년 월 일 추출
                    if(currentTime.compareTo(compareDate) == 0) {                           //날짜가 같으면 시, 분 추출
                        addDate = addDate.substring(11, 16);                                //00:00 형식
                    } else {                                                                //다른 날이면 년 월 일 추출
                        addDate = addDate.substring(5, 10);
                        addDate = addDate.replaceAll("-", ".");
                    }

                    QuestionList temp = new QuestionList();
                    temp.setQid(q_id);
                    temp.setKeywords(keyword);
                    temp.setViewCount(viewCount);
                    temp.setTitle(title);
                    temp.setSuccess_count(successCount);
                    temp.setAddDate(addDate);
                    temp.setResponseCount(responseCount);
                    question_array.add(temp);
                }
                SelectMyQuestionAdapter adapter = new SelectMyQuestionAdapter(getActivity(), R.layout.select_question, question_array);
                my_question_list.setAdapter(adapter);
            } catch (JSONException e){
                e.printStackTrace();
            } catch (ParseException e ) {
            }
        }
    }
}
