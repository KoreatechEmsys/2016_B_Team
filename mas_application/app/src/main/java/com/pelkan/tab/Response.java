package com.pelkan.tab;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Response extends ActionBarActivity {
    private TextView re_nick;
    private EditText re_title;
    private ImageButton re_galleryBtn;
    private ImageButton re_cameraBtn;
    private EditText re_content;
    private Button reBtn;
    private Spinner spinner;

    private ArrayAdapter sAdapter;

    private String qid;
    private String title;
    private String content;
    private String gallery_image_path;
    private String url_create_product = "http://218.150.182.58:2041/mas/add_response.php";

    SharedPreferences setting;
    SharedPreferences.Editor editor;
    com.pelkan.tab.JSONParser jsonParser = new com.pelkan.tab.JSONParser();

    private static final String TAG = AddQuestion.class.getSimpleName();

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PICK_FROM_GALLERY = 300;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video
    private ProgressDialog pDialog;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        Intent intent = getIntent();
        qid = intent.getExtras().getString("qid");
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        re_title = (EditText)findViewById(R.id.response_title);
        re_galleryBtn = (ImageButton)findViewById(R.id.response_galleryBtn);
        re_cameraBtn = (ImageButton)findViewById(R.id.response_camera);
        re_content = (EditText)findViewById(R.id.response_content);
        reBtn = (Button)findViewById(R.id.response_button);
        re_nick = (TextView)findViewById(R.id.nick_name);
        re_nick.setText(setting.getString("ID", ""));
       // spinner = (Spinner) findViewById(R.id.txt_question_type);
        //sAdapter = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);

        //spinner.setAdapter(sAdapter);

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "현재 사용하시고 있는 기기는 카메라를 지원하지 않습니다!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        reBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = re_title.getText().toString();
                content = re_content.getText().toString();
                new CreateNewContent().execute();
                editor.remove("temp_title");
                editor.remove("temp_content");
                editor.commit();
            }
        });

        re_cameraBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //카메라 버튼 입력시 미리 입력해뒀던 내용 저장
                title = re_title.getText().toString();
                content = re_content.getText().toString();
                editor.putString("temp_re_title", title);
                editor.putString("temp_re_content", content);
                editor.commit();
                captureImage();
            }
        });

        re_galleryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //카메라 버튼 입력시 미리 입력해뒀던 내용 저장
                title = re_title.getText().toString();
                content = re_content.getText().toString();
                editor.putString("temp_re_title", title);
                editor.putString("temp_re_content", content);
                editor.commit();
                takeImage();
            }
        });
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
            re_title.setText(setting.getString("temp_re_title", ""));
            re_content.setText(setting.getString("temp_re_content", ""));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {

            return false;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                launchUploadActivity(true, requestCode);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();
                gallery_image_path = getPath(fileUri);
                launchUploadActivity(true, requestCode);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "이미지 불러오는것이 취소됬습니다.", Toast.LENGTH_SHORT)
                        .show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "갤러리 로드 실패", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                launchUploadActivity(false, requestCode);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
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
        Intent i = new Intent(Response.this, UploadActivity.class);
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

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
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

    class CreateNewContent extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Response.this);
            pDialog.setMessage("로딩 중...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            // Building Parameters
            String json;
            String email = setting.getString("ID", "");
            String insert_content = content;
            String insert_title = title;
            String img_url = setting.getString("img_url", "");

            if(setting.getBoolean("add_image", false) == false)
                img_url = null;

            editor.putBoolean("add_image", false);
            editor.commit();

            try {
                HttpParams para = new BasicHttpParams();
                para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                DefaultHttpClient httpClient = new DefaultHttpClient(para);
                HttpPost httppost = new HttpPost(url_create_product);

                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("qid", qid));
                param.add(new BasicNameValuePair("email", email));
                param.add(new BasicNameValuePair("title", insert_title));
                param.add(new BasicNameValuePair("content", insert_content));
                System.out.println("qid는 " + qid);
                if(img_url != null) {
                    param.add(new BasicNameValuePair("img_url", img_url));
                }
                else {
                    param.add(new BasicNameValuePair("img_url", "temp"));
                }

                httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
                HttpResponse httpResponse = httpClient.execute(httppost);

                json = inputStreamToString(httpResponse.getEntity().getContent()).toString();
                System.out.println("리턴값은 " + json + "임");
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(getApplicationContext(), Tab.class);
            startActivity(i);

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

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }
}
