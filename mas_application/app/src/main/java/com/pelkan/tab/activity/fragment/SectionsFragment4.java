package com.pelkan.tab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
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
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import mehdi.sakout.fancybuttons.FancyButton;

public class SectionsFragment4 extends Fragment {
    View rootView;
    String qid;
    Button ptnLoad;
    TextView question_title;
    TextView question_nickName;
    ImageView question_img;
    ImageView profile_img;
    TextView question_content;
    TextView view_count;
    TextView q_level;
    TextView q_keyword;
    TextView r_count;
    TextView c_count;
    LinearLayout modify_layout;
    FancyButton add_response;
    FancyButton view_correct;
    FancyButton delete_question;
    Boolean is_favorite = false;
    ExpandableHeightListView lv;
    ExpandableHeightListView c_list;
    LinearLayout correct_layout;
    LinearLayout c_list_layout;
    String json;
    String json_response;
    String img_url;
    String r_id;
    String uploaderEmail;
    String profile_img_url;
    private AQuery aq;
    private AQuery profile_aq;
    String url_favorite = "http://218.150.182.58:2041/mas/add_favorite.php";
    JSONObject product;
    ArrayList<String> ridArr = new ArrayList<>();
    ArrayList<String> c_ridArr = new ArrayList<>();
    com.pelkan.tab.JSONParser jsonParser = new com.pelkan.tab.JSONParser();
    public String question_mid = "";
    String temp_title;
    String temp_content;
    String temp_img_url;

    //비디오
    /*
    final static String SAMPLE_VIDEO_URL = "http://218.150.182.45/mas/uploads/test@a/20160726165708209.mp4";
    VideoView videoView;
    SeekBar seekBar;
    Button playBtn;
    Button pauseBtn;
    Handler updateHandler = new Handler();




    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    MediaController mcontroller;
*/
    private static final String url_product_detials = "http://218.150.182.58:2041/mas/question_detail.php";
    private static final String delete_question_url = "http://218.150.182.58:2041/mas/delete_question.php";
    private static final String url_response ="http://218.150.182.58:2041/mas/response_detail.php";
    LongOperation task;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public SectionsFragment4() {

    }
    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment4 newInstance(int SectionNumber){
        SectionsFragment4 fragment = new SectionsFragment4();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page4,
                container, false);
        question_title = (TextView) rootView.findViewById(R.id.question_title);
        question_nickName = (TextView) rootView.findViewById(R.id.quetion_nickname);
        view_count = (TextView) rootView.findViewById(R.id.view_count);
        q_level = (TextView) rootView.findViewById(R.id.q_level);
        q_keyword = (TextView) rootView.findViewById(R.id.q_keyword_list);
        question_img = (ImageView) rootView.findViewById(R.id.q_image);
        question_content = (TextView)rootView.findViewById(R.id.q_content);
        r_count = (TextView)rootView.findViewById(R.id.r_count);
        add_response = (FancyButton)rootView.findViewById(R.id.add_response);
        lv = (ExpandableHeightListView) rootView.findViewById(R.id.r_list);
        profile_img = (ImageView) rootView.findViewById(R.id.profile_img);
        view_correct = (FancyButton) rootView.findViewById(R.id.view_correct);
        c_list = (ExpandableHeightListView) rootView.findViewById(R.id.c_list);
        c_count = (TextView) rootView.findViewById(R.id.c_count);
        c_list_layout = (LinearLayout) rootView.findViewById(R.id.c_list_layout);
        correct_layout = (LinearLayout) rootView.findViewById(R.id.correct_layout);
        modify_layout = (LinearLayout) rootView.findViewById(R.id.modify_layout);
        delete_question = (FancyButton) rootView.findViewById(R.id.delete_question);
        aq = new AQuery(getActivity(), rootView);
        profile_aq = new AQuery(getActivity(), rootView);


        /*      비디오 부분
        playBtn = (Button) rootView.findViewById(R.id.btnPlay);
        pauseBtn = (Button) rootView.findViewById(R.id.btnPause);

        ptnLoad = (Button) rootView.findViewById(R.id.ptnLoad);

        EditText tvURL = (EditText)rootView.findViewById(R.id.etVieoURL);
        tvURL.setText(SAMPLE_VIDEO_URL);

        videoView = (VideoView)rootView.findViewById(R.id.videoView);
        //MediaController mc = new MediaController(this);
        //videoView.setMediaController(mc);

        seekBar = (SeekBar)rootView.findViewById(R.id.seekBar);
        */
        if (getArguments() != null) {                       //클릭한 pid 저장
            qid = getArguments().getString("pid");
        }
        System.out.print(qid);
        lv.setExpanded(true);
        c_list.setExpanded(true);
        //asynchtask = new GetProductDetails();

