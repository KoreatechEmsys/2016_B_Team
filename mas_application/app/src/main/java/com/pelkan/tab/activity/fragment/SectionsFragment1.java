package com.pelkan.tab;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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

public class SectionsFragment1 extends Fragment {
    private ProgressDialog pDialog;
    private String jsonResult;
    private String url = "http://218.150.182.58:2041/mas/employee_details.php";
    private ListView lv;
    private ListView best;
    private View rootView;
    private String best_qid = "";
    private boolean mLockListView;
    private boolean levelSorted = false;
    private boolean keywordSorted = false;
    private boolean regDateSorted = false;
    private boolean responseSorted = false;
    private boolean viewSorted = false;

//    private TextView key_sort;
//    private TextView level_sort;
//    private TextView response_sort;
//    private TextView view_sort;
//    private TextView reg_date_sort;
    private LayoutInflater mInflater;
    private FrameLayout temp;
    public String pid="";
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    ArrayList<Integer> pidArr = new ArrayList<>();                  //문제의 리스트의 id
    ArrayList<QuestionList> question_array = new ArrayList<QuestionList>();    //문제 리스트
    QuestionList temp_Question = new QuestionList();
    ArrayList<QuestionList> question_array_date = new ArrayList<QuestionList>();    //날짜를 위한 리스트
    ArrayList<Integer> indexArray = new ArrayList<Integer>();

    public SectionsFragment1() {

    }

    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment1 newInstance(int SectionNumber){
        SectionsFragment1 fragment = new SectionsFragment1();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page1, container, false);
        mLockListView = true;

        best = (ListView) rootView.findViewById(R.id.rcmd_question);
        lv = (ListView) rootView.findViewById(R.id.list);
//        key_sort = (TextView) rootView.findViewById(R.id.keyword_sort);
//        level_sort = (TextView) rootView.findViewById(R.id.level_sort);
//        response_sort = (TextView) rootView.findViewById(R.id.response_sort);
//        reg_date_sort = (TextView) rootView.findViewById(R.id.reg_date_sort);
//        view_sort = (TextView) rootView.findViewById(R.id.view_count_sort);
        setting = getActivity().getSharedPreferences("setting", 0);
        editor= setting.edit();

