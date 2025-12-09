package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MenuLineGraph extends MenuElement {

    private Date xDate;

    private int largestDay = 0;

    private double largestAmount = 0;

    private static Color mangaLineColor = Color.rgb(255,100,0,0.5);
    private static Color animeLineColor = Color.rgb(255,255,255,0.5);
    private static Color rewatchLineColor = Color.rgb(0,255,255,0.5);

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
  //      System.out.println(getVisibleY());
   //     System.out.println(HelloApplication.mouseY );
        double day = -(parent.x / Util.determinedDiff + 1000 / Util.determinedDiff - AnimeLog.imageWidth / Util.determinedDiff + 1);
        double mousedayD = (day+0.3+(HelloApplication.mouseX/zoomScale)/Util.determinedDiff-3);
        int mousedayV = (int)mousedayD;


        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");


        long milliSeconds= Long.parseLong(""+((long)((mousedayD)*(Util.YEAR)+profile.startDate)));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        String mouseDay = formatter.format(calendar.getTime());


        g.setFont(new Font(20));
        if (HelloApplication.mouseY < HelloApplication.getCanvasHeight() - lineHeight*4) {
            g.fillText((mouseDay+"")
                    ,HelloApplication.mouseX,
                    HelloApplication.mouseY+100);

            return;
        }




        g.setFill(Color.BLACK);
        //pre watch line
     //   g.fillRect(AnimeLog.imageWidth * zoomScale
     //           , HelloApplication.getCanvasHeight() - lineHeight,
     //           HelloApplication.getCanvasWidth(),
     //           lineHeight);
     //   g.setFill(Color.GREY);
    //    g.fillRect(AnimeLog.imageWidth * zoomScale
      //          , HelloApplication.getCanvasHeight() - lineY,
     // //          HelloApplication.getCanvasWidth(),
     //           1);
        g.setFill(Color.WHITE);
        double traveler = 0;
        int days = 0;
        //  int startYear= ( new Date(profile.startDate)).getYear()+1900;
        //     int totalYears = (date.getYear()+1900)-startYear;

        //   double day = Util.getRelativeValue(profile.startDate, getEndDate()) / Util.determinedDiff + parent.x / Util.determinedDiff  ;
        double totalDays = (getWidth() / zoomScale) / Util.determinedDiff;
        double totalDaysAll = Util.getAnimeDayDifference(profile.startDate,profile.endDate);

        //    System.out.println(day + " " + totalDays);
        double diff = day - (int) day;
        drawAnimeLine(totalDays, profile, totalDaysAll, day, diff, zoomScale, g,"",animeLineColor);
        drawAnimeLine(totalDays, profile, totalDaysAll, day, diff, zoomScale, g, "Manga", mangaLineColor);
        drawAnimeLine(totalDays, profile, totalDaysAll, day, diff, zoomScale, g, "Rewatch", rewatchLineColor);

        if (profile.dayLog.getOrDefault(mousedayV + "_Pace", 0.0) > profile.dayLog.getOrDefault(mousedayV + "_MangaPace", 0.0) ) {
            if (profile.dayLog.getOrDefault(mousedayV + "_RewatchPace", 0.0) > profile.dayLog.getOrDefault(mousedayV + "_Pace", 0.0) ) {
                g.setFill(rewatchLineColor);
            } else {
                g.setFill(animeLineColor);
            }
        } else {
            if (profile.dayLog.getOrDefault(mousedayV + "_RewatchPace", 0.0) > profile.dayLog.getOrDefault(mousedayV + "_MangaPace", 0.0) ) {
                g.setFill(rewatchLineColor);
            } else {
                g.setFill(mangaLineColor);
            }
        }

        g.fillText((mouseDay+"\n"+String.format("%.2f",profile.dayLog.getOrDefault(mousedayV + "_Pace", 0.0)) +"m\n"+profile.dayLog.getOrDefault(mousedayV+ "_Count", 0.0) +" ongoing")
                ,HelloApplication.mouseX,
                HelloApplication.mouseY+100);
    }



    private static double lineOffset = -2.3;

    boolean polygonOption = false;
    private void drawAnimeLine(double totalDays, AnimeProfile profile, double totalDaysAll, double day, double diff, double zoomScale, GraphicsContext g, String mediaKey, Color color) {
        int mouseDay = (int)(day+(HelloApplication.mouseX/zoomScale)/Util.determinedDiff-2);



        double[] xpos = new double[(int) totalDays];
        double[] ypos = new double[(int) totalDays];
        int counted = 0;
        g.setFill(color);
        double previousX = 0;
        for (int i = 0; i < (int) totalDays; i++) {
            double value = profile.dayLog.getOrDefault((int) ((i + day)) + "_"+mediaKey+"Pace", 0.0);
            if (i+day > totalDaysAll) {
                i = (int)totalDays;
            } else if (i+day >0) {


                if (largestAmount < value) {
                    largestAmount = value;
                    largestDay = (int) ((i + day));
                }






                if (polygonOption) {
                    xpos[counted] = (i - diff) * zoomScale * Util.determinedDiff + AnimeLog.imageWidth * zoomScale + lineOffset * zoomScale;
                    ypos[counted] = HelloApplication.getCanvasHeight() - lineY - value * lineScale;
                } else {
                    g.fillRect((i - diff) * zoomScale * Util.determinedDiff + AnimeLog.imageWidth * zoomScale + lineOffset * zoomScale
                            ,HelloApplication.getCanvasHeight() - lineY - value * lineScale
                            , zoomScale * Util.determinedDiff
                            ,value * lineScale);
                }

                counted++;
                //  System.out.println((int)(i+day)+"_pace" + " " + profile.dayLog.getOrDefault((int)((i+day))+"_pace",0.0));


            }
        }
        if (polygonOption) {
            g.setStroke(color);
            g.strokePolyline(xpos, ypos, counted);
        }





        for (int i = 0; i < (int) totalDays; i++) {
            double value = profile.dayLog.getOrDefault((int) ((i + day)) +  "_"+mediaKey+"Pace", 0.0);
            if (i+day > totalDaysAll) {
                i = (int)totalDays;
            } else if (i+day >0) {


                if (largestAmount < value) {
                    largestAmount = value;
                    largestDay = (int) ((i + day));
                }
                if (!(counted >= xpos.length)) {
                    xpos[counted] = (i - diff) * zoomScale * Util.determinedDiff + AnimeLog.imageWidth * zoomScale;
                    ypos[counted] = HelloApplication.getCanvasHeight() - lineY - value * lineScale;
                }
                counted++;

                //  System.out.println((int)(i+day)+"_pace" + " " + profile.dayLog.getOrDefault((int)((i+day))+"_pace",0.0));


            }
        }
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
