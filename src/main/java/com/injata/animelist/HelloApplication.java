package com.injata.animelist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class HelloApplication extends Application {



    public static String directory = "";

    public static boolean useFile = false;

    public static int CANVAS_WIDTH = 2560;
    public static int CANVAS_HEIGHT = 1440;

    private static boolean indentByTime = true;

    public static Canvas canvas;

    public static AnimeProfile profile;
    @Override
    public void start(Stage stage) throws IOException {

        HBox pane = new HBox();

        System.out.println(queryAnilistAPI());

        canvas = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);


        pane.setSpacing(0);
        pane.setAlignment(Pos.BASELINE_CENTER);
        pane.getChildren().add(canvas);


        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);



        stage.setTitle("Anime list");
        Scene scene = new Scene(pane, CANVAS_WIDTH, CANVAS_HEIGHT);

        stage.setScene(scene);
        if (directory.isEmpty()) {
            userChooseDir(stage);
        }
        stage.show();



        profile = getAnimeProfile(stringAnilistFile(directory));
        profile.orderList("finish", true);


        //System.out.println(profile.getDisplayString());
        displayTimeline(profile);
        canvas.setOnMousePressed(dragEvent -> {
            dragX = dragEvent.getX();
            dragY = dragEvent.getY();
            dragXB = x;
            dragYB=y;

        });
        canvas.setOnMouseDragged(dragEvent -> {

            x = dragXB-(dragX-dragEvent.getX())/zoomScale;
            y = dragYB-(dragY-dragEvent.getY())/zoomScale;
            x = Math.min(100,x);
            y = Math.min(100,y);
            displayTimeline(profile);
        });

        canvas.setOnMouseReleased(dragEvent -> {
            dragX = dragEvent.getX();
            dragY = dragEvent.getY();


        });
        canvas.setOnScroll(scrollEvent -> {
            zoomScale=Math.min(zoomScale*(scrollEvent.getDeltaY() > 0 ? 1.1 : 0.9),2);
            displayTimeline(profile);
        });
//        displayScoreDistribution(profile);
    }

    private static double zoomScale = 1.0;

    private static double x = 0;
    private static double dragXB = 0;
    private static double dragYB = 0;
    private static double dragX = 0;
    private static double dragY = 0;
    private static double y = 0;






    private static void userChooseDir(Stage stage) {


        FileChooser choice = new FileChooser();

        File option = choice.showOpenDialog(stage);


        directory= option.getAbsolutePath();



    }

    public static double getRelativeValue(long start, long end) {
       return ((end-start)/5000000.0)*zoomScale;
    }


    public static void displayTimeline(AnimeProfile profile) {
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);

        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().setFont(Font.font("monospace",20*zoomScale));




        int lines = 0;
        for (AnimeLog log : profile.getAnimes()) {
            double xv = x*zoomScale;

            lines++;

            double yv = 40*zoomScale+y*zoomScale+lines*zoomScale*40;

            if (indentByTime) {
                canvas.getGraphicsContext2D().setFill(log.getScoreColor());
                canvas.getGraphicsContext2D().fillRect(xv
                        ,yv,
                        getRelativeValue(profile.startDate,log.getEndDate())+3*zoomScale+zoomScale*1000,
                        2*zoomScale);
              //  canvas.getGraphicsContext2D().setFill(Color.WHITE);


                canvas.getGraphicsContext2D().fillRect(zoomScale*1000+xv+getRelativeValue(profile.startDate, log.getStartDate())
                        ,yv,
                        getRelativeValue(log.getStartDate(),log.getEndDate())+10*zoomScale,
                        10*zoomScale);
            }

            canvas.getGraphicsContext2D().setFill(Color.WHITE);
            if (!(xv > CANVAS_WIDTH || yv > CANVAS_HEIGHT)) {
                canvas.getGraphicsContext2D().fillText(log.getDisplayString(),xv,yv);
            }
        }
        canvas.getGraphicsContext2D().setFill(Color.rgb(0,0,0,0.5));
        canvas.getGraphicsContext2D().fillRect(0,0,CANVAS_WIDTH,Math.max(40*zoomScale,40*zoomScale+y*zoomScale));
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().setFont(Font.font("monospace", FontWeight.BOLD, FontPosture.REGULAR,20*zoomScale));
        canvas.getGraphicsContext2D().fillText(profile.getTitleCard() ,Math.max(0,x*zoomScale),Math.max(40*zoomScale,40*zoomScale+y*zoomScale));
    }




    public static void displayScoreDistribution(AnimeProfile profile) {
        int[] ratings = new int[10];
        for (AnimeLog log : profile.getAnimes()) {
            if (log.getValueInt("my_score") > 0) {
            ratings[log.getValueInt("my_score")/10-1]++;
            }
        }
        canvas.getGraphicsContext2D().setFill(Color.WHEAT);
        for (int i = 0; i < ratings.length; i++) {
            canvas.getGraphicsContext2D().fillRect(x*zoomScale+i*zoomScale*ratings.length,y*zoomScale,zoomScale*ratings.length,zoomScale*20*ratings[i]);
        }
    }



    public static String queryAnilistAPI() {
        String query= """
                
                query ($type: MediaType!, $userName: String!) {
                      MediaListCollection(type: ANIME, userName: "Injata") {
                        lists {
                          name
                          entries {
                            id
                            media {
                              id
                              title {
                                romaji
                              }
                            }
                          }
                        }
                      }
                    }
                
                """;
        try {
            String json = "{\"query\":\"";
            json += query;
            json += "\"}";

            json = json.replace("\n", " ").replace("  ", " ");
            URL url = new URL("https://graphql.anilist.co");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.addRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

            in.close();
            conn.disconnect();
            return result;
        } catch (Exception exp) {
            System.out.println("exception TRIGGERED");
            System.out.println(exp.getLocalizedMessage());
            return "";
        }
    }



    public static AnimeProfile getAnimeProfile(String input) throws IOException {

        AnimeProfile profile = new AnimeProfile();

        String[] output = input.split("<anime>");
        profile.setProfileValue("user_name",output[0]);
        profile.setProfileValue("user_total_watching",output[0]);
        profile.setProfileValue("user_total_completed",output[0]);
        profile.setProfileValue("user_total_onhold",output[0]);
        profile.setProfileValue("user_total_dropped",output[0]);
        profile.setProfileValue("user_total_plantowatch",output[0]);

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

            profile.addAnime(anime);



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
        if (args.length > 0) {
            directory = args[0];
        }
        launch();
    }



}