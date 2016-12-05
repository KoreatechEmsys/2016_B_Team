package com.pelkan.tab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import mehdi.sakout.fancybuttons.FancyButton;

public class SectionsFragment2 extends Fragment {

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PICK_FROM_GALLERY = 300;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static boolean add_image = false;

    private Uri fileUri; // file url to store image/video

    private Button btnCapturePicture, btnRecordVideo;

    private ProgressDialog pDialog;

    private ArrayList<String> ke_list = new ArrayList<String>();
    private Set<String> keyword;
    private AutoCompleteTextView keyword_search_view;
    InputMethodManager imm;
    com.pelkan.tab.JSONParser jsonParser = new com.pelkan.tab.JSONParser();
    EditText content;
    EditText title;
    TextView keyword_list;
    String notice_content;
    String question_level;
    String temp_title;
    FancyButton add_keyword;
    Spinner spinner;
    String gallery_image_path;
    String jsonResult;
    String all_keyword = "";
    String add_keyword_list = "";
    ImageView myimage;
    ImageButton uploadBtn;
    ImageButton galleryBtn;
    final CharSequence[] items = { "카메라", "갤러리" };
    //SharedPreferences setting = getActivity().getPreferences(0);
    //SharedPreferences.Editor editor = setting.edit();
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    View rootView;

