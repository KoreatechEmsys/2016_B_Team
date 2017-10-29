package com.pelkan.tab;

    import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

    /**
     * Created by JangLab on 2016-07-05.
     */
    public class SectionsFragment3 extends Fragment {        //마이페이지
        public static ArrayList<String> favorite_list = new ArrayList<>();
        public SectionsFragment3() {

        }
        private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
        private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
        private static final int PICK_FROM_GALLERY = 300;

        public static final int MEDIA_TYPE_IMAGE = 1;
        public static final int MEDIA_TYPE_VIDEO = 2;
        private DialogNewMindmap newMindmapDialog;
        private Uri fileUri; // file url to store image/video
        private String jsonResult;
        private ImageView my_question;
        private ImageView profile;
        private ImageButton logout;
        private ImageButton config;
        private ImageButton my_response;
        private ImageButton my_power;
        private ImageButton friend;
        String gallery_image_path;
        SharedPreferences setting;
        SharedPreferences.Editor editor;


    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment3 newInstance(int SectionNumber){
        SectionsFragment3 fragment = new SectionsFragment3();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_page3,
                container, false);
        my_question = (ImageView)rootView.findViewById(R.id.my_question);
        logout = (ImageButton)rootView.findViewById(R.id.logout);
        my_response = (ImageButton)rootView.findViewById(R.id.my_response);
        profile = (ImageView) rootView.findViewById(R.id.profile);
        config = (ImageButton) rootView.findViewById(R.id.config);
        my_power = (ImageButton) rootView.findViewById(R.id.my_power);
        friend = (ImageButton) rootView.findViewById(R.id.friend);
        setting = getActivity().getSharedPreferences("setting", 0);
        editor= setting.edit();

        System.out.println("3번째 시작");
        System.out.println(setting.getBoolean("is_upload", false));
        if (setting.getBoolean("is_upload", false) != false) {
            //fragment 전환하기
            editor.putBoolean("is_upload", false);
            editor.commit();
            System.out.println("이동완료");
            //((Tab)getActivity()).getViewPager().setCurrentItem(2);
            System.out.println("이동완료");
            //Tab.mViewPager.setCurrentItem(2);
        }

        my_power.setOnClickListener(new View.OnClickListener() {            //alert로 카메라 갤러리 로드해서 프로필 이미지 업로드 나중에 하자

            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                FirstFragment temp_fragment = new FirstFragment();
                trans.replace(R.id.root_frame1, temp_fragment);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
//                for(int i = 0; i < Start.maps.size(); i++) {
//                    System.out.println("이름은 " + Start.maps.get(i).getName());
//                    for(int j = 0; j < Start.maps.get(i).getParentNode().size(); j++) {
//                        System.out.println("부모 노드는 " + Start.maps.get(i).getParentNode().get(j));
//                    }
//                }
            }
        });

        friend.setOnClickListener(new View.OnClickListener() {            //alert로 카메라 갤러리 로드해서 프로필 이미지 업로드 나중에 하자

            @Override
            public void onClick(View view) {
                newMindmapDialog = new DialogNewMindmap();
                newMindmapDialog.show(getFragmentManager(), "dialog !!");
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {            //alert로 카메라 갤러리 로드해서 프로필 이미지 업로드 나중에 하자

            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"갤러리", "카메라"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());     // 여기서 this는 Activity의 this

                builder.setTitle("이미지를 불러옵니다")        // 제목 설정
                        .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                            public void onClick(DialogInterface dialog, int index){
                                if(index == 0) {        //갤러리
                                    takeImage();
                                } else if(index == 1) {                //카메라
                                    captureImage();
                                }
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
            }
        });

        my_question.setOnClickListener(new View.OnClickListener() {            //alert로 카메라 갤러리 로드해서 프로필 이미지 업로드 나중에 하자

            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment8 temp_fragment = new SectionsFragment8();
                trans.replace(R.id.root_frame1, temp_fragment);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });

        my_response.setOnClickListener(new View.OnClickListener() {            //alert로 카메라 갤러리 로드해서 프로필 이미지 업로드 나중에 하자

            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment9 temp_fragment = new SectionsFragment9();
                trans.replace(R.id.root_frame1, temp_fragment);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {        //로그아웃

            @Override
            public void onClick(View view) {
                editor.putString("ID", "");
                editor.putString("PW", "");
                editor.putBoolean("Auto_Login_enabled", false);
                editor.commit();
                Intent intent1 = new Intent(getActivity(), Start.class);
                startActivity(intent1);
                getActivity().finish();
            }
        });

        config.setOnClickListener(new View.OnClickListener() {          //설정

            @Override
            public void onClick(View view) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment7 temp = new SectionsFragment7();
                trans.replace(R.id.root_frame1, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });

        System.out.println("3번째 끝");
        return rootView;
    }

        private Bitmap getCircleBitmap(Bitmap bitmap) {
            final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);

            final int color = Color.RED;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            bitmap.recycle();

            return output;
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

    private void launchUploadActivity(boolean isImage, int requestCode){
        Intent i = new Intent(getActivity(), ProfileUploadActivity.class);
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

}