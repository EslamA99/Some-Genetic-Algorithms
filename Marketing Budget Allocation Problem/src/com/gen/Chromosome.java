package com.gen;

public class Chromosome {
    float[]genes;
    float budget;
    float totalProfit;

    public Chromosome(float[] genes, float budget, float totalProfit) {
        this.genes = genes;
        this.budget = budget;
        this.totalProfit = totalProfit;
    }

    public float[] getGenes() {
        return genes;
    }

    public float getBudget() {
        return budget;
    }

    public float getTotalProfit() {
        return totalProfit;
    }
}
