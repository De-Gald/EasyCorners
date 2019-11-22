package com.example.degald.easycorners;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.degald.easycorners.utilities.GraphUtils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class FullscreenActivity extends AppCompatActivity {
    public static final String PATH_TO_FILE = "pathToFile";

    Button mButton;

    private GraphView mGraph;
    private GraphView mGraph2;
    private GraphView mGraph3;
    private GraphView mGraph4;

    private float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 250;


    @Override
    protected void onResume() {
        super.onResume();
        mGraph.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mGraph2.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mGraph3.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mGraph4.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        mGraph = (GraphView) findViewById(R.id.graph);
        mGraph2 = (GraphView) findViewById(R.id.graph2);
        mGraph3 = (GraphView) findViewById(R.id.graph3);
        mGraph4 = (GraphView) findViewById(R.id.graph4);

        int[] corners = getIntent().getIntArrayExtra(MainActivity.OUTGOING_CORNERS);
        String title = getIntent().getStringExtra(MainActivity.TITLE_ID);
        int indicator = getIntent().getIntExtra(MainActivity.INDICATOR_ID, -1);

        DataPoint[] list = new DataPoint[corners.length];
        double max = 0;
        for (int i = 0; i < corners.length; ++i) {
            if (corners[i] > max)
                max = corners[i];
            list[i] = new DataPoint(i, corners[corners.length - 1 - i]);
        }


        DataPoint[] list2 = new DataPoint[corners.length - 1];
        double max2 = 0;
        for (int i = 0; i < corners.length - 1; ++i) {
            list2[i] = new DataPoint(i, Math.abs((Integer) corners[corners.length - 2 - i] - (Integer) corners[corners.length - 1 - i]));
            if (list2[i].getY() > max2)
                max2 = list2[i].getY();
        }


        DataPoint[] list3 = new DataPoint[corners.length - 2];
        double max3 = 0;
        for (int i = 0; i < corners.length - 2; ++i) {
            list3[i] = new DataPoint(i, Math.abs(Math.abs((Integer) corners[corners.length - 3 - i] - (Integer) corners[corners.length - 2 - i]) - Math.abs((Integer) corners[corners.length - 2 - i] - (Integer) corners[corners.length - 1 - i])));
            if (list3[i].getY() > max3)
                max3 = list3[i].getY();
        }


        DataPoint[] list4 = new DataPoint[corners.length - 3];
        double max4 = 0;
        for (int i = 0; i < corners.length - 3; ++i) {
            list4[i] = new DataPoint(i, Math.abs(Math.abs(Math.abs((Integer) corners[corners.length - 4 - i] - (Integer) corners[corners.length - 3 - i]) - Math.abs((Integer) corners[corners.length - 3 - i] - (Integer) corners[corners.length - 2 - i])) - Math.abs(Math.abs((Integer) corners[corners.length - 3 - i] - (Integer) corners[corners.length - 2 - i]) - Math.abs((Integer) corners[corners.length - 2 - i] - (Integer) corners[corners.length - 1 - i]))));
            if (list3[i].getY() > max4)
                max4 = list3[i].getY();
        }

        if (indicator == 1) {
            GraphUtils.buildGraph(mGraph, list, max, corners, "Out. corners, " + title);
            GraphUtils.buildGraph(mGraph2, list2, max2, corners, "Corner difference, " + title);
            GraphUtils.buildGraph(mGraph3, list3, max3, corners, "Double corner difference, " + title);
            GraphUtils.buildGraph(mGraph4, list4, max4, corners, "Triple corner difference, " + title);
        } else {
            GraphUtils.buildGraph(mGraph, list, max, corners, "In. corners, " + title);
            GraphUtils.buildGraph(mGraph2, list2, max2, corners, "Corner difference, " + title);
            GraphUtils.buildGraph(mGraph3, list3, max3, corners, "Double corner difference, " + title);
            GraphUtils.buildGraph(mGraph4, list4, max4, corners, "Triple corner difference, " + title);
        }

        setListeners();

    }


    private void setListeners() {
        mGraph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        y1 = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        y2 = motionEvent.getY();
                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;
                        if (deltaX > MIN_DISTANCE) {
                            mGraph.setVisibility(View.GONE);
                            mGraph2.setVisibility(View.VISIBLE);
                            mGraph3.setVisibility(View.GONE);
                            mGraph4.setVisibility(View.GONE);
                        } else if (deltaX < -MIN_DISTANCE) {
                            mGraph.setVisibility(View.GONE);
                            mGraph2.setVisibility(View.GONE);
                            mGraph3.setVisibility(View.GONE);
                            mGraph4.setVisibility(View.VISIBLE);
                        } else if (deltaY > MIN_DISTANCE) {
                            toggle();
                        } else if (deltaY < -1 * MIN_DISTANCE) {
                            saveToDatabase(mGraph.getTitle());
                        }
                        break;
                }
                return false;
            }
        });


        mGraph2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        y1 = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        y2 = motionEvent.getY();
                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;
                        if (deltaX > MIN_DISTANCE) {
                            mGraph.setVisibility(View.GONE);
                            mGraph2.setVisibility(View.GONE);
                            mGraph3.setVisibility(View.VISIBLE);
                            mGraph4.setVisibility(View.GONE);
                        } else if (deltaX < -MIN_DISTANCE) {
                            mGraph.setVisibility(View.VISIBLE);
                            mGraph2.setVisibility(View.GONE);
                            mGraph3.setVisibility(View.GONE);
                            mGraph4.setVisibility(View.GONE);
                        } else if (deltaY > MIN_DISTANCE) {
                            toggle();
                        } else if (deltaY < -1 * MIN_DISTANCE) {
                            saveToDatabase(mGraph2.getTitle());
                        }
                        break;
                }
                return false;
            }
        });


        mGraph3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        y1 = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        y2 = motionEvent.getY();
                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;
                        if (deltaX > MIN_DISTANCE) {
                            mGraph.setVisibility(View.GONE);
                            mGraph2.setVisibility(View.GONE);
                            mGraph3.setVisibility(View.GONE);
                            mGraph4.setVisibility(View.VISIBLE);
                        } else if (deltaX < -MIN_DISTANCE) {
                            mGraph.setVisibility(View.GONE);
                            mGraph2.setVisibility(View.VISIBLE);
                            mGraph3.setVisibility(View.GONE);
                            mGraph4.setVisibility(View.GONE);
                        } else if (deltaY > MIN_DISTANCE) {
                            toggle();
                        } else if (deltaY < -1 * MIN_DISTANCE) {
                            saveToDatabase(mGraph3.getTitle());
                        }
                        break;
                }
                return false;
            }
        });


        mGraph4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        y1 = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        y2 = motionEvent.getY();
                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;
                        if (deltaX > MIN_DISTANCE) {
                            mGraph.setVisibility(View.VISIBLE);
                            mGraph2.setVisibility(View.GONE);
                            mGraph3.setVisibility(View.GONE);
                            mGraph4.setVisibility(View.GONE);
                        } else if (deltaX < -MIN_DISTANCE) {
                            mGraph.setVisibility(View.GONE);
                            mGraph2.setVisibility(View.GONE);
                            mGraph3.setVisibility(View.VISIBLE);
                            mGraph4.setVisibility(View.GONE);
                        } else if (deltaY > MIN_DISTANCE) {
                            toggle();
                        } else if (deltaY < -1 * MIN_DISTANCE) {
                            saveToDatabase(mGraph4.getTitle());
                        }
                        break;
                }
                return false;
            }
        });
    }


    protected void toggle() {
        takeScreenshot(new View(this));
        share(new View(this));
    }

    protected void saveToDatabase(String title) {
        takeScreenshot(new View(this));
        addToDatabase(title);
    }


    private String sharePath;


    public void takeScreenshot(View view) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            String filePath = imageFile.getPath();
            sharePath = filePath;

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public void share(View view) {
        File file = new File(sharePath);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(intent);

    }

    public void addToDatabase(String title) {
        Intent intent = new Intent(FullscreenActivity.this, CornersScreenshots.class);
        intent.putExtra(PATH_TO_FILE, sharePath);
        intent.putExtra(MainActivity.TITLE_ID, title);
        startActivity(intent);

    }
}
