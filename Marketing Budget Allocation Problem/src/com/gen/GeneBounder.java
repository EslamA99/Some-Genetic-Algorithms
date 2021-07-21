package com.gen;

public class GeneBounder {
    float lower;
    float upper;

    public GeneBounder(float lower, float upper,float totalBudget) {
        this.lower = lower;
        this.upper = totalBudget*(upper/ 100);
    }
}