        System.out.println("황커간다.");                               //httpclien 방식;
        task = new LongOperation();
        task.execute(url_product_detials);


        //동영상 관련 부분 비디오 불러오기
        /*
        ptnLoad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loadVideo(view);
            }
        });

        //재생 버튼
        playBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                playVideo(view);
            }
        });

        //정지 버튼
        pauseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pauseVideo(view);
            }
        });

*/

        delete_question.setOnClickListener(new View.OnClickListener() {        //문제 삭제

            @Override
            public void onClick(View view) {
                new DeleteOperation().execute(delete_question_url);
            }
        });

        view_correct.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                correct_layout.setVisibility(View.VISIBLE);
                c_list_layout.setVisibility(View.VISIBLE);
            }
        });

        question_img.setOnClickListener(new View.OnClickListener() {        //이미지 누르면 큰화면으로 보여주기

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), ImageDetailActivity.class);
                intent1.putExtra("img_url", img_url);
                System.out.println("이미지는 " + img_url);
                startActivity(intent1);
            }
        });

        add_response.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment6 temp = new SectionsFragment6();
                Bundle args = new Bundle();
                args.putString("qid", qid);
                temp.setArguments(args);

                trans.replace(R.id.root_frame, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });

        c_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment5 temp = new SectionsFragment5();
                int touchIndex = (int) arg3;
                // getting values from selected ListItem
                r_id = c_ridArr.get(touchIndex).toString();
                Bundle args = new Bundle();
                args.putString("rid", r_id);
                args.putString("mid", question_mid);
                temp.setArguments(args);

                trans.replace(R.id.root_frame, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment5 temp = new SectionsFragment5();
                int touchIndex = (int) arg3;
                // getting values from selected ListItem
                r_id = ridArr.get(touchIndex).toString();
                Bundle args = new Bundle();
                args.putString("rid", r_id);
                args.putString("mid", question_mid);
                temp.setArguments(args);

                trans.replace(R.id.root_frame, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();

            }
        });
        //Button btn =  (Button)rootView.findViewById(R.id.addBtn);
        System.out.println("fragment 종료");
        return rootView;
    }

    @Override
    public void onDestroy() {
        if (task!=null) {
            task.cancel(true);
        }
        super.onDestroy();
    }

    /*
        public void loadVideo(View view) {
            //Sample video URL : http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_2mb.mp4
            EditText tvURL = (EditText) rootView.findViewById(R.id.etVieoURL);
            String url = tvURL.getText().toString();

            Toast.makeText(getActivity().getApplicationContext(), "Loading Video. Plz wait", Toast.LENGTH_LONG).show();
            videoView.setVideoURI(Uri.parse(url));
            videoView.requestFocus();

            // 토스트 다이얼로그를 이용하여 버퍼링중임을 알린다.
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {

                                            @Override
                                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                                switch(what){
                                                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                                        // Progress Diaglog 출력
                                                        Toast.makeText(getActivity().getApplicationContext(), "Buffering", Toast.LENGTH_LONG).show();
                                                        break;
                                                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                                        // Progress Dialog 삭제
                                                        Toast.makeText(getActivity().getApplicationContext(), "Buffering finished.\nResume playing", Toast.LENGTH_LONG).show();
                                                        videoView.start();
                                                        break;
                                                }
                                                return false;
                                            }
                                        }

            );

            // 플레이 준비가 되면, seekBar와 PlayTime을 세팅하고 플레이를 한다.
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                    long finalTime = videoView.getDuration();
                    TextView tvTotalTime = (TextView) rootView.findViewById(R.id.tvTotalTime);
                    tvTotalTime.setText(String.format("%d:%d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                    );
                    seekBar.setMax((int) finalTime);
                    seekBar.setProgress(0);
                    updateHandler.postDelayed(updateVideoTime, 100);
                    //Toast Box
                    Toast.makeText(getActivity().getApplicationContext(), "Playing Video", Toast.LENGTH_SHORT).show();
                }
            });
        }


        public void playVideo(View view){
            videoView.requestFocus();
            videoView.start();

        }

        public void pauseVideo(View view){
            videoView.pause();
        }

        // seekBar를 이동시키기 위한 쓰레드 객체
        // 100ms 마다 viewView의 플레이 상태를 체크하여, seekBar를 업데이트 한다.
        private Runnable updateVideoTime = new Runnable(){
            public void run(){
                long currentPosition = videoView.getCurrentPosition();
                seekBar.setProgress((int) currentPosition);
                updateHandler.postDelayed(this, 100);

            }
        };

    */

    class AddFavorite extends AsyncTask<String, String, String> {              //업로드

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", uploaderEmail));
            params.add(new BasicNameValuePair("my_email", Login.my_id));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_favorite,
                    "POST", params);

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {}

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
                param.add(new BasicNameValuePair("qid", qid));
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
                //위 StringBuilder값을 : sb를 Content 변수에 저장함
                json = sb.toString();
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
            ArrayList<Responser> array = new ArrayList<Responser>();
            ArrayList<Responser> correct_array = new ArrayList<Responser>();
            if(json == null)
                return;
            System.out.println("튜플 받아옴");
            System.out.println("데이터는 " + json);

            try{
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("question_list");
                product = jsonMainNode.getJSONObject(0);
                System.out.println("오브젝트는 " + product.toString());

                question_title.setText(product.getString("title"));
                question_nickName.setText(product.getString("m_id"));
                question_mid = product.getString("m_id");
                question_content.setText(product.getString("q_content"));
                q_keyword.setText(product.getString("keyword"));
                view_count.setText("조회수 " + product.getString("view_count"));
                q_level.setText("Level " + product.getString("difficulty"));
                r_count.setText(product.getString("response_count"));
                img_url = product.getString("q_img_url");

                temp_content = product.getString("q_content");
                temp_title = product.getString("title");
                temp_img_url = product.getString("q_img_url");

/*
                DetailImageLoader imgLoader = new DetailImageLoader(getActivity().getApplicationContext());
                imgLoader.DisplayImage(img_url, question_img);       //String 타입의 url 받아서 로더에 url 넘겨서 출력한당 url이랑 이미지 뷰
                System.out.println("이미지 다그림");
*/
                for(int i = 0; i < SectionsFragment3.favorite_list.size(); i++) {       //선호하는 사용자인지 체크해서 선호하는 사용자일 경우 별색 바꾸기
                    if(SectionsFragment3.favorite_list.get(i).equals(product.getString("m_id"))) {
                        is_favorite = true;
                    }
                }
                jsonMainNode = jsonResponse.optJSONArray("response_list");
                System.out.println("예압");
                //답변글 출력
                if(jsonMainNode.toString().equals("[0]")) {     //없을 시에
                    System.out.println("끝낫냐");
                    array.clear();
                } else {                                        //있을 시에
                    System.out.println("아니");
                    uploaderEmail = product.getString("m_id");
                    System.out.println("답변글 숫자 시작");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        ridArr.add(jsonChildNode.optString("r_id"));
                        String r_id = jsonChildNode.optString("r_m_id");
                        String title = jsonChildNode.optString("r_title");
                        Responser temp = new Responser();
                        temp.setR_id(r_id);
                        temp.setTitle(title);
                        array.add(temp);
                    }
                    System.out.println("답변글 다 받음");

                    ResponseAdapter adapter = new ResponseAdapter(getActivity().getApplicationContext(), R.layout.re_list, array);
                    lv.setAdapter(adapter);
                    System.out.println("답변글 설정 완료");
                }

                jsonMainNode = jsonResponse.optJSONArray("correct_list");
                System.out.println("예압");
                //답변글 출력
                if(jsonMainNode.toString().equals("[0]")) {     //없을 시에
                    System.out.println("끝낫냐");
                    correct_array.clear();
                } else {                                        //있을 시에
                    System.out.println("아니");
                    System.out.println("답변글 숫자 시작");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        c_ridArr.add(jsonChildNode.optString("r_id"));
                        String r_id = jsonChildNode.optString("r_m_id");
                        String title = jsonChildNode.optString("r_title");
                        Responser temp = new Responser();
                        temp.setR_id(r_id);
                        temp.setTitle(title);
                        correct_array.add(temp);
                    }
                    System.out.println("답변글 다 받음");

                    CorrectResponseAdapter adapter = new CorrectResponseAdapter(getActivity().getApplicationContext(), R.layout.re_list, correct_array);
                    c_list.setAdapter(adapter);
                    System.out.println("답변글 설정 완료");
                }

                jsonMainNode = jsonResponse.optJSONArray("profile");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                profile_img_url = jsonChildNode.optString("profile_img_url");
            } catch (JSONException e){
                e.printStackTrace();
            }
            aq.id(question_img).image(img_url);
            profile_aq.id(profile_img).image(profile_img_url);

            if(Login.my_id.equals(question_nickName.getText().toString())) {
                modify_layout.setVisibility(View.VISIBLE);
            }
        }
    }

    private class DeleteOperation  extends AsyncTask<String, Void, Void> {


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
                param.add(new BasicNameValuePair("q_id", qid));
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
                //위 StringBuilder값을 : sb를 Content 변수에 저장함
                json = sb.toString();
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
            getFragmentManager().popBackStack();
            Toast.makeText(getActivity().getApplicationContext(), "문제삭제 완료", Toast.LENGTH_LONG).show();
        }
    }
}