        //    temp = (FrameLayout) rootView.findViewById(R.id.temp_lay);


//        ArrayList<MainProduct> array = new ArrayList<MainProduct>();
//        final MainProduct temp = new MainProduct();
//        temp.setLevel("난이도 5");
//        temp.setKeywords("확률, 도함수, 확률 분포");
//        temp.setReponseCount("풀이수 5");
//        temp.setViewCount("조회수 150");
//        temp.setImage_url("http://218.150.182.45/mas/img/best_test.png");
//        array.add(temp);
//
//        MainCustomAdapter adapter = new MainCustomAdapter(getActivity(), R.layout.main_list, array);
//        best.setAdapter(adapter);
//
//
//        ArrayList<MainProduct> array = new ArrayList<MainProduct>();
//        final MainProduct temp = new MainProduct();
//        temp.setKeywords("통계, 적분");
//        temp.setViewCount("82");
//        temp.setTitle("모평균과 모비율 추정 문제");
//        temp.setSuccessCount("8");
//        temp.setResponseCount("10");
//        temp.setAddDate("08.02");
//        array.add(temp);
//
//        MainCustomAdapter adapter1 = new MainCustomAdapter(getActivity(), R.layout.main_list, array);
//        best.setAdapter(adapter1);


//
//
//
//
//
//
//
//
//
//        QuestionList test_temp = new QuestionList();
//        test_temp.setKeywords("적분, 부정적분");
//        test_temp.setViewCount("4");
//        test_temp.setTitle("함수에 대한 부정적분 계산 문제");
//        test_temp.setSuccess_count("0");
//        test_temp.setResponseCount("0");
//        test_temp.setAddDate("10:24");
//        question_array.add(test_temp);
//
//        QuestionList test_temp1 = new QuestionList();
//        test_temp1.setKeywords("확률, 경우의 수");
//        test_temp1.setViewCount("15");
//        test_temp1.setTitle("서로 다른색의 공 뽑기 문제");
//        test_temp1.setSuccess_count("2");
//        test_temp1.setResponseCount("3");
//        test_temp1.setAddDate("09.11");
//        question_array.add(test_temp1);
//
//        QuestionList test_temp2 = new QuestionList();
//        test_temp2.setKeywords("도형, 부피");
//        test_temp2.setViewCount("11");
//        test_temp2.setTitle("삼각뿔의 부피 문제");
//        test_temp2.setSuccess_count("1");
//        test_temp2.setResponseCount("5");
//        test_temp2.setAddDate("09.01");
//        question_array.add(test_temp2);
//
//        QuestionList test_temp3 = new QuestionList();
//        test_temp3.setKeywords("집합, 부분집합");
//        test_temp3.setViewCount("34");
//        test_temp3.setTitle("두 부분집합간의 관계 문제");
//        test_temp3.setSuccess_count("6");
//        test_temp3.setResponseCount("8");
//        test_temp3.setAddDate("08.28");
//        question_array.add(test_temp3);
//
//        QuestionList test_temp4 = new QuestionList();
//        test_temp4.setKeywords("함수, 직선의 방정식");
//        test_temp4.setViewCount("57");
//        test_temp4.setTitle("직선의 방정식 도출");
//        test_temp4.setSuccess_count("9");
//        test_temp4.setResponseCount("12");
//        test_temp4.setAddDate("08.05");
//        question_array.add(test_temp4);
//
//
//        MainQuestionAdapter adapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array);
//        lv.setAdapter(adapter);
/*
        이건 리스트 테스트용
        QuestionList te = new QuestionList();
        te.setQid("1");
        te.setKeywords("확률");
        te.setViewCount("5");
        te.setM_id("아이디");
        te.setTitle("제목");
        te.setSuccess_count("정답수");
        te.setAddDate("날짜");
        te.setResponseCount("대답수");
        question_array.add(te);

        MainQuestionAdapter aadapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array);
        lv.setAdapter(aadapter);
*/
        //TextView hah = (TextView)rootView.findViewById(R.id.aaa);
        //여기까지임

        System.out.println("연결전");
        new LongOperation().execute(url);

        best.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {          //문제 리스트 클릭 이벤트
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment4 temp = new SectionsFragment4();

                int touchIndex = (int) arg3;
                // getting values from selected ListItem

                if(best_qid.equals("")) {
                    Toast.makeText(getActivity(), "오늘의 문제를 추출할 데이터가 부족합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    pid = best_qid;
                    Bundle args = new Bundle();
                    args.putString("pid", pid);
                    temp.setArguments(args);
                    System.out.println("arg3는 " + arg3);
                    System.out.println("pid는 " + pid);

                    trans.replace(R.id.root_frame, temp);
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);

                    trans.commit();
                    System.out.println("리스트 전환 완료 및 fragment 시작");
                }
            }
        });

        if(setting.getBoolean("is_searched", true)){
            editor.putBoolean("is_searched", false);
            editor.commit();
            FragmentTransaction trans = getFragmentManager()
                    .beginTransaction();
            SectionsFragment4 temp_fragment = new SectionsFragment4();
            Bundle args = new Bundle();
            args.putString("pid", setting.getString("search_pid", ""));
            temp_fragment.setArguments(args);
            trans.replace(R.id.root_frame, temp_fragment);
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);

            trans.commit();
        }

        //키워드 별 소팅, 가나다 순,
