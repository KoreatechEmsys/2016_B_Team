package com.pelkan.tab;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class VideoActivity extends Activity implements SurfaceHolder.Callback {
    private Camera cam;
    private MediaRecorder mediaRecorder;
    private Button start;
    private SurfaceView sv;
    private SurfaceHolder sh;
    private String videoUri = "";
    private String temp = "";
    boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);

        start = (Button)findViewById(R.id.button);

        Intent i = getIntent();

        // image or video path that is captured in previous activity
        temp = i.getStringExtra("exam_response");

        String str = Environment.getExternalStorageState();
        if ( str.equals(Environment.MEDIA_MOUNTED)) {

            String dirPath = "/storage/emulated/0/Pictures/MAS";
            File file = new File(dirPath);
            if( !file.exists())  // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
        }

        View.OnClickListener captrureListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    cam.lock();
                    recording = false;
                    System.out.println("촬영 종료");
                    Intent i;
                    if(temp.equals("1")) {
                        i = new Intent(getApplicationContext(), ExamResponseUploadActivity.class);
                    } else {
                        i = new Intent(getApplicationContext(), ResponseUploadActivity.class);
                    }
                    i.putExtra("filePath", videoUri);
                    i.putExtra("isImage", false);
                    startActivity(i);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            start.setText("녹화종료");
                            Toast.makeText(VideoActivity.this, "촬영시작", Toast.LENGTH_LONG).show();
                            try {
                                mediaRecorder = new MediaRecorder();
                                cam.unlock();
                                mediaRecorder.setCamera(cam);
                                CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);

                                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                                mediaRecorder.setVideoEncodingBitRate((profile.videoBitRate));
                                mediaRecorder.setVideoFrameRate(30);
                                mediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);

//                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//                                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//                                mediaRecorder.setAudioEncoder(3);
//                                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
//                                mediaRecorder.setOrientationHint(90);
//                                mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//                                mediaRecorder.setVideoFrameRate(30);
//                                mediaRecorder.setVideoSize(640, 480);

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                                System.out.println("현재 시간 : " + dateFormat.format(calendar.getTime()));
                                videoUri = "/storage/emulated/0/Pictures/MAS/" + dateFormat.format(calendar.getTime()) + ".mp4";
                                mediaRecorder.setOutputFile("/storage/emulated/0/Pictures/MAS/" + dateFormat.format(calendar.getTime()) + ".mp4");
                                mediaRecorder.setPreviewDisplay(sh.getSurface());
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                recording = true;
                                Toast.makeText(VideoActivity.this, "시작", Toast.LENGTH_LONG).show();
                            } catch (final Exception ex) {
                                ex.printStackTrace();
                                mediaRecorder.release();
                                return;

                                // Log.i("---","Exception in thread");
                            }
                        }
                    });
                }
            }
        };

        setting();
        start.setOnClickListener(captrureListener);

//        start.setOnClickListener(captrureListener);
//        setting();
    }

    private void setting(){
        cam = Camera.open();
        cam.setDisplayOrientation(90);
        sv = (SurfaceView)findViewById(R.id.surfaceView);
        sh = sv.getHolder();
        sh.addCallback(this);
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (cam == null) {
                cam.setPreviewDisplay(holder);
                cam.startPreview();
            }
        } catch (IOException e) {
        }
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        refreshCamera(cam);
    }
    public void refreshCamera(Camera camera) {
        if (sh.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            cam.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            cam.setPreviewDisplay(sh);
            cam.startPreview();
        } catch (Exception e) {
        }
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        cam = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        cam.stopPreview();
        cam.release();
        cam = null;
        System.out.println("서페이스 끝");
    }
}