    // url to create new product
    private String url_create_product = "http://218.150.182.58:2041/mas/add_content.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public SectionsFragment2() {

    }

    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment2 newInstance(int SectionNumber){
        SectionsFragment2 fragment = new SectionsFragment2();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page2,
                container, false);
        content = (EditText) rootView.findViewById(R.id.add_content);
        add_keyword = (FancyButton) rootView.findViewById(R.id.add_keyword);
        title = (EditText) rootView.findViewById(R.id.title);
        keyword_list = (TextView) rootView.findViewById(R.id.my_keyword_list);
        myimage = (ImageView) rootView.findViewById(R.id.afterImage);
        spinner = (Spinner) rootView.findViewById(R.id.q_level);
        galleryBtn = (ImageButton) rootView.findViewById(R.id.galleryBtn);

        setting = getActivity().getSharedPreferences("setting", 0);
        editor= setting.edit();
        //sAdapter = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);

        //spinner.setAdapter(sAdapter);

        // Create button
        FancyButton btnCreateProduct = (FancyButton) rootView.findViewById(R.id.add_button);
        uploadBtn = (ImageButton) rootView.findViewById(R.id.uploadBtn);

        JsonReadTask task = new JsonReadTask();
        task.execute(new String[]{"http://218.150.182.58:2041/mas/keyword_list.php"});

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "현재 사용하시고 있는 기기는 카메라를 지원하지 않습니다!",
                    Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        galleryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                takeImage();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!(setting.getString("is_add_img_true", "").equals("1"))) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "문제와 관련된 이미지나 동영상을 올려주세요",
                            Toast.LENGTH_SHORT).show();
                } else {
                    add_image = false;
                    temp_title = title.getText().toString();
                    notice_content = content.getText().toString();

                    if(ke_list.size() != 1) {
                        for (int i = 0; i < ke_list.size() - 1; i++) {
                            all_keyword += ke_list.get(i) + ",";
                            System.out.println(ke_list.get(i));
                        }
                    } else {
                        question_level = spinner.getSelectedItem().toString();
                        all_keyword += ke_list.get(ke_list.size() - 1);
                    }
                    System.out.println("키워드들은 ");
                    System.out.println(all_keyword);
                    if(all_keyword.equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "문제에 대한 키워드를 추가해주세요", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        if(ke_list.size() != 1){
                            question_level = spinner.getSelectedItem().toString();
                            all_keyword += ke_list.get(ke_list.size() - 1);
                        }
                        System.out.println("싴발");
                        new LongOperation().execute(url_create_product);
//                    new CreateNewContent().execute();
                        editor.remove("temp_title");
                        editor.remove("temp_content");
                        editor.commit();
                    }
                }
            }
        });

        add_keyword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                add_keyword_list += keyword_search_view.getText().toString() + " ";
                ke_list.add(keyword_search_view.getText().toString());
                keyword_list.setText(add_keyword_list);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(getActivity()).addApi(AppIndex.API).build();

        System.out.println("2번째 끝");

        return rootView;
    }

    public void takeImage() {
        /*
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요"), PICK_FROM_GALLERY);
*/
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }
/*
    @Override
    void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    void onRestoreInstanceState(Bundle savedInstanceState) {
        super.getActivity().onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true, requestCode);


            } else if (resultCode == Activity.RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                fileUri = data.getData();
                gallery_image_path = getRealPathFromURI(getActivity(), fileUri);
                //gallery_image_path = getPath(fileUri);
                // video successfully recorded
                // launching upload activity
                launchUploadActivity(true, requestCode);
            } else if (resultCode == Activity.RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getActivity().getApplicationContext(),
                        "이미지 불러오는것이 취소됬습니다.", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getActivity().getApplicationContext(),
                        "갤러리 로드 실패", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false, requestCode);

            } else if (resultCode == Activity.RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getActivity().getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
        }
        cursor.close();
        return cursor.getString(columnIndex);
    }

    private void launchUploadActivity(boolean isImage, int requestCode){
        Intent i = new Intent(getActivity(), UploadActivity.class);
        String path = "";
        if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            System.out.println("카메라 경로" + fileUri.getPath());
            i.putExtra("filePath", fileUri.getPath());
            i.putExtra("isImage", isImage);
            path = fileUri.getPath();
        } else if(requestCode == PICK_FROM_GALLERY) {
            System.out.println("갤러리 경로" + gallery_image_path);
            i.putExtra("filePath", gallery_image_path);
            i.putExtra("isImage", isImage);
            path = gallery_image_path;
        } else {
            System.out.println(fileUri.getPath() + " " + isImage);
            i.putExtra("filePath", fileUri.getPath());
            i.putExtra("isImage", isImage);
        }
        startActivity(i);
        File imgFile = new  File(path);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myimage.setImageBitmap(myBitmap);
            myimage.setVisibility(View.VISIBLE);
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onStart() {
        super.onStart();
/*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddQuestion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pelkan.tab/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        */
    }

    @Override
    public void onStop() {
        super.onStop();
/*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddQuestion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pelkan.tab/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
        */
    }

    private class JsonReadTask extends AsyncTask<String, Void, String> {                //키워드 불러오기
        @Override
        protected String doInBackground(String... params) {
            HttpParams para = new BasicHttpParams();
            para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpClient httpclient = new DefaultHttpClient(para);
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayList<String> array = new ArrayList<String>();

            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("keyword_list");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String q_id = jsonChildNode.optString("name");
                    array.add(q_id);
                }
            } catch (JSONException e) {
            }
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            keyword = new HashSet<String>();

            for(int i = 0; i < array.size(); i++) {
                keyword.add(array.get(i));
            }
            keyword_search_view = (AutoCompleteTextView)rootView.findViewById(R.id.keyword_list);
            ArrayAdapter<String> keywordAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.keyword_list_style, keyword.toArray(new String[keyword.size()]));


            keyword_search_view.setAdapter(keywordAdapter);
            keyword_search_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //꼭하나만 입력하지 않아도 naver처럼 AutoCompleteTextView가 눌리면 리스트가 먼저 보이게끔 처리
                    keyword_search_view.showDropDown();
                }
            });

            keyword_search_view.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (keyword_search_view.getText().toString().replaceAll("\\p{Space}", "").equals("")) {
                            imm.hideSoftInputFromWindow(keyword_search_view.getWindowToken(), 0);
                        } else {
                            //addSearchName(keyword_search_view.getText().toString());
                        }
                        return true;
                    }
                    return false;
                }
            });
        }

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

            String q_title = temp_title;
            String email = Login.my_id;
            String content = notice_content;
            String img_url = setting.getString("img_url", "");
            String is_img = setting.getString("is_add_img_true", "");
            if (!(is_img.equals("1")))
                return null;
            if(all_keyword.equals(""))
                return null;
            String temp_level = question_level.substring(4);
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
                System.out.println("정보뿌림");
                System.out.println(email);
                System.out.println(q_title);
                System.out.println(img_url);
                System.out.println(content);
                System.out.println(all_keyword);
                System.out.println(temp_level);
                param.add(new BasicNameValuePair("email", email));
                param.add(new BasicNameValuePair("title", q_title));
                param.add(new BasicNameValuePair("img_url", img_url));
                param.add(new BasicNameValuePair("content", content));
                param.add(new BasicNameValuePair("keyword", all_keyword));
                param.add(new BasicNameValuePair("level", temp_level));
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
//            if (add_image == false) {
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "문제에 대한 이미지를 올려주세요", Toast.LENGTH_SHORT)
//                        .show();
//            }
//            else {
                content.setText("");
                title.setText("");
                keyword_list.setText("#선택키워드1 #선택키워드2");
                ke_list.clear();
                add_image = false;

                editor.putString("is_add_img_true", "0");
                Intent intent1 = new Intent(getActivity(), Tab.class);
                startActivity(intent1);
                getActivity().finish();

                //((Tab)getActivity()).getViewPager().setCurrentItem(1);
                //Tab.mViewPager.setCurrentItem(1);
//            }
        }
    }
}