//        key_sort.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if(keywordSorted == false) {
//                    Collections.sort(question_array, new NameAscCompare());
//                    keywordSorted = true;
//                    for(int i = 0; i < question_array.size(); i++)
//                        System.out.println("키워드별 정렬" + question_array.get(i).getKeywords());
//                } else {
//                    Collections.sort(question_array, new NameAscCompare());
//                    Collections.reverse(question_array);
//
//                    keywordSorted = false;
//                    for(int i = 0; i < question_array.size(); i++)
//                        System.out.println("키워드별 정렬" + question_array.get(i).getKeywords());
//                }
//
//                MainQuestionAdapter adapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array);
//                lv.setAdapter(adapter);
//            }
//        });
//
//        //난이도 별 소팅
//        level_sort.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                // creating new product in background thread
//                /*
//                if(levelSorted == false) {
//                    for (int i = 0; i < question_array.size(); i++)
//                        for (int j = i + 1; j < question_array.size(); j++) {
//                            if (Integer.parseInt(question_array.get(i).getLevel()) > Integer.parseInt(question_array.get(j).getLevel())) {
//                                temp_Question = question_array.get(i);
//                                question_array.set(i, question_array.get(j));
//                                question_array.set(j, temp_Question);
//                            }
//                        }
//                    levelSorted = true;
//                } else {
//                    for (int i = 0; i < question_array.size(); i++)
//                        for (int j = i + 1; j < question_array.size(); j++) {
//                            if (Integer.parseInt(question_array.get(i).getLevel()) > Integer.parseInt(question_array.get(j).getLevel())) {
//                                temp_Question = question_array.get(i);
//                                question_array.set(i, question_array.get(j));
//                                question_array.set(j, temp_Question);
//                            }
//                        }
//                    Collections.reverse(question_array);
//                    levelSorted = false;
//                }
//
//                MainQuestionAdapter adapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array);
//                lv.setAdapter(adapter);
//                */
//            }
//        });
//
//        //풀이수 소팅
//        response_sort.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(responseSorted == false) {
//                    for (int i = 0; i < question_array.size(); i++)
//                        for (int j = i + 1; j < question_array.size(); j++) {
//                            if (Integer.parseInt(question_array.get(i).getResponseCount()) > Integer.parseInt(question_array.get(j).getResponseCount())) {
//                                temp_Question = question_array.get(i);
//                                question_array.set(i, question_array.get(j));
//                                question_array.set(j, temp_Question);
//                            }
//                        }
//                    responseSorted = true;
//                } else {
//                    for (int i = 0; i < question_array.size(); i++)
//                        for (int j = i + 1; j < question_array.size(); j++) {
//                            if (Integer.parseInt(question_array.get(i).getResponseCount()) > Integer.parseInt(question_array.get(j).getResponseCount())) {
//                                temp_Question = question_array.get(i);
//                                question_array.set(i, question_array.get(j));
//                                question_array.set(j, temp_Question);
//                            }
//                        }
//                    Collections.reverse(question_array);
//                    responseSorted = false;
//                }
//
//                MainQuestionAdapter adapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array);
//                lv.setAdapter(adapter);
//            }
//        });
//
//        //조회수 소팅
//        view_sort.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(viewSorted == false) {
//                    for (int i = 0; i < question_array.size(); i++)
//                        for (int j = i + 1; j < question_array.size(); j++) {
//                            if (Integer.parseInt(question_array.get(i).getViewCount()) > Integer.parseInt(question_array.get(j).getViewCount())) {
//                                temp_Question = question_array.get(i);
//                                question_array.set(i, question_array.get(j));
//                                question_array.set(j, temp_Question);
//                            }
//                        }
//                    viewSorted = true;
//                } else {
//                    for (int i = 0; i < question_array.size(); i++)
//                        for (int j = i + 1; j < question_array.size(); j++) {
//                            if (Integer.parseInt(question_array.get(i).getViewCount()) > Integer.parseInt(question_array.get(j).getViewCount())) {
//                                temp_Question = question_array.get(i);
//                                question_array.set(i, question_array.get(j));
//                                question_array.set(j, temp_Question);
//                            }
//                        }
//                    Collections.reverse(question_array);
//                    viewSorted = false;
//                }
//
//                MainQuestionAdapter adapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array);
//                lv.setAdapter(adapter);
//            }
//        });
//
//        //등록일 소팅
//        reg_date_sort.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if(regDateSorted == false) {
//                    Collections.reverse(question_array_date);
//                    regDateSorted = true;
//                } else {
//                    Collections.reverse(question_array_date);
//                    regDateSorted = false;
//                }
//                for (int i = 0; i < question_array.size(); i++)
//                    question_array.set(i, question_array_date.get(i));
//
//                MainQuestionAdapter adapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array_date);
//                lv.setAdapter(adapter);
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {          //문제 리스트 클릭 이벤트
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment4 temp = new SectionsFragment4();

                int touchIndex = (int) arg3;
                // getting values from selected ListItem

                pid = question_array.get(touchIndex).getQid().toString();
                Bundle args = new Bundle();
                args.putString("pid", pid);
                temp.setArguments(args);
                System.out.println("arg3는 " + arg3);
                System.out.println("pid는 " + pid);

                trans.replace(R.id.root_frame, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
                System.out.println("리스트 전환 완료 및 fragment 시작");
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

    private class LongOperation  extends AsyncTask<String, Void, Void> {


        protected void onPreExecute() {

        }

        // onPreExecute 메소드 이후 호출
        protected Void doInBackground(String... urls) {//클릭이벤트 실행시 파라미터값 urls= LongOperation().execute(serverURL);

            System.out.println("연결 준비");
            BufferedReader reader = null;

            // 데이터 전송
            try {
                // Lab code here....
                //Data를 보낼 URL =
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
                //위 StringBuilder값을 : sb를 Content 변수에 저장함
                jsonResult = sb.toString();
                System.out.println("황커 온다1");
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
            if(jsonResult == null)
                return;
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.KOREA );
            Date currentTime = new Date( );
            Date compareDate = null;
            String mTime = mSimpleDateFormat.format ( currentTime );
            question_array.clear();
            question_array_date.clear();

            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                if(jsonResult == null)
                    return;
                JSONArray jsonMainNode = jsonResponse.optJSONArray("question_list");
                currentTime = mSimpleDateFormat.parse(mTime);                               //현재 시간은 Date 타입으로 구함

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
/*
이메일 제거
                int charIndex = m_id.indexOf("@");                                      //아이디 이메일 앞부분 추출
                m_id = m_id.substring(0, charIndex);
*/
                    pidArr.add(i, Integer.parseInt(q_id));
                    QuestionList temp = new QuestionList();
                    temp.setQid(q_id);
                    temp.setKeywords(keyword);
                    temp.setViewCount(viewCount);
                    temp.setTitle(title);
                    temp.setSuccess_count(successCount);
                    temp.setAddDate(addDate);
                    temp.setResponseCount(responseCount);
                    question_array.add(temp);
                    question_array_date.add(temp);
                }
                jsonMainNode = jsonResponse.optJSONArray("map_list");
                Start.maps.clear();
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    ArrayList<Integer> tempArr = new ArrayList<Integer>();
                    ArrayList<Integer> tempArr1 = new ArrayList<Integer>();
                    String name = jsonChildNode.optString("keyword");
                    String grade = jsonChildNode.optString("grade");
                    KnowledgeMap temp = new KnowledgeMap();
                    temp.setName(name);
                    temp.setGrade(Float.parseFloat(grade));

                    Start.maps.add(temp);
                }

                MainQuestionAdapter adapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array);
                lv.setAdapter(adapter);
                jsonMainNode = jsonResponse.optJSONArray("best");

                if(jsonMainNode.toString().equals("[0]")) {     //없을 시에
                    return;
                } else {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
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

                    ArrayList<MainProduct> array = new ArrayList<MainProduct>();
                    final MainProduct temp = new MainProduct();
                    temp.setQ_id(q_id);
                    temp.setKeywords(keyword);
                    temp.setViewCount(viewCount);
                    temp.setTitle(title);
                    temp.setSuccessCount(successCount);
                    temp.setResponseCount(responseCount);
                    temp.setAddDate(addDate);
                    array.add(temp);
                    best_qid = q_id;

                    MainCustomAdapter adapter1 = new MainCustomAdapter(getActivity(), R.layout.main_list, array);
                    best.setAdapter(adapter1);

                    for (int i = 0; i < question_array.size(); i++) {
                        System.out.println("내부의 데이터는 " + question_array.get(i).getQid());
                    }
                }
            } catch (JSONException e) {
                System.out.println("에러 싴발 2");
            } catch (ParseException e ) {
            }
        }
    }
}