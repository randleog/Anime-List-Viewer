package com.injata.animelist;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimeProfile {

    public HashMap<String, String> profileValues;


    public ArrayList<AnimeLog> animes;




    public AnimeProfile() {
        profileValues = new HashMap<>();
        animes = new ArrayList<>();
    }
    public AnimeProfile(String... values) {
        profileValues = new HashMap<>();
        animes = new ArrayList<>();
    }

    public String getDisplayString() {
        String output = profileValues.getOrDefault("user_name","-1");
        for (AnimeLog a : animes) {
            output = output + a.getDisplayString() + "\n";
        }
        return output;
    }

}
