package com.injata.animelist;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AnimeLog extends MenuElement{


    public int episodes;

    public int watchedEpisodes;
    public double score;

    public long startDate;
    public long endDate;


    public long showStartDate;
    public long showEndDate;

    public JsonNode rawData;


    public int rewatches;


    public HashMap<String, String> animeValues;

    public boolean displayEN = true;

    public static ArrayList<Integer[]> colors;

    public Color color;



    public static void loadColors () {
        colors = new ArrayList<>();

        colors.add(new Integer[]{0,0,0});
        colors.add(new Integer[]{30,0,0});
        colors.add(new Integer[]{55,0,0});
        colors.add(new Integer[]{80,0,0});
        colors.add(new Integer[]{105,0,0});
        colors.add(new Integer[]{130,0,0});
        colors.add(new Integer[]{180,0,0});
        colors.add(new Integer[]{255,30,0});
        colors.add(new Integer[]{255,70,0});
        colors.add(new Integer[]{250,80,0});
        colors.add(new Integer[]{240,100,0});
        colors.add(new Integer[]{230,115,0});
        colors.add(new Integer[]{210,135,0});
        colors.add(new Integer[]{190,150,0});
        colors.add(new Integer[]{180,180,0});
        colors.add(new Integer[]{170,180,0});
        colors.add(new Integer[]{160,190,0});
        colors.add(new Integer[]{150,200,0});
        colors.add(new Integer[]{0,255,0});
        colors.add(new Integer[]{0,255,180});
        colors.add(new Integer[]{0,255,320});

    }

    public static Color getColorFromScore (double score){



        double v=Math.ceil(((score/10.0)*colors.size())*10.0)/10.0-1;

        Integer[] rgb1 = colors.get((int)(v));
        Integer[] rgb2 = colors.get((int)Math.ceil(v));




        double rgb2Fraction = ((v)-(int)(v));
        double rgb1Fraction = 1-((v)-(int)(v));
  //      System.out.println(v + " r:"+((int)Math.min(255,(rgb1[0]*rgb1Fraction+rgb2[0]*rgb2Fraction)))+" g:"+Math.min(255,(int)(rgb1[1]*rgb1Fraction+rgb2[1]*rgb2Fraction))+ " b:"+Math.min(255,(int)(rgb1[2]*rgb1Fraction+rgb2[2]*rgb2Fraction)));

        return Color.rgb((int)Math.min(255,(rgb1[0]*rgb1Fraction+rgb2[0]*rgb2Fraction)),Math.min(255,(int)(rgb1[1]*rgb1Fraction+rgb2[1]*rgb2Fraction)),Math.min(255,(int)(rgb1[2]*rgb1Fraction+rgb2[2]*rgb2Fraction)));
    }




    public boolean findingImage = false;
    public Image image;

    public status animestatus;



    @Override
    public void drawElement(GraphicsContext g) {

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
        return "";
    }

    public enum status {
        COMPLETED {
            public String textValue() {
                return "COMPLETED";
            }
        },
        DROPPED {
            public String textValue() {
                return "DROPPED";
            }
        },
        PAUSED {
            public String textValue() {
                return "PAUSED";
            }
        },
        PLANNING {
            public String textValue() {
                return "PLANNING";
            }
        },
        CURRENT {
            public String textValue() {
                return "CURRENT";
            }
        };

        public abstract String textValue();

    }


    public AnimeLog() {
        super(0,0);
        startDate = 0;
        endDate = 0;
        score =0;
        watchedEpisodes = 0;
        episodes = 0;
        animeValues = new HashMap<>();
        animestatus = status.PAUSED;
        rewatches= 0;

    }

    public String getDisplayString() {
        return getValue("series_title" + (displayEN ? "_english" : "")) + "\n"
                + getValue("my_score")+ "/10 " +  getValue("my_status") + " re-watch:" +rewatches + "\n"
                + getValue("my_start_date")+ " to " + getValue("my_finish_date");
    }


    public void setValue(String type, String input) {
        if (input == null) {
            return;
        }
        switch(type) {
            case "my_status"-> {
                animestatus = status.valueOf(input);
                animeValues.put(type,input);
            }
            case "series_episodes" -> episodes = Integer.parseInt(input);
            case "my_watched_episodes" -> watchedEpisodes = Integer.parseInt(input);
            case "my_score" -> score = Integer.parseInt(input);
            case "repeat" -> rewatches = Integer.parseInt(input);
            case "my_start_date" -> {
                input = input.replace("null","0");
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = f.parse(input);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                startDate = d.getTime();
                animeValues.put(type,input);
            }
            case "my_finish_date" -> {
                input = input.replace("null","0");
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = f.parse(input);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                endDate = d.getTime();
                animeValues.put(type,input);
            }
            case "series_start" -> {
                input = input.replace("null","0");
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = f.parse(input);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                showStartDate = d.getTime();
                animeValues.put(type,input);
            }
            case "series_end" -> {
                input = input.replace("null","0");
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = f.parse(input);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                showEndDate = d.getTime();
                animeValues.put(type,input);
            }
            default -> animeValues.put(type,input);

        }
    }
    public long getEndDate() {


        if (endDate > 0) {
            return endDate;
        }
        if (animestatus==status.CURRENT) {
            return HelloApplication.launchTime;
        }
        return getStartDate();
    }
    public long getStartDate() {
        return startDate <= 0 ? Long.MAX_VALUE : startDate;
    }

    public int getRewatches() {
        return rewatches;
    }

    public long getShowEndDate() {
        return showEndDate<= 0 ? getShowStartDate() : showEndDate;
    }
    public long getShowStartDate() {
        return showStartDate <= 0 ? Long.MAX_VALUE : showStartDate;
    }
    public int getTimeWatching() {

        return (int)Math.ceil(MenuAnimeTimeline.getAnimeDayDifference(startDate,endDate));
    }

    public double getWatchPace() {

        int timeWatching = getTimeWatching()+1;
        if (timeWatching <=0) {
            timeWatching=Integer.MAX_VALUE;
        }

        return ((episodes*1.0)/Math.max(timeWatching,1.0));
    }

    public int getEpisodes() {
        return episodes;
    }
    public int getWatchedEpisodes() {
        return watchedEpisodes;
    }
    public int getScore() {
        return (int)(score*10);
    }

    public Color getScoreColor() {
        if (score==0) {
            return Color.GREY;
        }

        //if (this.animestatus==status.PAUSED || this.animestatus==status.DROPPED || ) {

     //   }


        return getColorFromScore(score);

    }
    public int getValueInt(String type) {
        return switch(type) {
            case "series_episodes" -> episodes;
            case "my_watched_episodes" -> watchedEpisodes;
            case "my_score" -> getScore();
            default -> Integer.parseInt(animeValues.getOrDefault(type,"0"));

        };
    }
    public String getValue(String type) {
        return switch(type) {
            case "series_episodes" -> episodes+"";
            case "my_watched_episodes" -> watchedEpisodes+"";
            case "my_score" -> score+"";
            case "color" -> animeValues.getOrDefault(type,"#ffffff");
            default -> animeValues.getOrDefault(type,"-1");

        };
    }
}
