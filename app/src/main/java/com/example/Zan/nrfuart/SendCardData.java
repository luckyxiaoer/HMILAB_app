package com.example.Zan.nrfuart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by nodgd on 2017/09/16.
 */

class SendCardData extends BaseCardData {

    private static final String TAG = "SendCardData";

    public double[] cc=new double [4];
    public double dac_high;
    public double dac_low;
    public int cha;
    public int crp;
    private int state = 0;
    private int count = 0;
    private int data[] = new int[0];
    private int channel = 0;
    private List<Integer> messageFlow = null;
    private SendRunner send;
    private Thread sendThread;
//233
    public SendCardData() {
        super();
    }

    public SendCardData(SendCardData cardData) {
        super(cardData);
        data = cardData.getData().clone();
    }

    @Override
    public String getTitle() {
        if (title.equals("")) {
            return "No title" + " (SendCard)";
        } else {
            return title + " (SendCard)";
        }
    }


    public void startSendThread() {
        if (cha==-1)
            (sendThread=new Thread(send=new SendRunner(data, channel, getIdentifier()))).start();
        else
            (sendThread=new Thread(send=new SendRunner(data, channel, getIdentifier(),cc,dac_high,dac_low,crp))).start();
    }


    public void stopSendThread() {
        //TODO 安全的结束
        Intent intent=new Intent(SendRunner.SendRunner_Off);
        intent.putExtra("channel",channel);
        LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(intent);
        Log.i(TAG, "stopSendThread(): TODO");
    }
    //TODO 整理下面这些乱七八糟的函数，没有用的就删了


    public void DataReview() {
        count = count + 1;
        if (count < data.length)
            add(data[count]);
    }

    public void setData(int a[]) {
        data = a.clone();
    }

    public int[] getData() {
        return data;
    }


    public void setChannel(int Channel) {
        channel = Channel;
    }

    public int getChannel() {
        return channel;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInString() {
        return "" + state;
    }

    public void setMessageFlow(int[] msg) {
        messageFlow = new ArrayList<>();
        for (int i = 0; i < msg.length; i++) {
            messageFlow.add(msg[i]);
        }
    }

    public void setMessageFlow(List<Integer> msg) {
        messageFlow = msg;
    }

    public void add(int msgItem) {
        messageFlow.add(msgItem);
    }

    public void add(int[] msg) {
        for (int msgItem : msg) {
            messageFlow.add(msgItem);
        }
    }

    public void add(List<Integer> msg) {
        for (Integer msgItem : msg) {
            messageFlow.add(msgItem);
        }
    }

    public LineChartData getLineChartData() {
        //流数据变成点序列
        List<PointValue> valueList = new ArrayList<>();
        if (messageFlow != null) {
            for (int i = 0; i < messageFlow.size(); i++) {
                valueList.add(new PointValue(i, messageFlow.get(i)));
            }
        }
        //点序列变成线
        Line line = new Line(valueList);
        line.setColor(Color.BLUE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasPoints(false);
        line.setStrokeWidth(1);
        //线变成线序列，但是线序列里面只有一条线
        List<Line> lineList = new ArrayList<>();
        lineList.add(line);

        //x轴
        Axis axisX = new Axis();
        axisX.setTextColor(Color.RED);
        axisX.setTextSize(10);
        axisX.setAutoGenerated(true);

        //y轴
        Axis axisY = new Axis();
        axisY.setTextColor(Color.RED);
        axisY.setTextSize(10);
        axisY.setAutoGenerated(true);

        //新建一个可以传入的数据，填入数据
        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lineList);
        lineChartData.setAxisXBottom(axisX);
        lineChartData.setAxisYLeft(axisY);
        return lineChartData;
    }
}