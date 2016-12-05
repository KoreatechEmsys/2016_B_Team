package com.pelkan.tab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import mehdi.sakout.fancybuttons.FancyButton;

//풀이보기 fragment
public class SectionsFragment5 extends Fragment {
    String rid;
    TextView title;
    TextView view_count;
    TextView responser_id;
    String is_correct = "";
    String r_m_id;
    ImageView image;
    ImageView profile_img;
    TextView content;
    LinearLayout correct_response;
    FancyButton correctBtn;
    String json;
    String img_url;
    String m_id;
    String profile_img_url;
    LinearLayout videoView_layout;
    JCVideoPlayerStandard jcVideoPlayerStandard;
    JSONObject product;
    private AQuery aq;
    private AQuery profile_aq;
    ArrayList<Integer> pidArr = new ArrayList<>();
    com.pelkan.tab.JSONParser jsonParser = new com.pelkan.tab.JSONParser();
    private static final String url_add_correct ="http://218.150.182.58:2041/mas/add_correct.php";
    private static final String url_response ="http://218.150.182.58:2041/mas/response.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public SectionsFragment5() {

    }
    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment5 newInstance(int SectionNumber){
        SectionsFragment5 fragment = new SectionsFragment5();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page5,
                container, false);
        title = (TextView) rootView.findViewById(R.id.response_title);
        view_count = (TextView) rootView.findViewById(R.id.view_count);
        responser_id = (TextView) rootView.findViewById(R.id.responser_id);
        image = (ImageView) rootView.findViewById(R.id.r_image);
        content = (TextView) rootView.findViewById(R.id.r_content);
        correct_response = (LinearLayout) rootView.findViewById(R.id.add_correct);
        correctBtn = (FancyButton) rootView.findViewById(R.id.correct);
        profile_img = (ImageView) rootView.findViewById(R.id.profile_img);
        videoView_layout = (LinearLayout) rootView.findViewById(R.id.videoView_layout);
        aq = new AQuery(getActivity(), rootView);
        profile_aq = new AQuery(getActivity(), rootView);
        jcVideoPlayerStandard = (JCVideoPlayerStandard) rootView.findViewById(R.id.videoView);

        if (getArguments() != null) {                       //클릭한 pid 저장
            rid = getArguments().getString("rid");
            m_id = getArguments().getString("mid");
            if(m_id.equals(Login.my_id)) {
                correct_response.setVisibility(View.VISIBLE);
            }
        }
        System.out.print(rid);
        new LongOperation().execute(url_response);

        image.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), ImageDetailActivity.class);
                intent1.putExtra("img_url", img_url);
                System.out.println("이미지는 " + img_url);
                startActivity(intent1);
            }
        });

        correctBtn.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                new AddCorrect().execute(url_add_correct);
            }
        });
        //Button btn =  (Button)rootView.findViewById(R.id.addBtn);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
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
                param.add(new BasicNameValuePair("rid", rid));
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
            try{
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("response");
                product = jsonMainNode.getJSONObject(0);

                title.setText(product.getString("r_title"));
                r_m_id = product.getString("r_m_id");
                responser_id.setText(r_m_id);
                content.setText(product.getString("r_content"));
                view_count.setText("조회수 " + product.getString("view_count"));
                img_url = product.getString("r_img_url");
                is_correct = product.getString("is_correct");

                jsonMainNode = jsonResponse.optJSONArray("profile");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                profile_img_url = jsonChildNode.optString("profile_img_url");
                profile_aq.id(profile_img).image(profile_img_url);
            } catch (JSONException e){
                e.printStackTrace();
            }
            if(is_correct.equals("1")) {
                correctBtn.setText("정답취소");
            }
            String tempString = img_url;
            System.out.println("gg" + tempString);
            System.out.println("ㅇㅇ" + tempString.substring(tempString.length()-3, tempString.length()));
            if(tempString.substring(tempString.length()-3, tempString.length()).equals("mp4")) {            //동영상일 경우
                image.setVisibility(View.GONE);
                videoView_layout.setVisibility(View.VISIBLE);
                jcVideoPlayerStandard.setUp(img_url, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "test");
                System.out.println("url은 " + img_url);
            } else {                                                                                        //이미지일 경우
                aq.id(image).image(img_url);
                videoView_layout.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }
        }
    }

    private class AddCorrect  extends AsyncTask<String, Void, Void> {


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
                param.add(new BasicNameValuePair("rid", rid));
                param.add(new BasicNameValuePair("is_correct", is_correct));
                param.add(new BasicNameValuePair("r_m_id", r_m_id));
                System.out.println("전달된값 " + rid);
                System.out.println("전달된값 " + r_m_id);
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
            System.out.println("계산 " + json);
            if(is_correct.equals("1")) {
                Toast.makeText(getActivity().getApplicationContext(),       //버튼을 회색으로 바꾸기, db에서 쿼리를 통해 해당 사용자 삭제
                        "정답 목록에서 제거되었습니다.",
                        Toast.LENGTH_SHORT).show();
                correctBtn.setText("정답등록");
                is_correct = "0";
            } else {
                Toast.makeText(getActivity().getApplicationContext(),       //버튼을 회색으로 바꾸기, db에서 쿼리를 통해 해당 사용자 삭제
                        "정답 목록에 추가되었습니다.",
                        Toast.LENGTH_SHORT).show();
                correctBtn.setText("정답취소");
                is_correct = "1";
            }
        }
    }
}