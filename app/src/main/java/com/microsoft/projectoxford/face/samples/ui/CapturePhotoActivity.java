package com.microsoft.projectoxford.face.samples.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.microsoft.projectoxford.face.samples.R;

/**
 * Created by Dipeanra CFC on 3/23/2017.
 */

public class CapturePhotoActivity extends AppCompatActivity {

   Button mCaptureButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturephoto);

        mCaptureButton =  (Button) findViewById(R.id.activity_capturephoto_capture_button);
    }



}
