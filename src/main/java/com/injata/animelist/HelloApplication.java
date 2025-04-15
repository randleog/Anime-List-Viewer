package com.injata.animelist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class HelloApplication extends Application {

    public static String directory = "";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Anime list");
        stage.setScene(scene);
        stage.show();


        System.out.println(getAnimeProfile(stringAnilistFile("scrape_anilistanime.xml")).getDisplayString());

    }

    public static AnimeProfile getAnimeProfile(String input) throws IOException {

        AnimeProfile profile = new AnimeProfile();

        String[] output = input.split("<anime>");
        profile.profileValues.put("user_name",getSingluarValue(output[0],"user_name"));

        for (int i = 1; i < output.length; i++) {


            AnimeLog anime = new AnimeLog();
            anime.setValue("series_title", getAnimeTitle(output[i]));
            anime.setValue("series_episodes", getSingluarValue(output[i], "series_episodes"));
            anime.setValue("my_watched_episodes", getSingluarValue(output[i], "my_watched_episodes"));
            anime.setValue("my_score", getSingluarValue(output[i], "my_score"));
            anime.setValue("my_start_date", getSingluarValue(output[i], "my_start_date"));
            anime.setValue("my_finish_date", getSingluarValue(output[i], "my_finish_date"));
            anime.setValue("my_status", getSingluarValue(output[i], "my_status"));
            anime.setValue("my_comments", getSingluarValue(output[i], "my_comments"));
            anime.setValue("my_times_watched", getSingluarValue(output[i], "my_times_watched"));

            profile.animes.add(anime);



        }
        return profile;
    }


    public static String[] listStep1(String input) throws IOException {

        String[] output = input.split("<anime>");

        for (int i = 0; i < output.length; i++) {
            output[i] = output[i].split("</anime>")[0];



        }





        return output;
    }

    public static String getSingluarValue(String input, String key) {
        String[] output = input.replace("</","<").split("<"+key+">");

        if (output.length > 1) {
            return input.replace("</","<").split("<"+key+">")[1];
        } else {
            return "-1";
        }

    }



    public static String stringAnilistFile(String input) throws IOException {
        return new String(Files.readAllBytes(Paths.get(input)));
    }


    public static String stringFromArray(String[] input) {
        String output = "";
        for (int i = 0; i < input.length; i++) {
            output = output + " " + i + " " + input[i] + "\n";
        }
        return output;
    }
    public static String getAnimeTitle(String input) {
        return  unWrapCDATA(getSingluarValue(input,"series_title"));
    }
    public static String unWrapCDATA(String input) {
        return input.substring(9).split("]]>")[0];
    }

    public static String[] outputAnilistFile(String input) {
        File config = new File(input);
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        BufferedReader reader = null;


        try {
            reader = new BufferedReader(new FileReader(config));
            String line = null;
            while ((line = reader.readLine()) != null) {

            }


        } catch (IOException ignored) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignored) {

                }
            }
        }


        return new String[]{""};
    }

    public static void main(String[] args) {
        launch();
    }



}