package com.gen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static Scanner sc;

    public static void main(String[] args) throws IOException {
        sc = new Scanner(System.in);
        Info info = takeInput();
        System.out.println("Please wait while running the GA…\n");
        File uniformFile=new File("uniform.txt");
        String uniformData="";
        List<Chromosome> uniformOut=new ArrayList<>();
        for (int j = 0; j < 20; j++) {
            Population population = new Population(info);
            StepsMaker stepsMaker = new StepsMaker(population);

            stepsMaker.startSteps(1);
            uniformData+="The final marketing budget allocation is:\n";
            //System.out.println("The final marketing budget allocation is:");
            for (int i = 0; i < population.pop.get(0).genes.length; i++) {
                uniformData+=population.info.channelName[i] + " -> " + population.pop.get(0).genes[i] + "k\n";
                //System.out.println(population.info.channelName[i] + " -> " + population.pop.get(0).genes[i] + "k");
            }
            uniformData+="The total profit is " + population.pop.get(0).totalProfit + "k\n";
            //System.out.println("The total profit is " + population.pop.get(0).totalProfit + "k");
            uniformData+="the total budget is "+population.pop.get(0).budget+"k\n\n";
            uniformOut.add(population.pop.get(0));
            //System.out.println(population.pop.get(0).budget);
            //System.out.println();
        }

        writeFile(uniformFile,uniformData);

        uniformOut= uniformOut.stream().sorted(Comparator.comparing(Chromosome::getTotalProfit).reversed()).collect(Collectors.toList());

        System.out.println("the best profit in UniformMutation is");

        for (int i = 0; i < uniformOut.get(0).genes.length; i++) {
            System.out.println(info.channelName[i] + " -> " + uniformOut.get(0).genes[i] + "k");
        }
        System.out.println("The total profit is " + uniformOut.get(0).totalProfit + "k\n");
        uniformOut.clear();


        System.out.println("Please wait while running the GA…\n");


        File nonUniformFile=new File("nonUniform.txt");
        String nonUniformData="";
        List<Chromosome> nonUniformOut=new ArrayList<>();

        for (int j = 0; j < 20; j++) {
            Population population = new Population(info);
            StepsMaker stepsMaker = new StepsMaker(population);

            stepsMaker.startSteps(1);
            nonUniformData+="The final marketing budget allocation is:\n";
            //System.out.println("The final marketing budget allocation is:");
            for (int i = 0; i < population.pop.get(0).genes.length; i++) {
                nonUniformData+=population.info.channelName[i] + " -> " + population.pop.get(0).genes[i] + "k\n";
                //System.out.println(population.info.channelName[i] + " -> " + population.pop.get(0).genes[i] + "k");
            }
            nonUniformData+="The total profit is " + population.pop.get(0).totalProfit + "k\n";
            //System.out.println("The total profit is " + population.pop.get(0).totalProfit + "k");
            nonUniformData+="the total budget is "+population.pop.get(0).budget+"k\n\n";
            //System.out.println(population.pop.get(0).budget);
            //System.out.println();
            nonUniformOut.add(population.pop.get(0));

        }
        writeFile(nonUniformFile,nonUniformData);
        nonUniformOut= nonUniformOut.stream().sorted(Comparator.comparing(Chromosome::getTotalProfit).reversed()).collect(Collectors.toList());

        System.out.println("the best profit in NonUnifrom Mutation is");

        for (int i = 0; i < nonUniformOut.get(0).genes.length; i++) {
            System.out.println(info.channelName[i] + " -> " + nonUniformOut.get(0).genes[i] + "k");
        }
        System.out.println("The total profit is " + nonUniformOut.get(0).totalProfit + "k\n");
    }
    private static void writeFile(File file,String date) throws IOException {
        file.createNewFile();
        FileWriter fileWriter=new FileWriter(file);
        fileWriter.write(date);
        fileWriter.close();
    }

    private static Info takeInput() {
        System.out.println("Enter the marketing budget (in thousands):");
        float totalBudget = sc.nextInt();
        System.out.println("Enter the number of marketing channels:");
        int numOfChannels = sc.nextInt();


        Info info = new Info(totalBudget, numOfChannels);
        System.out.println("Enter the name and ROI (in %) of each channel separated by space:");
        String line;
        StringBuilder num;
        sc.nextLine();
        for (int i = 0; i < numOfChannels; i++) {
            line = sc.nextLine();
            line = line.trim();
            num = new StringBuilder();

            for (int k = line.length() - 1; k >= 0; k--) {
                if (line.charAt(k) == ' ') break;
                num.append(line.charAt(k));
            }
            num = num.reverse();
            info.channelsROI[i] = Float.parseFloat(num.toString().trim());
            info.channelName[i] = line.substring(0, line.length() - num.length()).trim();
        }


        System.out.println("Enter the lower (k) and upper bounds (%) of investment in each channel:\n" +
                "(enter x if there is no bound)");
        String bound;
        float lower, upper;
        for (int i = 0; i < numOfChannels; i++) {
            bound = sc.next();
            if (bound.equals("x"))
                lower = 0;
            else
                lower = Float.parseFloat(bound);
            bound = sc.next();
            if (bound.equals("x"))
                upper = totalBudget;
            else
                upper = Float.parseFloat(bound);
            info.bounders.add(new GeneBounder(lower, upper, totalBudget));
        }
        /*for (GeneBounder gene:
             info.bounders) {
            System.out.println(gene.lower+" "+gene.upper);
        }*/
        return info;
    }
}
