package com.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
    List<Chromosome> pop;
    Info info;
    int popSize;
    Population(Info info){
        this.info=info;
        popSize=10;
        //popSize=(int)(Math.random()*(100-10+1)+10);
        pop=new ArrayList<>();
        generateInitialPop();
        /*for (float[]chrom:
             pop) {
            for(int i=0;i<chrom.length;i++){
                System.out.print(chrom[i]+" ");
            }
            System.out.println();
        }*/
    }

    private void generateInitialPop() {
        float budget,profit;
        for (int i=0;i<popSize;i++){
            float []chromosome=new float[info.numOfChannels];
            do {
                budget = 0;
                profit=0;
                for (int currGene = 0; currGene < info.numOfChannels; currGene++) {
                    GeneBounder bounder= info.bounders.get(currGene);
                    chromosome[currGene]=(float) (Math.random()*(bounder.upper-bounder.lower+1)+bounder.lower);
                    budget+=chromosome[currGene];
                    profit+=chromosome[currGene]*(info.channelsROI[currGene]/100);

                }
            } while (budget > info.totalBudget);
            /*for(int k=0;k<info.numOfChannels;k++)
                System.out.print(chromosome[k]+" ");
            System.out.println();
            System.out.println(":::"+profit);*/
            pop.add(new Chromosome(chromosome,budget,profit));
        }
    }
}
