package com.ddonging.debugtools.utils;


import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class lineChartHandle {
    LineChart lineChart;
    YAxis mLeftYAxis; //左侧Y轴
    YAxis mRightYAxis; //右侧Y轴
    List<Float> yValues = new ArrayList<>();
    List<Float> xValues = new ArrayList<>();
    List<Float> zValues = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    public void initLineChart(LineChart lineChart)
    {
        this.lineChart=lineChart;
        //无数据时显示
        lineChart.setNoDataText("暂时没有数据");
        lineChart.setNoDataTextColor(Color.parseColor("#2699FB"));
        //取消缩放
        lineChart.setScaleEnabled(false);
        //不显示高亮
        lineChart.setHighlightPerTapEnabled(false);
        //不显示description
        lineChart.getDescription().setEnabled(false);
        //不显示边界
        lineChart.setDrawBorders(false);
        //显示图例
        lineChart.getLegend().setEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setExtraBottomOffset(10f);
        //获取X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(true);
        //设置网格线为虚线效果
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        lineChart.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8f);
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setEnabled(true);
        leftYAxis.setLabelCount(10,true);
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(true);
        rightYAxis.setLabelCount(10,true);
        mLeftYAxis = lineChart.getAxisLeft(); // 得到侧Y轴
      //  mLeftYAxis.setLabelCount(10,false); // 设置X轴的刻度数量，第二个参数表示是否平均分配
         lineChart.setGridBackgroundColor(Color.parseColor("#ffffff"));
//        lineChart.setBorderColor(Color.parseColor("#3092D1"));
        lineChart.setDrawGridBackground(true); //是否展示网格线
        lineChart.setDrawBorders(false); //是否显示边界
        lineChart.setDragEnabled(true); //是否可以拖动
        lineChart.setScaleEnabled(true); // 是否可以缩放
        lineChart.setTouchEnabled(true); //是否有触摸事件
        //初始化显示数
//        description.setText("Temperature");//描述文字
//        //description.setPosition(50f,50f);//文字位置，默认图表右下角
//        description.setTextColor(Color.BLUE);//描述文字字体颜色
//        lineChart.setDescription(description);//设置图表描述
        lineChart.setScaleYEnabled(true);
        lineChart.animateY(2500);
        lineChart.animateX(10);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴设置显示位置在底部
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMinimum(0f); // 设置X轴的最小值
        xAxis.setAxisMaximum(100000); // 设置X轴的最大值
        xAxis.setLabelCount(100,false); // 设置X轴的刻度数量，第二个参数表示是否平均分配
        xAxis.setGranularity(1f); // 设置X轴坐标之间的最小间隔
        int xNum = 100;
        lineChart.setVisibleXRangeMaximum(xNum);// 当前统计图表中最多在x轴坐标线上显示的总量
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0);
//        leftYAxis.setAxisMaximum(50);
        leftYAxis.setEnabled(true);
//        leftYAxis.setLabelCount(100);
        rightYAxis.setEnabled(true);
        rightYAxis.setAxisMinimum(0);
    }
    public void addData(String name,String color,LineChart lineChart, List<Float> value)
    {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0;i<value.size();i++){
            entries.add(new Entry(i,value.get(i)));
        }
        dataSets.add(set(entries,name,color));


    }
    public LineDataSet set(List<Entry> entries,String name,String color)
    {
        //将数据赋给数据集,一个数据集表示一条线
        LineDataSet lineDataSet = new LineDataSet(entries,name);
        //线颜色
        lineDataSet.setColor(Color.parseColor(color));
        //线宽度
        lineDataSet.setLineWidth(1f);
        //显示圆点
        lineDataSet.setDrawCircles(false);
        //设置圆点颜色(外圈)
//        lineDataSet.setCircleRadius(0.1f);
//        lineDataSet.setCircleColor(Color.parseColor("#008CFF"));
        //设置圆点填充颜色
//         lineDataSet.setCircleHoleColor(Color.parseColor("#008CFF"));
        //设置线条为平滑曲线
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        //设置直线图填充
//        lineDataSet.setDrawFilled(true);
//        //设置填充颜色
//        lineDataSet.setFillColor(Color.parseColor("#FFA2A2"));
        return lineDataSet;
    }

    /**
     * 功能：第1条折线添加一个点
     */
    public void addLine1Data(Float xValue)     {
        //0.5HZ低通滤波
        xValues.add(xValue);
        addData("时间","#F60D28",lineChart,xValues);
        LineData lineData = new LineData(dataSets);
        //不显示曲线点的具体数值
        lineData.setDrawValues(false);
        lineChart.moveViewToX(xValues.size()-100);
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

}