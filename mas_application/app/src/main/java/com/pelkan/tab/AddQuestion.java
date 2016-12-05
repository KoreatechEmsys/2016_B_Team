package com.pelkan.tab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;

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
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddQuestion extends ActionBarActivity {
    // LogCat tag
    private static final String TAG = AddQuestion.class.getSimpleName();

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PICK_FROM_GALLERY = 300;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    private Button btnCapturePicture, btnRecordVideo;

    private ProgressDialog pDialog;

    private ArrayList<String> ke_list = new ArrayList<String>();
    private Set<String> keyword;
    private AutoCompleteTextView keyword_search_view;
    InputMethodManager imm;
    com.pelkan.tab.JSONParser jsonParser = new com.pelkan.tab.JSONParser();
    EditText content;
    TextView nickName;
    TextView keyword_list;
    Spinner spinner;
    String notice_content;
    String question_level;
    Button add_keyword;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String gallery_image_path;
    String jsonResult;
    String all_keyword = "";
    String add_keyword_list = "";
    RadioGroup group1;
    RadioGroup group2;

    // url to create new product
    private String url_create_product = "http://218.150.182.58:2041/mas/add_content.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        content = (EditText) findViewById(R.id.add_content);
        nickName = (TextView) findViewById(R.id.nick_name);
        add_keyword = (Button) findViewById(R.id.add_keyword);
        keyword_list = (TextView) findViewById(R.id.my_keyword_list);
        nickName.setText(setting.getString("ID", ""));
       // spinner = (Spinner) findViewById(R.id.txt_question_type);
        //sAdapter = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);

        //spinner.setAdapter(sAdapter);

        // Create button
        group1 = (RadioGroup) findViewById(R.id.radio_group1);
        group2 = (RadioGroup) findViewById(R.id.radio_group2);
        Button btnCreateProduct = (Button) findViewById(R.id.add_button);
        ImageButton cameraBtn = (ImageButton) findViewById(R.id.camera);
        ImageButton galleryBtn = (ImageButton) findViewById(R.id.galleryBtn);
        //ImageButton albumBtn = (ImageButton) findViewById(R.id.upload);

        content.setText(setting.getString("temp_content", ""));
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{"http://218.150.182.45/mas/keyword_list.php"});

        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {            //문제의 레벨을 라디오버튼으로 구현 question_level 변수에 난이도 저장
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.level1:
                        question_level = "1";
                        group2.clearCheck();
                    case R.id.level2:
                        question_level = "2";
                        group2.clearCheck();
                    case R.id.level3:
                        question_level = "3";
                        group2.clearCheck();
                    case R.id.level4:
                        question_level = "4";
                        group2.clearCheck();
                    case R.id.level5:
                        question_level = "5";
                        group2.clearCheck();
                }
            }
        });
        group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.level6:
                        question_level = "6";
                        group1.clearCheck();
                    case R.id.level7:
                        question_level = "7";
                        group1.clearCheck();
                    case R.id.level8:
                        question_level = "8";
                        group1.clearCheck();
                    case R.id.level9:
                        question_level = "9";
                        group1.clearCheck();
                    case R.id.level10:
                        question_level = "10";
                        group1.clearCheck();
                }
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //카메라 버튼 입력시 미리 입력해뒀던 내용 저장
                notice_content = content.getText().toString();
                editor.putString("temp_content", notice_content);
                editor.commit();
                captureImage();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //카메라 버튼 입력시 미리 입력해뒀던 내용 저장
                notice_content = content.getText().toString();
                editor.putString("temp_content", notice_content);
                editor.commit();
                takeImage();
            }
        });

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "현재 사용하시고 있는 기기는 카메라를 지원하지 않습니다!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                notice_content = content.getText().toString();
                for (int i = 0; i < ke_list.size() - 1; i++) {
                    all_keyword += ke_list.get(i) + ",";
                }
                all_keyword += ke_list.get(ke_list.size() - 1);
                new CreateNewContent().execute();
                editor.remove("temp_title");
                editor.remove("temp_content");
                editor.commit();
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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void takeImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요"), PICK_FROM_GALLERY);

    }
    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            content.setText(setting.getString("temp_content", ""));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true, requestCode);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();
                gallery_image_path = getPath(fileUri);
                // video successfully recorded
                // launching upload activity
                launchUploadActivity(true, requestCode);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "이미지 불러오는것이 취소됬습니다.", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "갤러리 로드 실패", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false, requestCode);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    private void launchUploadActivity(boolean isImage, int requestCode){
        Intent i = new Intent(AddQuestion.this, UploadActivity.class);
        if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            i.putExtra("filePath", fileUri.getPath());
            i.putExtra("isImage", isImage);
        } else if(requestCode == PICK_FROM_GALLERY) {
            i.putExtra("filePath", gallery_image_path);
            i.putExtra("isImage", isImage);
        }
        startActivity(i);
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
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
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
    }

    @Override
    public void onStop() {
        super.onStop();

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
    }

    private class JsonReadTask extends AsyncTask<String, Void, String> {
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
                System.out.println("에러 싴발 1");
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
                    String q_id = jsonChildNode.optString("keyword");
                    array.add(q_id);
                }
            } catch (JSONException e) {
                System.out.println("에러 싴발 2");
            }
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            keyword = new HashSet<String>();
            keyword.add("가");
            keyword.add("가나다");
            keyword.add("가나다라마");
            keyword.add("나다라마");
            keyword.add("다라마바사아자");

            for(int i = 0; i < array.size(); i++) {
                keyword.add(array.get(i));
            }
            keyword_search_view = (AutoCompleteTextView)findViewById(R.id.keyword_list);
            ArrayAdapter<String> keywordAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.keyword_list_style, keyword.toArray(new String[keyword.size()]));


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

    class CreateNewContent extends AsyncTask<String, String, String> {              //업로드

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddQuestion.this);
            pDialog.setMessage("로딩 중...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            String email = setting.getString("ID", "");
            String content = notice_content;
            String img_url = setting.getString("img_url", "");

            if (setting.getBoolean("add_image", false) == false)
                img_url = "http://218.150.182.45/mas/img/default.png";

            editor.putBoolean("add_image", false);
            editor.commit();
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("img_url", img_url));
            params.add(new BasicNameValuePair("content", content));
            params.add(new BasicNameValuePair("keyword", all_keyword));
            params.add(new BasicNameValuePair("keyword", question_level));
            // getting JSON Object
            // Note that create product url accepts POST method

            for(int i = 0; i < params.size(); i++)
                System.out.println("데이터는 " + params.get(i));
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
     //       Log.d("Create Response", json.toString());

            // check for success tag

            Intent i = new Intent(getApplicationContext(), Tab.class);
            startActivity(i);
            /*
            try {

                int success = json.getInt(TAG_SUCCESS);
                System.out.print("성공했냐?" + success);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Tab.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                System.out.println("응 에러");
                e.printStackTrace();
            }
*/
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
