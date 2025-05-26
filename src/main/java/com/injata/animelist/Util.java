package com.injata.animelist;

public class Util {
    public static double determinedDiff = 17.28;
    public static final long space = (long)(((1000)*86400000L)/determinedDiff);
    public static final long dayDiff = (long)(86400000L/determinedDiff);


    public static final long dayDiffA = (long)(86400000L);
    public static double getAnimeDayDifference(long startdate, long enddate) {
        return (getRelativeValue(startdate, enddate))/determinedDiff;
    }

    public static double getAnimatedValue(double currentV, double oldV, long currentTime, long startTime, long animationLength) {
        if (currentTime - startTime < animationLength) {

            //   System.out.println(((HelloApplication.currentTime-lastPosUpdate)/1500.0) +  " " +(1-((HelloApplication.currentTime-lastPosUpdate)/1500.0)) );
            return ((currentTime - startTime) / (animationLength * 1.0)) * currentV + (1 - ((currentTime - startTime) / (animationLength * 1.0))) * oldV;
        } else {
            return currentV;
        }
    }

    public static double getRelativeValue(long start, long end) {
        return ((end-start)/5000000.0);
    }
}
