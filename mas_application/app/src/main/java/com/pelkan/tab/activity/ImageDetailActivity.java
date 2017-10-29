package com.pelkan.tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageDetailActivity extends Activity {
    ImageView question_img;
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        question_img = (ImageView) findViewById(R.id.q_image);

        Intent intent = getIntent();
        String img_url = intent.getStringExtra("img_url");
        System.out.println("이미지는 " + img_url);
        DetailImageLoader imgLoader = new DetailImageLoader(getApplicationContext());
        imgLoader.DisplayImage(img_url, question_img);

        mAttacher = new PhotoViewAttacher(question_img);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
}
