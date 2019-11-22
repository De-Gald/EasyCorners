package com.example.degald.easycorners.utilities;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;


public class GraphUtils {

    static double y = 0;

    public static void buildGraph(final GraphView graphView, final DataPoint[] list, double max, final int[] corners, String title) {

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(list);


        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxX(20);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxY(15);
        graphView.getViewport().setMinY(0);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(21);
        graphView.getGridLabelRenderer().setNumVerticalLabels(16);
        graphView.setBackgroundColor(Color.WHITE);


        // styling series
        series.setColor(Color.WHITE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graphView.setTitleTextSize(103);
        graphView.setTitle(title);

        graphView.addSeries(series);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        series.setCustomPaint(paint);

        series.setCustomPaint(paint);


        DataPoint[] listPoints = new DataPoint[(int) max + 1];
        for (int i = 0; i < max + 1; ++i) {
            listPoints[i] = new DataPoint(list.length, i);
        }

        PointsGraphSeries<DataPoint> points = new PointsGraphSeries<DataPoint>(listPoints);
        points.setShape(PointsGraphSeries.Shape.POINT);
        points.setColor(Color.RED);
        points.setSize(1);
        graphView.addSeries(points);


        graphView.setOnLongClickListener(new View.OnLongClickListener() {
            LineGraphSeries<DataPoint> seriesTemprorary;

            @Override
            public boolean onLongClick(View view) {
                DataPoint[] dataPoints = new DataPoint[2];
                dataPoints[0] = new DataPoint(list.length - 1, list[list.length - 1].getY());
                dataPoints[1] = new DataPoint(list.length, y);

                LineGraphSeries<DataPoint> seriesTemprorary = new LineGraphSeries<DataPoint>(dataPoints);
                seriesTemprorary.setColor(Color.RED);
                seriesTemprorary.setThickness(10);

                graphView.addSeries(seriesTemprorary);

                return true;
            }
        });


        points.setOnDataPointTapListener(new OnDataPointTapListener() {

            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                y = dataPoint.getY();

                Toast.makeText(graphView.getContext(), "Point: " + dataPoint + " is selected, now tap and hold on the screen", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
