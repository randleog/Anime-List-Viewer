package com.injata.animelist;

public class Util {
    public static double determinedDiff = 17.28;
    public static final long space = (long)(((1000)*86400000L)/determinedDiff);
    public static final long dayDiff = (long)(86400000L/determinedDiff);


    public static final long dayDiffA = (long)(86400000L);
    public static double getAnimeDayDifference(long startdate, long enddate) {
        return (getRelativeValue(startdate, enddate))/determinedDiff;
    }

    public static double getRelativeValue(long start, long end) {
        return ((end-start)/5000000.0);
    }
}
