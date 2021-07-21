package com.company;

import java.util.*;

public class Population {
    int numOfChromosomes=0;
    int numOfTargets=0;
    int numOfWeapons=0;
    ArrayList<Integer>countOfWeapons=new ArrayList<>();
    ArrayList<Integer>targetValues=new ArrayList<>();
    float [][]probability;
    ArrayList<Chromosome>chromosomes=new ArrayList<>();
    Population(int numOfChromosome,int Targets, int weapons, ArrayList<Integer> countOfWeapon, ArrayList<Integer>V, float [][] matrix){
        numOfChromosomes=numOfChromosome;
        numOfTargets=Targets;
        numOfWeapons=weapons;
        countOfWeapons=countOfWeapon;
        targetValues=V;
        probability=new float[numOfWeapons][numOfTargets];
        probability=matrix;
    }
    void initializePopulation(){
        for (int i=0;i<numOfChromosomes;i++){
            Chromosome ch=new Chromosome(numOfTargets,numOfWeapons);
            ch.initialize();
            chromosomes.add(ch);
        }
    }
    void calcPopulationFit(){
        for (int i=0;i<numOfChromosomes;i++){
            chromosomes.get(i).calcFit(probability,targetValues,countOfWeapons);
        }
    }

    public void print() {
        for (int i=0;i<chromosomes.size();i++){
            System.out.println(Arrays.toString(chromosomes.get(i).genes));
            System.out.println(chromosomes.get(i).fit);
            System.out.println();
        }
    }

    public void crossOver(Chromosome chromosome1,Chromosome chromosome2 ) {
        Random random=new Random();
        int crossoverIndex=random.nextInt(numOfWeapons);

        Chromosome ch1=new Chromosome(numOfTargets,numOfWeapons);
        ch1.initialize();
        Chromosome ch2=new Chromosome(numOfTargets,numOfWeapons);
        ch2.initialize();
        for (int i = 0; i < crossoverIndex; i++) {
            ch1.genes[i] = chromosome2.genes[i];
            ch2.genes[i] = chromosome1.genes[i];
        }
        for (int i = crossoverIndex; i < numOfWeapons; i++) {
            ch1.genes[i] = chromosome1.genes[i];
            ch2.genes[i] = chromosome2.genes[i];
        }
        ch1.calcFit(probability,targetValues,countOfWeapons);
        ch2.calcFit(probability,targetValues,countOfWeapons);
        chromosomes.add(ch1);
        chromosomes.add(ch2);
    }

    public void mutation(double PM) {
        for (int i=numOfChromosomes;i<chromosomes.size();i++){
            for (int j=0;j<numOfWeapons;j++){
                Random random=new Random();
                double bitProb=random.nextDouble();
                if(bitProb<PM){
                    int temp=chromosomes.get(i).genes[j];
                    int newValue=random.nextInt(3);
                    while (newValue==temp)
                        newValue=random.nextInt(3);
                    chromosomes.get(i).genes[j]=newValue;
                }
            }
        }
    }

    public void Replace() {
        calcPopulationFit();
        Collections.sort(chromosomes,Comparator.comparing(chromosome -> chromosome.fit));
        chromosomes=new ArrayList<Chromosome>(chromosomes.subList(0,numOfChromosomes));
    }

    public ArrayList<Chromosome> selection(int k) {
        ArrayList<Chromosome>returnChromosomes=new ArrayList<>();
        ArrayList<Chromosome>temp=new ArrayList<>();
        ArrayList<Integer>randomIndexes=new ArrayList<>();
        for (int i=0;i<numOfChromosomes;i++)randomIndexes.add(i);

        for (int i=0;i<10;i++){
            Collections.shuffle(randomIndexes);
            if(i==0)
                returnChromosomes.add(tournamentSelection(randomIndexes.subList(0,k)).get(0));
            else {
                temp = tournamentSelection(randomIndexes.subList(0, k ));
                if ((temp.get(0) == returnChromosomes.get(returnChromosomes.size() - 1)) && (i%2==1))
                    returnChromosomes.add(temp.get(1));
                else
                    returnChromosomes.add(temp.get(0));
            }
        }
        return returnChromosomes;
    }

    private ArrayList<Chromosome> tournamentSelection(List<Integer> indexes){
        ArrayList<Chromosome>ch=new ArrayList<>();
        ArrayList<Chromosome>returns=new ArrayList<>();
        for (int i=0;i<indexes.size();i++){
            ch.add(chromosomes.get(indexes.get(i)));
        }
        Collections.sort(ch,Comparator.comparing(chromosome -> chromosome.fit));
        returns.add(ch.get(0));
        returns.add(ch.get(1));
        return returns;
    }


    /*public void checkValidation() {
        for (int i=numOfChromosomes;i<numOfChromosomes*2;i++){
            chromosomes.get(i).Validation();
        }
    }*/
}
