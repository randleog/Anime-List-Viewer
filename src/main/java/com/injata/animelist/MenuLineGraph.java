package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Date;

public class MenuLineGraph extends MenuElement {

    private Date xDate;

    private int largestDay = 0;
    private double largestAmount = 0;

    public MenuLineGraph(int x, int y) {
        super(x, y);

    }


    @Override
    public double getWidth() {
        return HelloApplication.getCanvasWidth();
    }

    private static final int lineHeight = 100;
    private static final int lineY = 40;
    private static final double lineScale = 0.2;

    public void draw(GraphicsContext g, double zoomScale, AnimeProfile profile, Date date) {


        double yv = getVisibleY() * zoomScale;
        double xv = getVisibleX() * zoomScale;


        g.setFill(Color.BLACK);
        //pre watch line
        g.fillRect(AnimeLog.imageWidth * zoomScale
                , HelloApplication.getCanvasHeight() - lineHeight,
                HelloApplication.getCanvasWidth(),
                lineHeight);
        g.setFill(Color.GREY);
        g.fillRect(AnimeLog.imageWidth * zoomScale
                , HelloApplication.getCanvasHeight() - lineY,
                HelloApplication.getCanvasWidth(),
                1);
        g.setFill(Color.WHITE);
        double traveler = 0;
        int days = 0;
        //  int startYear= ( new Date(profile.startDate)).getYear()+1900;
        //     int totalYears = (date.getYear()+1900)-startYear;

        //   double day = Util.getRelativeValue(profile.startDate, getEndDate()) / Util.determinedDiff + parent.x / Util.determinedDiff  ;
        double totalDays = (getWidth() / zoomScale) / Util.determinedDiff;
        double totalDaysAll = Util.getAnimeDayDifference(profile.startDate,profile.endDate);
        double day = -(parent.x / Util.determinedDiff + 1000 / Util.determinedDiff - AnimeLog.imageWidth / Util.determinedDiff + 1);
        //    System.out.println(day + " " + totalDays);
        double diff = day - (int) day;

       
        double[] xpos = new double[(int) totalDays];
        double[] ypos = new double[(int) totalDays];
        int counted = 0;
        for (int i = 0; i < (int) totalDays; i++) {
            double value = profile.dayLog.getOrDefault((int) ((i + day)) + "_pace", 0.0);
            if (i+day > totalDaysAll) {
                i = (int)totalDays;
            } else if (i+day >0) {


                if (largestAmount < value) {
                    largestAmount = value;
                    largestDay = (int) ((i + day));
                }

                xpos[counted] = (i - diff) * zoomScale * Util.determinedDiff + AnimeLog.imageWidth * zoomScale;
                ypos[counted] = HelloApplication.getCanvasHeight() - lineY - value * lineScale;
                counted++;
                //  System.out.println((int)(i+day)+"_pace" + " " + profile.dayLog.getOrDefault((int)((i+day))+"_pace",0.0));


            }
        }
        g.strokePolyline(xpos, ypos, counted);
        int mouseDay = (int)(day+(HelloApplication.mouseX/zoomScale)/Util.determinedDiff-2);
        g.fillText((mouseDay+"\n"+String.format("%.2f",profile.dayLog.getOrDefault(mouseDay + "_pace", 0.0)) +"m\n"+profile.dayLog.getOrDefault(mouseDay+ "_count", 0.0) +" ongoing")
                ,HelloApplication.mouseX,
                HelloApplication.getCanvasHeight() - lineY-10 );


    }

    @Override
    public void drawElement(GraphicsContext g) {

        //double yv = getVisibleY() * zoomScale;
        // double xv = getVisibleX() * zoomScale;
    }

    @Override
    public boolean interactElement(String info, boolean mouseDown, double xp, double yp) {
        return false;
    }

    @Override
    public boolean scroll(double delta, double xp, double yp) {
        return false;
    }

    @Override
    public boolean drag(double xp, double yp) {
        return false;
    }

    @Override
    public boolean mouseRelease(double xp, double yp) {
        return false;
    }

    @Override
    public boolean mouseDown(double xp, double yp) {
        return false;
    }

    @Override
    public String getInfo() {
        return null;
    }
}
