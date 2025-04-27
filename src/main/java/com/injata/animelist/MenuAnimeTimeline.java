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




    public AnimeProfile profile;

    public boolean initialise_zoom = false;


    public static double minZoomImage = 1.5;

    public static long min_interval_load_image = 100;//need to do the same but for querying the api in general as well. however, that would be with the config saving the last query, so we dont question more than every 2 minutes
    public static long lastImageLoad = 0;

    public static double gap = 75;

    public static double determinedDiff = 17.28;

    public static double imageWidth = 1.03*gap/1.5;
    public static double imageHeight = gap*0.95;

    public static double zoom_Text_Visible = 0.5;
    public static double lines_visible = 1.5;

    public static double max_text_Size_zoom = 2;

    public MenuAnimeTimeline(int x, int y) {
        super(x, y);
    }


    @Override
    public void drawElement(GraphicsContext g, double zoom, double xp, double yp) {
            if (profile == null) {
                g.fillText("ERROR: No profile selected",100,100);
                return;
            }
            g.setFill(Color.WHITE);
            g.setFont(Font.font("monospace",9*HelloApplication.zoomScale));
            if (HelloApplication.zoomScale==0) {
                initialise_zoom = true;

                double divisionValue = (4882812) * g.getCanvas().getWidth();
                HelloApplication.zoomScale = 1.0 / ((profile.endDate - profile.startDate) / divisionValue);
                System.out.println("initialised zoom to " + HelloApplication.zoomScale);
            }





        //     double yearFactor = 1000;//and remember to remove the differential between the start of the year and when is started watching anime
        //   for (int i = 0; i < 5; i++) {
        //       canvas.getGraphz



        long currentTime = System.currentTimeMillis();
        double now = getRelativeValue(profile.startDate,currentTime);
        double xv = HelloApplication.x*HelloApplication.zoomScale;



        //now line
        g.setLineWidth(Math.max(0.5,HelloApplication.zoomScale*1.1));

        g.setLineDashes(1);
        g.setStroke(Color.WHITE);
        g.strokeLine(HelloApplication.zoomScale*1000+xv+now*HelloApplication.zoomScale
                ,0,
                HelloApplication.zoomScale*1000+xv+now*HelloApplication.zoomScale,
                g.getCanvas().getHeight());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(currentTime);
        g.fillText("NOW "+date, HelloApplication.zoomScale*1010+xv+now*HelloApplication.zoomScale,((gap)*HelloApplication.zoomScale));



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
            g.strokeLine(HelloApplication.zoomScale*1000+xv+yearTime*HelloApplication.zoomScale
                    ,0,
                    HelloApplication.zoomScale*1000+xv+yearTime*HelloApplication.zoomScale,
                    g.getCanvas().getHeight());

            fillTextIfVisible(g,(startYear+i)+"-01-01",HelloApplication.zoomScale * 1010 + xv + yearTime * HelloApplication.zoomScale, ((gap) * HelloApplication.zoomScale));

        }



        int lines = 0;

        for (AnimeLog log : profile.getAnimes()) {


            lines++;


            double yv = gap*HelloApplication.zoomScale+HelloApplication.y*HelloApplication.zoomScale+lines*HelloApplication.zoomScale*gap;






                g.setFill(log.getScoreColor());
                g.fillRect(xv
                        ,yv+HelloApplication.zoomScale*(gap/4)+HelloApplication.zoomScale*5.5,
                        getRelativeValue(profile.startDate,log.getEndDate())*HelloApplication.zoomScale+HelloApplication.zoomScale*1000,
                        2*HelloApplication.zoomScale);
                //  canvas.getGraphicsContext2D().setFill(Color.WHITE);


                fillTextIfVisible(g,(int)Math.ceil(getAnimeDayDifference(log.startDate,log.endDate)+1)+" days, " +log.getValue("series_episodes") + " eps"
                        ,HelloApplication.zoomScale*910+xv+getRelativeValue(profile.startDate, log.getEndDate())*HelloApplication.zoomScale+HelloApplication.zoomScale*determinedDiff
                        ,yv+HelloApplication.zoomScale*(gap/4));

                g.fillRect(HelloApplication.zoomScale*1000+xv+getRelativeValue(profile.startDate, log.getStartDate())*HelloApplication.zoomScale
                        ,yv+HelloApplication.zoomScale*(gap/4)+HelloApplication.zoomScale*1.5,
                        Math.max(getRelativeValue(log.getStartDate(),log.getEndDate())*HelloApplication.zoomScale,HelloApplication.zoomScale)+HelloApplication.zoomScale*determinedDiff,
                        10*HelloApplication.zoomScale);

                double startPointX = getRelativeValue(profile.startDate, log.getShowStartDate());
                double endPointX = getRelativeValue(profile.startDate, log.getShowEndDate());

                g.fillRect(HelloApplication.zoomScale*1000+xv+startPointX*HelloApplication.zoomScale
                        ,yv+HelloApplication.zoomScale*(gap/4)+HelloApplication.zoomScale*1.5,
                        HelloApplication.zoomScale*1,
                        10*HelloApplication.zoomScale);

                g.fillRect(HelloApplication.zoomScale*1000+xv+endPointX*HelloApplication.zoomScale
                        ,yv+HelloApplication.zoomScale*(gap/4)+HelloApplication.zoomScale*1.5,
                        HelloApplication.zoomScale*1,
                        10*HelloApplication.zoomScale);
                //   if ()

                //  System.out.println(zoomScale*1000+xv+getRelativeValue(profile.startDate, log.getStartDate()));

                double day =getRelativeValue(profile.startDate,log.getEndDate())/determinedDiff+ x/determinedDiff-imageWidth/determinedDiff+1000/determinedDiff;//getAnimeDayDifference(profile.startDate,log.getEndDate());
                if (day+1 <0) {

                    fillTextIfVisible(g,"< "+String.format("%.02f", day+1)+"d",60*HelloApplication.zoomScale,yv+HelloApplication.zoomScale*(gap/4));
                } else {

                    fillTextIfVisible(g,String.format("%.02f", day+1)+"d >",60*HelloApplication.zoomScale,yv+HelloApplication.zoomScale*(gap/4));

                }




                g.setFill(Color.WHITE);

                g.fillRect(HelloApplication.zoomScale*1000+xv+startPointX*HelloApplication.zoomScale
                        ,yv+HelloApplication.zoomScale*(gap/4)+HelloApplication.zoomScale*1.5,
                        HelloApplication.zoomScale*1,
                        10*HelloApplication.zoomScale);
                //  canvas.getGraphicsContext2D().fillText("air date: "+ log.getValue("series_start"), zoomScale*920+xv+startPointX*zoomScale,yv+zoomScale*(gap/4)+zoomScale*21);
                fillTextIfVisible(g,"air date: "+ log.getValue("series_start"),HelloApplication.zoomScale*920+xv+startPointX*HelloApplication.zoomScale,yv+HelloApplication.zoomScale*(gap/4)+HelloApplication.zoomScale*21);

                if (endPointX != startPointX) {
                    g.fillRect(HelloApplication.zoomScale * 1000 + xv + endPointX * HelloApplication.zoomScale
                            , yv + HelloApplication.zoomScale * (gap / 4) + HelloApplication.zoomScale * 1.5,
                            HelloApplication.zoomScale * 1,
                            10 * HelloApplication.zoomScale);
                    fillTextIfVisible(g,"end date: " + log.getValue("series_end"),HelloApplication.zoomScale * 920 + xv + endPointX * HelloApplication.zoomScale
                            ,yv + HelloApplication.zoomScale * (gap / 4) + HelloApplication.zoomScale * 21);

                }






            if (!(xv > HelloApplication.CANVAS_WIDTH || yv > HelloApplication.CANVAS_HEIGHT || yv < 0)) {
                if (minZoomImage <HelloApplication.zoomScale) {
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
                    g.fillRect(0, yv-(gap/2)*HelloApplication.zoomScale, imageWidth*HelloApplication.zoomScale, HelloApplication.zoomScale*imageHeight);
                } else {
                    g.drawImage(log.image, 0, yv-(gap/2)*HelloApplication.zoomScale, imageWidth*HelloApplication.zoomScale, HelloApplication.zoomScale*imageHeight);
                }
                g.setFill(Color.WHITE);
                fillTextIfVisible(g,log.getDisplayString(),60*HelloApplication.zoomScale,yv-10*HelloApplication.zoomScale);


            }
        }
        g.setFill(Color.rgb(0,0,0,0.8));
        g.fillRect(0,0,HelloApplication.CANVAS_WIDTH,Math.max(gap*HelloApplication.zoomScale/2,gap*HelloApplication.zoomScale/2+HelloApplication.y*HelloApplication.zoomScale));
        g.setFill(Color.WHITE);
        g.setFont(Font.font("monospace", FontWeight.BOLD, FontPosture.REGULAR,18*HelloApplication.zoomScale));
        g.fillText(profile.getTitleCard() ,Math.max(0,HelloApplication.x*HelloApplication.zoomScale),Math.max((gap/3.4)*HelloApplication.zoomScale,(gap/3.4)*HelloApplication.zoomScale+HelloApplication.y*HelloApplication.zoomScale));
    }

    public void fillTextIfVisible(GraphicsContext g,String text, double x, double y) {
        if (HelloApplication.zoomScale < zoom_Text_Visible) {
           g.fillRect(x, y-HelloApplication.zoomScale, HelloApplication.zoomScale*5*text.split("\n")[0].length(), HelloApplication.zoomScale*6);
        } else {
            if (!(x > HelloApplication.CANVAS_WIDTH || x < 0 || y > HelloApplication.CANVAS_HEIGHT || y < 0)) {
                g.fillText(text, x, y);
            }
        }
    }

    public double getAnimeDayDifference(long startdate, long enddate) {
        return (getRelativeValue(startdate, enddate))/determinedDiff;
    }
    public double getRelativeValue(long start, long end) {
        return ((end-start)/5000000.0);
    }
    @Override
    public boolean interactElement(String info, boolean mouseDown, double zoom, double xp, double yp) {
        return false;
    }

    @Override
    public String getInfo() {
        return "";
    }
}
