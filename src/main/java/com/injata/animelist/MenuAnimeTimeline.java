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



    public static double gap = 75;

    public static double zoom_Text_Visible = 0.5;


    public double dragXB=0;
    public double dragYB=0;
    public double dragX=0;
    public double dragY=0;




    public double zoomScale;


    public MenuAnimeTimeline(int x, int y, MenuDirections direction) {
        super(x, y, direction);
    }



    public void setProfile(AnimeProfile profile) {
        for (AnimeLog log : profile.getAnimes()) {
            log.parent=this;
        }
        this.profile=profile;
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



        g.setStroke(Color.WHITE);
        g.fillRect(zoomScale*1000+xv+now*zoomScale
                ,0,
                zoomScale*3,
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
            g.fillRect(zoomScale*1000+xv+yearTime*zoomScale
                    ,0,
                    zoomScale*2,
                    g.getCanvas().getHeight());

            fillTextIfVisible(g,(startYear+i)+"-01-01",zoomScale * 1010 + xv + yearTime * zoomScale, ((gap) * zoomScale));

        }



        int lines = 0;

        for (AnimeLog log : profile.getAnimes()) {
            log.draw(g,zoomScale,profile);
        }
   //     g.setFill(Color.rgb(0,0,0,0.8));
      //  g.fillRect(0,0,HelloApplication.getCanvasWidth(),Math.max(gap*zoomScale/2,gap*zoomScale/2+y*zoomScale));
        g.setFill(Color.WHITE);
        g.setFont(Font.font("monospace", FontWeight.BOLD, FontPosture.REGULAR,18*zoomScale));
        g.fillText(profile.getTitleCard() ,Math.max(0,x*zoomScale),(gap/3.4)*zoomScale+y*zoomScale);
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
        return "timeline";
    }
}