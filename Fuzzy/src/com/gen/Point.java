package com.gen;

public class Point {
    double x;
    double y;
    Point(double x, double y){
        this.x=x;
        this.y=y;
    }
    public static double getM(Point p1,Point p2,double z){
        double slope = (p2.y-p1.y)/(p2.x-p1.x);
        return (p1.y+(slope*(z-p1.x)));
    }
}
