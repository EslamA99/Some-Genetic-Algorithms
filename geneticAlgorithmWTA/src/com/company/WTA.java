package com.company;

import java.util.*;

public class WTA {
    static Scanner in=new Scanner(System.in);
    public static void main(String[] args) {
        ArrayList<Integer>countOfWeapons=new ArrayList<>();
        ArrayList<String>typeOfWeapons=new ArrayList<>();
        int numOfWeapons=0;
        int numOfWeaponsTypes=0;
        int numOfTargets=0;
        int numOfChromosomes=10;
        ArrayList<Integer>targetThreatCoefficient=new ArrayList<>();
        ArrayList<Chromosome>selectionChromosomes=new ArrayList<>();

        System.out.println("Enter the weapon types and the number of instances of each type:\n" + "(Enter “x” when you’re done)");
        String line=in.nextLine();
        while(line.charAt(0)!='x' && line.length()!=1){
            String[] array = line.split(" ");
            typeOfWeapons.add(array[0]);
            countOfWeapons.add(Integer.parseInt(array[1]));
            numOfWeapons+=Integer.parseInt(array[1]);
            numOfWeaponsTypes++;
            line=in.nextLine();
        }
        System.out.println("Enter the number of targets:");
        numOfTargets=in.nextInt();
        System.out.println("Enter the threat coefficient of each target:");
        for (int i=0;i<numOfTargets;i++){
            targetThreatCoefficient.add(in.nextInt());
        }
        float [][] matrix=new float[numOfWeaponsTypes][numOfTargets];
        System.out.println("Enter the weapons’ success probabilities matrix:");
        for(int i=0;i<numOfWeaponsTypes;i++)
            for(int j=0;j<numOfTargets;j++){
                float num=in.nextFloat();
                matrix[i][j]=1-num;
            }

        Population population=new Population(numOfChromosomes,numOfTargets,numOfWeapons,countOfWeapons,
                targetThreatCoefficient,matrix);
        population.initializePopulation();

        double PC=0.7;
        double PM=0.2;
        for (int i=0;i<10;i++){
            population.calcPopulationFit();
            selectionChromosomes=population.selection(numOfChromosomes/2);

            for(int j=0;j<selectionChromosomes.size();j+=2){
                Random random=new Random();
                float prob=random.nextFloat();
                if(prob<=PC)
                    population.crossOver(selectionChromosomes.get(j),selectionChromosomes.get(j+1));
            }
            population.mutation(PM);

            population.Replace();
            population.print();
            System.out.println("-----------------------------------------");
        }

        //population.calcPopulationFit();

        System.out.println("The final WTA solution is:");
        int count=1;
        int index=-1;
        for (int i=0;i<numOfWeapons;i++){
            if(index==Chromosome.getWeaponType(i,countOfWeapons))
                count++;
            else count=1;
            index=Chromosome.getWeaponType(i,countOfWeapons);
            System.out.println(typeOfWeapons.get(index)+" #"+count+" is assigned to target #"+(population.chromosomes.get(0).genes[i]+1)+",");
        }
        System.out.println("The expected total threat of the surviving targets is "+population.chromosomes.get(0).fit);

    }
}
/*
Tank 2
Aircraft 1
Grenade 2
x

0.3 0.6 0.5
0.4 0.5 0.4
0.1 0.2 0.2


    population.print();
            Chromosome ch=new Chromosome(numOfTargets,numOfWeapons);
        ch.genes[0]=0;
        ch.genes[1]=2;
        ch.genes[2]=0;
        ch.genes[3]=1;
        ch.genes[4]=1;
        ch.calcFit(matrix,targetThreatCoefficient,countOfWeapons);
        System.out.println();
        System.out.println("  "+ch.fit);

 */