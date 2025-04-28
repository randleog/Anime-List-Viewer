package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;



public class MenuAnimeTimeline extends MenuElement{



    public static double minZoom = 1000;


    public AnimeProfile profile;

    public boolean initialise_zoom = false;


    public static double minZoomImage = 1.5;

    public static long min_interval_load_image = 100;//need to do the same but for querying the api in general as well. however, that would be with the config saving the last query, so we dont question more than every 2 minutes
    public static long lastImageLoad = 0;

    public static double gap = 75;

    public static double determinedDiff = 17.28;

    public static double imageWidth = 1.03*gap/1.45;
    public static double imageHeight = gap*0.95;

    public static double zoom_Text_Visible = 0.5;
    public static double lines_visible = 1.5;

    public static double max_text_Size_zoom = 2;

    public double dragXB=0;
    public double dragYB=0;
    public double dragX=0;
    public double dragY=0;




    public double zoomScale;

    public MenuAnimeTimeline(int x, int y) {
        super(x, y);
    }


    @Override
    public void drawElement(GraphicsContext g) {
        if (profile == null) {
            g.fillText("ERROR: No profile selected",100,100);
            return;
        }
        g.setFill(Color.WHITE);
        g.setFont(Font.font("monospace",9*zoomScale));
        if (!initialise_zoom) {
            initialise_zoom = true;

            double divisionValue = (4882812) * g.getCanvas().getWidth();
            zoomScale = 1.0 / ((profile.endDate - profile.startDate) / divisionValue);
            System.out.println("initialised zoom to " + zoomScale);
        }





        //     double yearFactor = 1000;//and remember to remove the differential between the start of the year and when is started watching anime
        //   for (int i = 0; i < 5; i++) {
        //       canvas.getGraphz



        long currentTime = System.currentTimeMillis();
        double now = getRelativeValue(profile.startDate,currentTime);
        double xv = x*zoomScale;



        //now line
        g.setLineWidth(Math.max(0.5,zoomScale*1.1));

        g.setLineDashes(1);
        g.setStroke(Color.WHITE);
        g.strokeLine(zoomScale*1000+xv+now*zoomScale
                ,0,
                zoomScale*1000+xv+now*zoomScale,
                g.getCanvas().getHeight());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(currentTime);
        g.fillText("NOW "+date, zoomScale*1010+xv+now*zoomScale,((gap)*zoomScale));



        //lines for each year



        int startYear= ( new Date(profile.startDate)).getYear()+1900;
        int totalYears = (date.getYear()+1900)-startYear;

        for (int i = 0; i < totalYears+1; i++) {

            Date yearDate= null;
            //   System.out.println((startYear+i));
            try {
                //      System.out.println((startYear+i));
                yearDate = dateFormat.parse((startYear+i)+"-01-01 01:00:00");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            double yearTime = getRelativeValue(profile.startDate,yearDate.getTime());
            g.strokeLine(zoomScale*1000+xv+yearTime*zoomScale
                    ,0,
                    zoomScale*1000+xv+yearTime*zoomScale,
                    g.getCanvas().getHeight());

            fillTextIfVisible(g,(startYear+i)+"-01-01",zoomScale * 1010 + xv + yearTime * zoomScale, ((gap) * zoomScale));

        }



        int lines = 0;

        for (AnimeLog log : profile.getAnimes()) {


            lines++;


            double yv = gap*zoomScale+y*zoomScale+lines*zoomScale*gap;






            g.setFill(log.getScoreColor());


            //pre watch line
            g.fillRect(xv
                    ,yv+zoomScale*(gap/4)+zoomScale*5.5,
                    getRelativeValue(profile.startDate,log.getEndDate())*zoomScale+zoomScale*1000,
                    2*zoomScale);
            //  canvas.getGraphicsContext2D().setFill(Color.WHITE);


            fillTextIfVisible(g,(int)Math.ceil(getAnimeDayDifference(log.startDate,log.endDate)+1)+" days, " +log.getValue("series_episodes") + " eps "
                    ,zoomScale*910+xv+getRelativeValue(profile.startDate, log.getEndDate())*zoomScale+zoomScale*determinedDiff
                    ,yv+zoomScale*(gap/4));


            //watch time line
            g.fillRect(zoomScale*1000+xv+getRelativeValue(profile.startDate, log.getStartDate())*zoomScale
                    ,yv+zoomScale*(gap/4)+zoomScale*5.5,
                    Math.max(getRelativeValue(log.getStartDate(),log.getEndDate())*zoomScale,zoomScale)+zoomScale*determinedDiff,
                    10*zoomScale);

            double startPointX = getRelativeValue(profile.startDate, log.getShowStartDate());
            double endPointX = getRelativeValue(profile.startDate, log.getShowEndDate());

            g.fillRect(zoomScale*1000+xv+startPointX*zoomScale
                    ,yv+zoomScale*(gap/4)+zoomScale*1.5,
                    zoomScale*1,
                    10*zoomScale);

            g.fillRect(zoomScale*1000+xv+endPointX*zoomScale
                    ,yv+zoomScale*(gap/4)+zoomScale*1.5,
                    zoomScale*1,
                    10*zoomScale);
            //   if ()

            //  System.out.println(zoomScale*1000+xv+getRelativeValue(profile.startDate, log.getStartDate()));

            double day =getRelativeValue(profile.startDate,log.getEndDate())/determinedDiff+ x/determinedDiff-imageWidth/determinedDiff+1000/determinedDiff;//getAnimeDayDifference(profile.startDate,log.getEndDate());
            if (day+1 <0) {

                fillTextIfVisible(g,"< "+String.format("%.02f", day+1)+"d",60*zoomScale,yv+zoomScale*(gap/4));
            } else {

                fillTextIfVisible(g,String.format("%.02f", day+1)+"d >",60*zoomScale,yv+zoomScale*(gap/4));

            }




            g.setFill(Color.WHITE);

            g.fillRect(zoomScale*1000+xv+startPointX*zoomScale
                    ,yv+zoomScale*(gap/4)+zoomScale*1.5,
                    zoomScale*1,
                    10*zoomScale);
            //  canvas.getGraphicsContext2D().fillText("air date: "+ log.getValue("series_start"), zoomScale*920+xv+startPointX*zoomScale,yv+zoomScale*(gap/4)+zoomScale*21);
            fillTextIfVisible(g,"air date: "+ log.getValue("series_start"),zoomScale*920+xv+startPointX*zoomScale
                    ,yv+zoomScale*(gap/4)+zoomScale*25);

            if (endPointX != startPointX) {
                g.fillRect(zoomScale * 1000 + xv + endPointX * zoomScale
                        , yv + zoomScale * (gap / 4) + zoomScale * 1.5,
                        zoomScale * 1,
                        10 * zoomScale);
                fillTextIfVisible(g,"end date: " + log.getValue("series_end"),
                        zoomScale * 920 + xv + endPointX * zoomScale
                        ,yv + zoomScale * (gap / 4) + zoomScale * 25);

            }






            if (!(xv > HelloApplication.getCanvasWidth() || yv > HelloApplication.getCanvasHeight() || yv < 0)) {
                if (minZoomImage <zoomScale) {
                    if (!log.findingImage) {

                        log.findingImage = true;
                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                                while (System.currentTimeMillis()-lastImageLoad < min_interval_load_image) {
                                    try {
                                        Thread.sleep(min_interval_load_image);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                lastImageLoad = System.currentTimeMillis();
                                log.image = new Image(log.getValue("image"), 100, 150, false, false);


                            }
                        });

                        thread.start();


                    }
                }
                if (log.image == null) {
                    g.setFill(Color.valueOf(log.getValue("color")));
                    g.fillRect(0, yv-(gap/2)*zoomScale, imageWidth*zoomScale, zoomScale*imageHeight);
                } else {
                    g.drawImage(log.image, 0, yv-(gap/2)*zoomScale, imageWidth*zoomScale, zoomScale*imageHeight);
                }
                g.setFill(Color.WHITE);
                fillTextIfVisible(g,log.getDisplayString(),60*zoomScale,yv-10*zoomScale);


            }
        }
        g.setFill(Color.rgb(0,0,0,0.8));
        g.fillRect(0,0,HelloApplication.getCanvasWidth(),Math.max(gap*zoomScale/2,gap*zoomScale/2+y*zoomScale));
        g.setFill(Color.WHITE);
        g.setFont(Font.font("monospace", FontWeight.BOLD, FontPosture.REGULAR,18*zoomScale));
        g.fillText(profile.getTitleCard() ,Math.max(0,x*zoomScale),Math.max((gap/3.4)*zoomScale,(gap/3.4)*zoomScale+y*zoomScale));
    }

