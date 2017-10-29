package com.pelkan.tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * 시험문제에 대한 풀이를 등록하는 fragment
 * Created by JangLab on 2016-09-17.
 */
public class TestSolveFragment2 extends Fragment {
    private static final String TAG = AddQuestion.class.getSimpleName();

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PICK_FROM_GALLERY = 300;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    private AQuery aq;
    private String gallery_image_path;
    private ArrayList<ExamQuestion> question_array = new ArrayList<ExamQuestion>();

    private ImageView myimage;
    private TextView title;
    private EditText add_content;
    private ImageButton uploadBtn;
    private ImageButton galleryBtn;
    private ImageButton videoBtn;
    private FancyButton add_button;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String url_qustion = "http://218.150.182.58:2041/mas/selected_exam.php";

    public TestSolveFragment2() {
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
        View rootView = inflater.inflate(R.layout.fragment_solve_exam3, container, false);

        myimage = (ImageView) rootView.findViewById(R.id.afterImage);
        add_content = (EditText) rootView.findViewById(R.id.add_content);
        title = (TextView) rootView.findViewById(R.id.title);
        uploadBtn = (ImageButton) rootView.findViewById(R.id.uploadBtn);
        add_button = (FancyButton) rootView.findViewById(R.id.add_button);
        galleryBtn = (ImageButton) rootView.findViewById(R.id.galleryBtn);
        videoBtn = (ImageButton) rootView.findViewById(R.id.videoBtn);


        setting = getActivity().getSharedPreferences("setting", 0);
        editor= setting.edit();

        title.setText("문제 " + (TestSolveFragment1.currentQIndex + 1) + ". " + TestSolveFragment1.question_array.get(TestSolveFragment1.currentQIndex).getTitle());
        galleryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {                                        //이미지 업로드 이벤트
                takeImage();
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {                                        //이미지 업로드 이벤트
                Intent intent1 = new Intent(getActivity(), VideoActivity.class);
                intent1.putExtra("exam_response", "1");
                startActivity(intent1);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {                                        //이미지 업로드 이벤트
                captureImage();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {                      //풀이 제출 이벤트

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                String img_url = setting.getString("exam_response_img_url", "");                 //시험문제 답변에 대한 이미지의 url 이 버튼을 누르면 TestSolveFragment1에 있는 문제에대한 해결 객체 arraylist에 해당 currentsequesce에 정보를 기입하자
                System.out.println((TestSolveFragment1.currentQIndex + 1) + "번쨰 문제의 번호는 " + img_url);

                ExamResponse temp = new ExamResponse();
                temp.setImg_url(img_url);
                temp.setContent(add_content.getText().toString());
                temp.setM_id(Login.my_id);
                temp.setQid(String.valueOf(TestSolveFragment1.currentQIndex + 1));
                temp.setOri_qid(TestSolveFragment1.q_id_array.get(TestSolveFragment1.currentQIndex));
                TestSolveFragment1.response_array.set(TestSolveFragment1.currentQIndex, temp);
                System.out.println("배열1의 크기는 " + TestSolveFragment1.response_array.size());
                getFragmentManager().popBackStack();
                SolveExamActivity.is_solve_array.set(TestSolveFragment1.currentQIndex, 1);
            }
        });

        return rootView;
    }

    public void takeImage() {
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
        String path = "";
        Intent i = new Intent(getActivity(), ExamResponseUploadActivity.class);
        if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            i.putExtra("filePath", fileUri.getPath());
            i.putExtra("isImage", isImage);
            System.out.println("카메라임");
            path = fileUri.getPath();
        } else if(requestCode == PICK_FROM_GALLERY) {
            System.out.println("갤러리 경로" + gallery_image_path);
            i.putExtra("filePath", gallery_image_path);
            i.putExtra("isImage", isImage);
            path = gallery_image_path;
        }
        File imgFile = new  File(path);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myimage.setImageBitmap(myBitmap);
            myimage.setVisibility(View.VISIBLE);
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
}
