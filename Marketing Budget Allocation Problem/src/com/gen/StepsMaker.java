package com.gen;

import java.util.*;
import java.util.stream.Collectors;

public class StepsMaker {
    private Population population;
    private Random random;
    private final int tournamentIteration;
    private final int parentsNum;
    private final float mutationProbability;
    private final int firstPoint, secondPoint;

    private final int maxNumOfGenerations;
    private final float b;
    public StepsMaker(Population population) {
        this.population = population;
        random = new Random();
        tournamentIteration = population.popSize / 2;
        parentsNum = ((population.popSize / 2) % 2 == 0 ? population.popSize / 2 : population.popSize / 2 - 1);
        firstPoint = getRandomIntNum(0,population.info.numOfChannels/2-1);
        secondPoint = getRandomIntNum(population.info.numOfChannels/2,population.info.numOfChannels-1);
        mutationProbability = (float) 0.2;
        maxNumOfGenerations = 1000;
        b=getRandomFloatNum((float) 0.5,5);
    }

    public void startSteps(int reqMutation) {
        for(int currGeneration=1;currGeneration<=maxNumOfGenerations;currGeneration++){
            List<Chromosome> parents = getSelectionList();
            List<Chromosome> children = crossOver(parents);
            if(reqMutation==1)
                doMutationWithUniform(children);
            else
                doMutationWithNonUniform(children,currGeneration);
            doReplacementWithElitistStrategy(children);
        }
    }

    private void doReplacementWithElitistStrategy(List<Chromosome> children) {
        population.pop.addAll(children);
        population.pop = population.pop.stream().sorted(Comparator.comparing(Chromosome::getTotalProfit).reversed()).collect(Collectors.toList());
        for (int i = population.popSize; i < population.pop.size(); i++)
            population.pop.remove(i--);
    }

    private void doMutationWithUniform(List<Chromosome> children) {
        for (Chromosome child : children) {
            float[] genes = child.genes;
            float newBudget = 0, newProfit = 0;

            for (int i = 0; i < population.info.numOfChannels; i++) {
                GeneBounder geneBounder = population.info.bounders.get(i);
                if (random.nextFloat() <= mutationProbability) {
                    float newGene;
                    if (random.nextFloat() <= 0.5) {
                        float deltaL = genes[i] - geneBounder.lower;
                        newGene = genes[i] - getRandomFloatNum(0, deltaL);
                    } else {
                        float deltaU = geneBounder.upper - genes[i];
                        newGene = genes[i] + getRandomFloatNum(0, deltaU);
                    }
                    if ((child.budget - genes[i] + newGene) <= population.info.totalBudget) {
                        if(newGene>=geneBounder.lower&&newGene<=geneBounder.upper){
                            child.budget = child.budget - genes[i] + newGene;
                            genes[i] = newGene;
                        }

                    }
                }
                newBudget += genes[i];
                newProfit += genes[i] * (population.info.channelsROI[i] / 100);
            }


            child.budget = newBudget;
            child.totalProfit = newProfit;
        }
    }

    private void doMutationWithNonUniform(List<Chromosome> children, int currGeneration) {
        for (Chromosome child : children) {
            float[] genes = child.genes;
            float newBudget = 0, newProfit = 0;

            for (int i = 0; i < population.info.numOfChannels; i++) {
                GeneBounder geneBounder = population.info.bounders.get(i);
                if (random.nextFloat() <= mutationProbability) {
                    float newGene;
                    float deltaL = genes[i] - geneBounder.lower;
                    //newGene = genes[i] - getRandomFloatNum(0, deltaL);

                    float deltaU = geneBounder.upper - genes[i];
                    //newGene = genes[i] + getRandomFloatNum(0, deltaU);

                    if (random.nextFloat() <=0.5){
                        float y=deltaL;
                        //float deltaTY= (float) (y*(1-random.nextFloat())*Math.pow((1-currGeneration)/(double)maxNumOfGenerations,b));
                        float deltaTY= (float) (y*(1-Math.pow(random.nextFloat(),Math.pow(1-currGeneration/(float)maxNumOfGenerations,b))));

                        newGene = genes[i] - deltaTY;
                    }else{
                        float y=deltaU;
                        //float deltaTY= (float) (y*(1-random.nextFloat())*Math.pow((1-currGeneration)/(double)maxNumOfGenerations,b));
                        float deltaTY= (float) (y*(1-Math.pow(random.nextFloat(),Math.pow(1-currGeneration/(float)maxNumOfGenerations,b))));

                        newGene = genes[i] + deltaTY;
                    }
                        if ((child.budget - genes[i] + newGene) <= population.info.totalBudget) {
                            if(newGene>=geneBounder.lower&&newGene<=geneBounder.upper){
                                child.budget = child.budget - genes[i] + newGene;
                                genes[i] = newGene;
                            }

                        }
                }
                newBudget += genes[i];
                newProfit += genes[i] * (population.info.channelsROI[i] / 100);
            }


            child.budget = newBudget;
            child.totalProfit = newProfit;
        }
    }