    public void fillTextIfVisible(GraphicsContext g,String text, double x, double y) {
        if (zoomScale < zoom_Text_Visible) {
            g.fillRect(x, y-zoomScale*4, zoomScale*5*text.split("\n")[0].length(), zoomScale*6);
        } else {
            if (!(x > HelloApplication.getCanvasWidth() || x < 0 || y > HelloApplication.getCanvasHeight() || y < 0)) {
                g.fillText(text, x, y);
            }
        }
    }

    public static double getAnimeDayDifference(long startdate, long enddate) {
        return (getRelativeValue(startdate, enddate))/determinedDiff;
    }
    public static double getRelativeValue(long start, long end) {
        return ((end-start)/5000000.0);
    }
    @Override
    public boolean interactElement(String info, boolean mouseDown, double xp, double yp) {
        return false;
    }

    @Override
    public boolean scroll(double delta, double xp, double yp) {
        double zoomFactor = delta > 0 ? 1.1 : 0.9;
        double distanceToCenterBeforeX = xp;
        double distanceToCenterBeforeY = yp;

        double distanceNowX = distanceToCenterBeforeX * zoomFactor;
        double distanceNowY = distanceToCenterBeforeY * zoomFactor;
        double xdiff = distanceNowX - distanceToCenterBeforeX;
        double ydiff = distanceNowY - distanceToCenterBeforeY;

        if (minZoom < zoomScale*zoomFactor) {
            zoomScale=minZoom;
        } else {
            x -= (xdiff / zoomScale) / zoomFactor;
            y -= (ydiff / zoomScale) / zoomFactor;
            zoomScale=zoomScale*zoomFactor;
        }
        return false;
    }

    @Override
    public boolean drag(double xp, double yp) {
        x = (dragXB-(dragX-xp)/zoomScale);
        y = (dragYB-(dragY-yp)/zoomScale);
        return false;
    }

    @Override
    public boolean mouseRelease(double xp, double yp) {
        dragX = xp;
        dragY = yp;

        return false;
    }

    @Override
    public boolean mouseDown(double xp, double yp) {
        dragX = xp;
        dragY = yp;
        dragXB = x;
        dragYB=y;
        return false;
    }

    @Override
    public String getInfo() {
        return "";
    }
}