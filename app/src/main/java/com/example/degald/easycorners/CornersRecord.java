package com.example.degald.easycorners;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class CornersRecord extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corners_screenshot);

        imageView = findViewById(R.id.imageView);

        String pathToFile = getIntent().getStringExtra(FullscreenActivity.PATH_TO_FILE);
        File file = new File(pathToFile);
        Uri uri = Uri.fromFile(file);
        imageView.setImageURI(uri);
    }
}
