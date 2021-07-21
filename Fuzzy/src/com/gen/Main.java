package com.gen;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            double[] mProjectFunding = new double[4];
            double[] mTeamExp = new double[4];
            System.out.print("Enter Project Fund: ");
            double projectFund = sc.nextDouble();
            System.out.print("Enter Experience Level: ");
            double experienceLevel = sc.nextDouble();
            if (projectFund >= 0.0 && projectFund <= 10.0)
                mProjectFunding[0] = 1;
            else if (projectFund > 10.0 && projectFund < 30.0) {
                Point point1 = new Point(10, 1);
                Point point2 = new Point(30, 0);

                Point point3 = new Point(10, 0);
                Point point4 = new Point(30, 1);

                mProjectFunding[0] = Point.getM(point1, point2, projectFund);
                mProjectFunding[1] = Point.getM(point3, point4, projectFund);
            } else if (projectFund >= 30 && projectFund <= 40) {
                mProjectFunding[1] = 1;
            } else if (projectFund > 40.0 && projectFund < 60.0) {
                Point point1 = new Point(40, 1);
                Point point2 = new Point(60, 0);

                Point point3 = new Point(40, 0);
                Point point4 = new Point(60, 1);

                mProjectFunding[1] = Point.getM(point1, point2, projectFund);
                mProjectFunding[2] = Point.getM(point3, point4, projectFund);
            } else if (projectFund >= 60.0 && projectFund <= 70.0) {
                mProjectFunding[2] = 1;
            } else if (projectFund > 70 && projectFund < 90) {
                Point point1 = new Point(70, 1);
                Point point2 = new Point(90, 0);

                Point point3 = new Point(70, 0);
                Point point4 = new Point(90, 1);

                mProjectFunding[2] = Point.getM(point1, point2, projectFund);
                mProjectFunding[3] = Point.getM(point3, point4, projectFund);
            } else if (projectFund >= 90.0 && projectFund <= 100.0) {
                mProjectFunding[3] = 1;
            }


            if (experienceLevel >= 0 && experienceLevel <= 15) {
                Point point1 = new Point(0, 0);
                Point point2 = new Point(15, 1);
                mTeamExp[0] = Point.getM(point1, point2, experienceLevel);
            } else if (experienceLevel > 15 && experienceLevel < 30) {
                Point point1 = new Point(15, 1);
                Point point2 = new Point(30, 0);

                Point point3 = new Point(15, 0);
                Point point4 = new Point(30, 1);

                mTeamExp[0] = Point.getM(point1, point2, experienceLevel);
                mTeamExp[1] = Point.getM(point3, point4, experienceLevel);
            } else if (experienceLevel == 30) {
                mTeamExp[1] = 1;
            } else if (experienceLevel > 30 && experienceLevel < 45) {
                Point point1 = new Point(30, 1);
                Point point2 = new Point(45, 0);

                Point point3 = new Point(30, 0);
                Point point4 = new Point(60, 1);

                mTeamExp[1] = Point.getM(point1, point2, experienceLevel);
                mTeamExp[2] = Point.getM(point3, point4, experienceLevel);
            } else if (experienceLevel >= 45 && experienceLevel <= 60) {
                Point point1 = new Point(30, 0);
                Point point2 = new Point(60, 1);
                mTeamExp[2] = Point.getM(point1, point2, experienceLevel);
            }


            double mRiskL = Math.max(mProjectFunding[3], mTeamExp[2]);
            double mRiskNormal = Math.min(mProjectFunding[2], Math.max(mTeamExp[0], mTeamExp[1]));
            double mRiskHigh1 = mProjectFunding[0];
            double mRiskHigh2 = Math.min(mProjectFunding[1], mTeamExp[0]);


            double centroidH = (25 + 50) / 3.0;
            double centroidN = (25 + 50 + 75) / 3.0;
            double centroidL = (50 + 100 + 100) / 3.0;

            double weight = ((mRiskL * centroidL) + (mRiskNormal * centroidN) + (mRiskHigh1 * centroidH) + (mRiskHigh2 * centroidH)) /
                    (mRiskL + mRiskNormal + mRiskHigh1 + mRiskHigh2);
            System.out.println("Predicted Value (Risk)= " + weight);
            System.out.print("Risk will be ");
            if (weight >= 0 && weight <= 25)
                System.out.println("High");
            else if (weight > 25 && weight < 50) {
                Point point1 = new Point(25, 1);
                Point point2 = new Point(50, 0);

                Point point3 = new Point(25, 0);
                Point point4 = new Point(50, 1);

                double prob1 = Point.getM(point1, point2, weight);
                double prob2 = Point.getM(point3, point4, weight);
                if (prob1 > prob2)
                    System.out.println("High");
                else if (prob1 < prob2)
                    System.out.println("Normal");
                else
                    System.out.println("High and Normal");

            } else if (weight == 50)
                System.out.println("Normal");
            else if (weight > 50 && weight < 75) {
                Point point1 = new Point(50, 1);
                Point point2 = new Point(75, 0);

                Point point3 = new Point(50, 0);
                Point point4 = new Point(100, 1);

                double prob1 = Point.getM(point1, point2, weight);
                double prob2 = Point.getM(point3, point4, weight);
                if (prob1 > prob2)
                    System.out.println("Normal");
                else if (prob1 < prob2)
                    System.out.println("Low");
                else
                    System.out.println("Normal and Low");


            } else if (weight >= 75 && weight <= 100)
                System.out.println("Low");

        }


    }
}
