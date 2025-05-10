package com.injata.animelist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AnimeProfile {

    public HashMap<String,Double> dayLog = new HashMap<>();

    public HashMap<String, String> profileValues;


    private ArrayList<AnimeLog> animes;

    private ArrayList<AnimeLog> bufferAnimes;

    public long startDate = Long.MAX_VALUE;

    public long endDate = 0;

    public String startDateString = "";
    public String endDateString = "";

    public ArrayList<String> lists;

    public boolean includePlanning = false;
    public boolean includePausedDropped = false;

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
       // System.out.println((anime.getStartDate()-anime.getEndDate())/864000000);

    }


    public void applyLineGraph() {
        for (AnimeLog anime : animes) {
            if (anime.getEndDate() - anime.getStartDate() > 0) {
                int start =  (int) ((anime.getStartDate()-startDate) / 86400000);
                int end = (int)((anime.getEndDate() -startDate)/ 86400000);
                for (int i = start; i <= end; i++) {
                    String key = i + "_pace";
                     dayLog.put(key,dayLog.getOrDefault(key,0.0)+anime.getWatchPace()*anime.getDuration());

                    key = i + "_count";
                    dayLog.put(key,dayLog.getOrDefault(key,0.0)+1);

                }
            }
        }
    }


    public void applyFilter(String order, boolean asc) {
        if (bufferAnimes.size() < animes.size()) {
            bufferAnimes = new ArrayList<>();
            bufferAnimes.addAll(animes);
        }
        orderList(order, asc, bufferAnimes);
        animes = new ArrayList<>();
        int y=0;
        for (AnimeLog anime : bufferAnimes) {
            if (!HelloApplication.textPool.getOrDefault("includestatus_"+anime.animestatus.textValue(),"1").equals("0")) {
                y+= (int) MenuAnimeTimeline.gap;
             //   System.out.println("diff: " + anime.y + " " + y);
                anime.updateOrder(y);

                animes.add(anime);
            }
        }
        HelloApplication.drawBriefly();
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
        bufferAnimes= new ArrayList<>();
        profileValues = new HashMap<>();
        animes = new ArrayList<>();
        lists = new ArrayList<>();
    }
    public AnimeProfile(String... values) {
        profileValues = new HashMap<>();
        bufferAnimes= new ArrayList<>();
        lists = new ArrayList<>();
        animes = new ArrayList<>();
    }

    public void setProfileValueXML(String key, String value) {
        setProfileValue(key,getSingluarValue(value,key));

    }//to use when parsing xml

    public void setProfileValue(String key, String value) {
        if (key.contains("user_total_") && !lists.contains(value)) {
            lists.add(value);

        }
          profileValues.put(key, value);
    }




    public void addProfileValue(String key, String value) {
        if (key.contains("user_total_") && !lists.contains(value)) {
            lists.add(value);
            System.out.println(value);
        }
        profileValues.put(key,
                (Integer.parseInt(profileValues.getOrDefault(key
                        ,"0"
                ))+1)+"");


    }


    public String getTitleCard() {
        String output = profileValues.get("user_name") ;

        for (String s : lists) {
            output = output + ", " + s + ":"+profileValues.get("user_total_"+s);
        }
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
        System.out.println(order );

     //   switch (order) {
       //     case "finish" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getEndDate) : Comparator.comparing(AnimeLog::getEndDate).reversed());
      //      case "start" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getStartDate) : Comparator.comparing(AnimeLog::getStartDate).reversed());
     //       case "score" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getScore): Comparator.comparing(AnimeLog::getScore).reversed());
     //       case "timewatching" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getTimeWatching): Comparator.comparing(AnimeLog::getTimeWatching).reversed());
     //       case "episodes" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getEpisodes): Comparator.comparing(AnimeLog::getEpisodes).reversed());
     //       case "rewatches" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getRewatches): Comparator.comparing(AnimeLog::getRewatches).reversed());
      //      case "pace" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getWatchPace): Comparator.comparing(AnimeLog::getWatchPace).reversed());
    //    }
        applyFilter(order, asc);

    }
    public void orderList(String order, boolean asc, ArrayList<AnimeLog> animes) {
        System.out.println(order );

        switch (order) {
            case "finish" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getEndDate) : Comparator.comparing(AnimeLog::getEndDate).reversed());
            case "start" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getStartDate) : Comparator.comparing(AnimeLog::getStartDate).reversed());
            case "score" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getScore): Comparator.comparing(AnimeLog::getScore).reversed());
            case "timewatching" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getTimeWatching): Comparator.comparing(AnimeLog::getTimeWatching).reversed());
            case "episodes" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getEpisodes): Comparator.comparing(AnimeLog::getEpisodes).reversed());
            case "rewatches" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getRewatches): Comparator.comparing(AnimeLog::getRewatches).reversed());
            case "pace" -> animes.sort(asc ? Comparator.comparing(AnimeLog::getWatchPace): Comparator.comparing(AnimeLog::getWatchPace).reversed());
        }
       // applyFilter();

    }
    public List<AnimeLog> getList() {

        return animes;

    }

}
