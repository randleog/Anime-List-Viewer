package com.injata.animelist;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AnimeLog {


    public int episodes;

    public int watchedEpisodes;
    public double score;

    public long startDate;
    public long endDate;


    public long showStartDate;
    public long showEndDate;

    public JsonNode rawData;



    public HashMap<String, String> animeValues;

    public boolean displayEN = true;


    public boolean findingImage = false;
    public Image image;

    public status animestatus;

    public enum status {
        COMPLETED,
        DROPPED,
        PAUSED,
        PLANNING,
        CURRENT

    }


    public AnimeLog() {
        startDate = 0;
        endDate = 0;
        score =0;
        watchedEpisodes = 0;
        episodes = 0;
        animeValues = new HashMap<>();
        animestatus = status.PAUSED;
    }

    public String getDisplayString() {
        return getValue("series_title" + (displayEN ? "_english" : "")) + "\n"
                + getValue("my_score")+ "/10 " +  getValue("my_status") + " " + getValue("my_start_date")+ " to " + getValue("my_finish_date");
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

    public long getShowEndDate() {
        return showEndDate<= 0 ? getShowStartDate() : showEndDate;
    }
    public long getShowStartDate() {
        return showStartDate <= 0 ? Long.MAX_VALUE : showStartDate;
    }

    public int getScore() {
        return (int)(score*10);
    }

    public Color getScoreColor() {
        int v = getScore();

     //   if (getValue("my_status").equals("DROPPED")) {
      //      return Color.DARKGRAY;
     //   }

        //red, orange, yellow, green, blue, pink
        if (v==0) {
            return Color.GRAY;
        }
        if (v <40) {



            return Color.rgb(v*5, 0, 0, 1);

        }

        if (v <=65) {



            return Color.rgb(255, (int)((Math.max(v-30,0))*7.28), 0, 1);

        }

        if (v <= 100) {
            return Color.rgb(0, 255, (int)((v-65)*7.28), 1);
        }
      //  if (v <= 100) {
      //      return Color.rgb(255, 0, 255, 1);
      //  }
        return Color.WHITE;
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
