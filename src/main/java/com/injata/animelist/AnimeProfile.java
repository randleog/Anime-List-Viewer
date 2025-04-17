package com.injata.animelist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AnimeProfile {

    public HashMap<String, String> profileValues;


    private ArrayList<AnimeLog> animes;

    public long startDate = Long.MAX_VALUE;

    public long endDate = 0;

    public String startDateString = "";
    public String endDateString = "";

    public void addAnime(AnimeLog anime) {

        animes.add(anime);
        long startDateValue = anime.startDate;
        if (startDateValue < startDate && !(startDateValue <= 0) && startDateValue !=Long.MAX_VALUE) {
            startDate = startDateValue;
            startDateString = anime.getValue("my_start_date");
        }
        long endDateValue = anime.getEndDate();
        if (endDateValue > endDate && !(endDateValue <= 0) && endDateValue !=Long.MAX_VALUE) {
            endDate = endDateValue;
        }
    }

    public ArrayList<AnimeLog> getAnimes() {
        return animes;
    }


    public static String getSingluarValue(String input, String key) {
        String[] output = input.replace("</","<").split("<"+key+">");

        if (output.length > 1) {
            return input.replace("</","<").split("<"+key+">")[1];
        } else {
            return "-1";
        }

    }

    public AnimeProfile() {
        profileValues = new HashMap<>();
        animes = new ArrayList<>();
    }
    public AnimeProfile(String... values) {
        profileValues = new HashMap<>();
        animes = new ArrayList<>();
    }

    public void setProfileValue(String key, String value) {
        profileValues.put(key,getSingluarValue(value,key));
    }

    public String getTitleCard() {
        String output = profileValues.get("user_name") + "   watching:"
                + profileValues.get("user_total_watching")
                + "   completed:" + profileValues.get("user_total_completed")
                + "   dropped:" + profileValues.get("user_total_dropped")
            + "   on hold:" + profileValues.get("user_total_onhold")
           + "   planning:" + profileValues.get("user_total_plantowatch");
      //  for (String s :profileValues.keySet()) {
       //     output = output + s + ":" + profileValues.get(s) + " ";
       // }

        return output;
    }

    public String getDisplayString() {
        String output = profileValues.getOrDefault("user_name","-1");
        for (AnimeLog a : animes) {
            output = output + a.getDisplayString() + "\n";
        }
        return output;
    }

    public void orderList(String order, boolean asc) {
        switch (order) {
            case "finish" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getEndDate) : Comparator.comparing(AnimeLog::getEndDate).reversed());
            case "score" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getScore): Comparator.comparing(AnimeLog::getScore).reversed());


        }
    }

    public List<AnimeLog> getList() {

        return animes;

    }

}
