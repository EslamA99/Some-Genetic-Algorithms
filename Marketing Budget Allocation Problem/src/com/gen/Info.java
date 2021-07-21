package com.gen;

import java.util.ArrayList;

public class Info {
    float totalBudget;
    int numOfChannels;
    String [] channelName;
    float [] channelsROI;
    ArrayList<GeneBounder> bounders;

    public Info(float totalBudget, int numOfChannels) {
        this.totalBudget = totalBudget;
        this.numOfChannels = numOfChannels;
        channelName=new String[numOfChannels];
        channelsROI=new float[numOfChannels];
        bounders=new ArrayList<>();
    }
}
