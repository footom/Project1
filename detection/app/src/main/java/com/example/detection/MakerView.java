package com.example.detection;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.List;

public class MakerView extends MarkerView {

    private TextView tv1, tv2,  tv4;
    private IAxisValueFormatter xAxisValueFormatter;
    private Chart chart;

    public MakerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.makerview);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv4 = findViewById(R.id.tv4);

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        chart = getChartView();
        if(chart instanceof LineChart){
            LineData lineData = ((LineChart) chart).getLineData();
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            for(int i=0; i<dataSetList.size(); i++) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                float y = dataSet.getValues().get((int) e.getX()).getY();
                if(i==0){
                    tv1.setText(dataSet.getLabel() + ": " + y + "ppm");
                }
                if(i==1){
                    tv2.setText(dataSet.getLabel() + ": " + y + "ppm");
                }

            }
            tv4.setText("時間:" + xAxisValueFormatter.getFormattedValue(e.getX(), null));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
