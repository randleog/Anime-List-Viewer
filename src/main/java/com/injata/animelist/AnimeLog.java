package com.injata.animelist;

import java.util.HashMap;

public class AnimeLog {


    public int episodes;

    public int watchedEpisodes;
    public double score;

    public long startDate;
    public long endDate;



    public HashMap<String, String> animeValues;



    public AnimeLog() {
        startDate = 0;
        endDate = 0;
        score =0;
        watchedEpisodes = 0;
        episodes = 0;
        animeValues = new HashMap<>();
    }

    public String getDisplayString() {
        return getValue("series_title") + " total eps:" + getValue("series_episodes")+ " watched:" + getValue("my_watched_episodes")+ " score:"
                + getValue("my_score")+ " status:" +  getValue("my_status");
    }


    public void setValue(String type, String input) {
        switch(type) {
            case "series_episodes" -> episodes = Integer.parseInt(input);
            case "my_watched_episodes" -> watchedEpisodes = Integer.parseInt(input);
            case "my_score" -> score = Integer.parseInt(input);
            default -> animeValues.put(type,input);

        }
    }

    public int getValueInt(String type) {
        return switch(type) {
            case "series_episodes" -> episodes;
            case "my_watched_episodes" -> watchedEpisodes;
            case "my_score" -> (int)(score*10);
            default -> Integer.parseInt(animeValues.getOrDefault(type,"0"));

        };
    }
    public String getValue(String type) {
        return switch(type) {
            case "series_episodes" -> episodes+"";
            case "my_watched_episodes" -> watchedEpisodes+"";
            case "my_score" -> score+"";
            default -> animeValues.getOrDefault(type,"-1");

        };
    }
}
