package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Chromosome {
    double fit=0;
    int [] genes;
    int numOfTargets=0;
    int numOfWeapons=0;

    Chromosome(int Targets,int weapons){
        numOfTargets=Targets;
        numOfWeapons=weapons;
        genes=new int[numOfWeapons];
        for (int i=0;i<numOfWeapons;i++)genes[i]=-1;

    }
    void initialize(){
        Random random=new Random();
        for (int i=0;i<numOfWeapons;i++){
            genes[i]=random.nextInt(numOfTargets);
            //System.out.print(genes[i]+"  ");
        }
    }
    void calcFit(float [][] probability,ArrayList<Integer>targetValues,ArrayList<Integer>countOfWeapons){
        double sum=0;
        double multiple=1;
        for(int i=0;i<numOfTargets;i++){
            for(int j=0;j<numOfWeapons;j++){
                if(genes[j]==i){
                   int weaponIndex= getWeaponType(j,countOfWeapons);
                   multiple*=probability[weaponIndex][i];
                }
            }
            sum+=(targetValues.get(i) * multiple);
            multiple=1;
        }
        fit=sum;
    }

    static int getWeaponType(int weaponIndex, ArrayList<Integer> countOfWeapons) {
        int sum=0;
        for(int i=0;i<countOfWeapons.size();i++){
            sum+=countOfWeapons.get(i);
            if(weaponIndex<sum)
                return i;
        }
        return -1;
    }
}