    private Chromosome getParentFromTournament() {
        List<Chromosome> parents = new ArrayList<>();
        for (int i = 0; i < tournamentIteration; i++) {
            parents.add(population.pop.get(getRandomIntNum(0, population.popSize - 1)));
        }
        parents = parents.stream().sorted(Comparator.comparing(Chromosome::getTotalProfit).reversed()).collect(Collectors.toList());
        return parents.get(0);
    }


    private List<Chromosome> getSelectionList() {
        List<Chromosome> parents = new ArrayList<>();
        for (int i = 0; i < parentsNum; i++) {
            parents.add(getParentFromTournament());
        }
        return parents;
    }

    private List<Chromosome> crossOver(List<Chromosome> parents) {
        List<Chromosome> children = new ArrayList<>();
        int firstParent, secondParent;
        for (int i = 0; i < parentsNum; i++) {
            if (random.nextFloat() <= 0.7) {

                do {
                    firstParent = getRandomIntNum(0, parentsNum - 1);
                    secondParent = getRandomIntNum(0, parentsNum - 1);
                } while (firstParent == secondParent);
                children.addAll(doCrossOver(parents.get(firstParent), parents.get(secondParent)));
            }
        }

        return children;
    }

    private List<Chromosome> doCrossOver(Chromosome parent1, Chromosome parent2) {
        List<Chromosome> children = new ArrayList<>();
        float[] gene1 = new float[population.info.numOfChannels], gene2 = new float[population.info.numOfChannels];
        float budget1 = 0, budget2 = 0;
        float profit1 = 0, profit2 = 0;
        for (int i = 0; i < firstPoint; i++) {
            gene1[i] = parent1.genes[i];
            budget1 += gene1[i];
            profit1 += gene1[i] * (population.info.channelsROI[i] / 100);

            gene2[i] = parent2.genes[i];
            budget2 += gene2[i];
            profit2 += gene2[i] * (population.info.channelsROI[i] / 100);
        }
        for (int i = firstPoint; i <= secondPoint; i++) {
            gene1[i] = parent2.genes[i];
            budget1 += gene1[i];
            profit1 += gene1[i] * (population.info.channelsROI[i] / 100);

            gene2[i] = parent1.genes[i];
            budget2 += gene2[i];
            profit2 += gene2[i] * (population.info.channelsROI[i] / 100);
        }

        for (int i = secondPoint + 1; i < population.info.numOfChannels; i++) {
            gene1[i] = parent1.genes[i];
            budget1 += gene1[i];
            profit1 += gene1[i] * (population.info.channelsROI[i] / 100);

            gene2[i] = parent2.genes[i];
            budget2 += gene2[i];
            profit2 += gene2[i] * (population.info.channelsROI[i] / 100);
        }
        if (budget1 <= population.info.totalBudget)
            children.add(new Chromosome(gene1, budget1, profit1));
        if (budget2 <= population.info.totalBudget)
            children.add(new Chromosome(gene2, budget2, profit2));
        return children;
    }


    private int getRandomIntNum(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private float getRandomFloatNum(float min, float max) {
        return (float) (Math.random() * (max - min + 1) + min);
    }
}
