package com.injata.animelist;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
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
import netscape.javascript.JSObject;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {



    public static String directory = "";

    public static boolean useFile = false;

    public static int CANVAS_WIDTH = 2560;
    public static int CANVAS_HEIGHT = 1440;

    private static boolean indentByTime = true;

    public static Canvas canvas;

    public static boolean choose_file = false;


    public static boolean initialise_zoom = false;

    public static double minZoom = 3;


    public static double minZoomImage = 1.5;

    public static long min_interval_load_image = 100;//need to do the same but for querying the api in general as well. however, that would be with the config saving the last query, so we dont question more than every 2 minutes
    public static long lastImageLoad = 0;


    //for xml formatting, use https://malscraper.azurewebsites.net/ and select anilist list type and enter your username.

    public static AnimeProfile profile;
    @Override
    public void start(Stage stage) throws IOException {
     //   GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
      //  CANVAS_WIDTH= Toolkit.getDefaultToolkit().getScreenSize().width;
        //CANVAS_HEIGHT= Toolkit.getDefaultToolkit().getScreenSize().height;
     //   System.out.println( Toolkit.getDefaultToolkit().getScreenSize().width);
        HBox pane = new HBox();

     //   System.out.println(queryAnilistAPI());

        canvas = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);

        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                CANVAS_WIDTH = newSceneWidth.intValue();
                canvas.setWidth(CANVAS_WIDTH);
                displayTimeline(profile);
            }
        });

        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                CANVAS_HEIGHT = newSceneHeight.intValue();
                System.out.println("set canvas height to " + CANVAS_HEIGHT);
                canvas.setHeight(CANVAS_HEIGHT);
                displayTimeline(profile);
            }
        });
        pane.setSpacing(0);
        pane.setAlignment(Pos.BASELINE_CENTER);
        pane.getChildren().add(canvas);


        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);



        stage.setTitle("Anime list");
        Scene scene = new Scene(pane, CANVAS_WIDTH, CANVAS_HEIGHT);

        stage.setScene(scene);
        if ( choose_file) {
            if (directory.isEmpty()) {
                userChooseDir(stage);

            }

            profile = getAnimeProfileXML(stringAnilistFile(directory));
        } else {
          //  profile =getAnimeProfileJson(queryAnilistAPI("Injata"),"Injata");
            profile = getAnimeProfileJson(stringAnilistFile("Injata_list.txt"), "Injata");
        }
        stage.show();



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
           // x = Math.min(100,x);
           // y = Math.min(100,y);
            displayTimeline(profile);
        });

        canvas.setOnMouseReleased(dragEvent -> {
            dragX = dragEvent.getX();
            dragY = dragEvent.getY();


        });
        canvas.setOnScroll(scrollEvent -> {
            zoomScale=Math.min(zoomScale*(scrollEvent.getDeltaY() > 0 ? 1.1 : 0.9),minZoom);
            displayTimeline(profile);
        });
//        displayScoreDistribution(profile);
        stage.setMaximized(true);
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

    public static double gap = 75;

    public static double determinedDiff = 17.28;

    public static double imageWidth = 1.03*gap/1.5;
    public static double imageHeight = gap*0.95;

    public static double getRelativeValue(long start, long end) {
       return ((end-start)/5000000.0);
    }


    public static void displayTimeline(AnimeProfile profile) {
        if (indentByTime) {

            if (!initialise_zoom) {
                initialise_zoom = true;

                double divisionValue = (4882812) * canvas.getWidth();
                zoomScale = 1.0 / ((profile.endDate - profile.startDate) / divisionValue);
                System.out.println("initialised zoom to " + zoomScale);
            }
        }

        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);

        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().setFont(Font.font("monospace",10*zoomScale));


   //     double yearFactor = 1000;//and remember to remove the differential between the start of the year and when is started watching anime
     //   for (int i = 0; i < 5; i++) {
     //       canvas.getGraphz

        int lines = 0;
        for (AnimeLog log : profile.getAnimes()) {
            double xv = x*zoomScale;

            lines++;


            double yv = gap*zoomScale+y*zoomScale+lines*zoomScale*gap;

            if (indentByTime) {

                canvas.getGraphicsContext2D().setFill(log.getScoreColor());
                canvas.getGraphicsContext2D().fillRect(xv
                        ,yv+zoomScale*(gap/4)+zoomScale*5.5,
                        getRelativeValue(profile.startDate,log.getEndDate())*zoomScale+zoomScale*1000,
                        2*zoomScale);
              //  canvas.getGraphicsContext2D().setFill(Color.WHITE);

                canvas.getGraphicsContext2D().fillText((int)Math.ceil(getAnimeDayDifference(log.startDate,log.endDate)+1)+"d, " +log.getValue("series_episodes") + " episodes"
                        ,zoomScale*910+xv+getRelativeValue(profile.startDate, log.getEndDate())*zoomScale+zoomScale*determinedDiff
                        ,yv+zoomScale*(gap/4));

                canvas.getGraphicsContext2D().fillRect(zoomScale*1000+xv+getRelativeValue(profile.startDate, log.getStartDate())*zoomScale
                        ,yv+zoomScale*(gap/4)+zoomScale*1.5,
                        Math.max(getRelativeValue(log.getStartDate(),log.getEndDate())*zoomScale,zoomScale)+zoomScale*determinedDiff,
                        10*zoomScale);

                double startPointX = getRelativeValue(profile.startDate, log.getShowStartDate());
                double endPointX = getRelativeValue(profile.startDate, log.getShowEndDate());

                canvas.getGraphicsContext2D().fillRect(zoomScale*1000+xv+startPointX*zoomScale
                        ,yv+zoomScale*(gap/4)+zoomScale*1.5,
                        zoomScale*1,
                        10*zoomScale);

                canvas.getGraphicsContext2D().fillRect(zoomScale*1000+xv+endPointX*zoomScale
                        ,yv+zoomScale*(gap/4)+zoomScale*1.5,
                        zoomScale*1,
                        10*zoomScale);
             //   if ()

              //  System.out.println(zoomScale*1000+xv+getRelativeValue(profile.startDate, log.getStartDate()));

                double day =getRelativeValue(profile.startDate,log.getEndDate())/determinedDiff+ x/determinedDiff-imageWidth/determinedDiff+1000/determinedDiff;//getAnimeDayDifference(profile.startDate,log.getEndDate());
                if (day+1 <0) {
                    canvas.getGraphicsContext2D().fillText("< "+String.format("%.02f", day+1)+"d",60*zoomScale
                            ,yv+zoomScale*(gap/4));
                } else {
                    canvas.getGraphicsContext2D().fillText(String.format("%.02f", day+1)+"d >",60*zoomScale
                            ,yv+zoomScale*(gap/4));
                }




                canvas.getGraphicsContext2D().setFill(Color.WHITE);

                canvas.getGraphicsContext2D().fillRect(zoomScale*1000+xv+startPointX*zoomScale
                        ,yv+zoomScale*(gap/4)+zoomScale*1.5,
                        zoomScale*1,
                        10*zoomScale);
                canvas.getGraphicsContext2D().fillText("air date: "+ log.getValue("series_start"), zoomScale*920+xv+startPointX*zoomScale,yv+zoomScale*(gap/4)+zoomScale*21);
                if (endPointX != startPointX) {
                    canvas.getGraphicsContext2D().fillRect(zoomScale * 1000 + xv + endPointX * zoomScale
                            , yv + zoomScale * (gap / 4) + zoomScale * 1.5,
                            zoomScale * 1,
                            10 * zoomScale);
                    canvas.getGraphicsContext2D().fillText("end date: " + log.getValue("series_end"), zoomScale * 920 + xv + endPointX * zoomScale, yv + zoomScale * (gap / 4) + zoomScale * 21);
                }

            }




            if (!(xv > CANVAS_WIDTH || yv > CANVAS_HEIGHT)) {
                if (minZoomImage <zoomScale) {
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
                    canvas.getGraphicsContext2D().setFill(Color.valueOf(log.getValue("color")));
                    canvas.getGraphicsContext2D().fillRect(0, yv-(gap/2)*zoomScale, imageWidth*zoomScale, zoomScale*imageHeight);
                } else {
                    canvas.getGraphicsContext2D().drawImage(log.image, 0, yv-(gap/2)*zoomScale, imageWidth*zoomScale, zoomScale*imageHeight);
                }
                canvas.getGraphicsContext2D().setFill(Color.WHITE);
                canvas.getGraphicsContext2D().fillText(log.getDisplayString(),60*zoomScale,yv);
            }
        }
        canvas.getGraphicsContext2D().setFill(Color.rgb(0,0,0,0.5));
        canvas.getGraphicsContext2D().fillRect(0,0,CANVAS_WIDTH,Math.max(gap*zoomScale,gap*zoomScale+y*zoomScale));
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().setFont(Font.font("monospace", FontWeight.BOLD, FontPosture.REGULAR,20*zoomScale));
        canvas.getGraphicsContext2D().fillText(profile.getTitleCard() ,Math.max(0,x*zoomScale),Math.max(gap*zoomScale,gap*zoomScale+y*zoomScale));
    }

    public static double getAnimeDayDifference(long startdate, long enddate) {
        return (getRelativeValue(startdate, enddate))/determinedDiff;
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


    // @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
//@JsonTypeName("query")
//public class Query {

  //      public String query = "query ($type: MediaType!, $userName: String!) { MediaListCollection(type: ANIME userName: \"Injata\") { lists { name entries { id media { id title { romaji } } } } } }";
//}

    public static String queryAnilistAPI(String username) {
        String query= "query  { MediaListCollection(type: ANIME userName: \"" + username+"\") { lists { name entries { id score repeat progress advancedScores status customLists notes startedAt { day month year } completedAt { day month year} media { id coverImage {extraLarge large medium color}  title { romaji english } episodes startDate {day month year} endDate {day month year} tags { category name } genres meanScore} } } } } ";
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("query",query);

        try {
            URL url = new URL("https://graphql.anilist.co");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Accept","application/json");
            con.setDoOutput(true);
            con.setDoInput(true);

         //   con.connect();



            OutputStream out = con.getOutputStream();
            System.out.println(json.toString());
            out.write(json.toString().getBytes());

            int statusCode = con.getResponseCode();

            InputStream is = null;

            if (statusCode >=200 && statusCode < 400) {
                is = con.getInputStream();
                System.out.println("managed to get input");

            } else {
                is = con.getErrorStream();
            }

            System.out.println("Response Code:"
                    + con.getResponseCode());
            System.out.println(
                    "Response Message:"
                            + con.getResponseMessage());

            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = responseReader.readLine()) != null) {
                response.append(inputLine);
            }
            responseReader.close();
            if (statusCode ==200) {//&& SAVE_FILES_ENABLED
                File file = new File(username + "_list.txt");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(response.toString().replace("{","\n{"));
                fileWriter.close();
            }

            return response.toString().replace("{","\n{");

        } catch (IOException e) {

            e.printStackTrace();
            return "";
        }

    }

    public static AnimeProfile getAnimeProfileJson(String input, String username) {
        AnimeProfile profile = new AnimeProfile();
        try {
            JsonNode node = new ObjectMapper().readTree(input);


            profile.setProfileValue("user_name",username);
         //   profile.setProfileValue("user_total_watching",node.get("data").get("MediaListCollection").get("lists").textValue());
         //   profile.setProfileValue("user_total_completed",output[0]);
        //    profile.setProfileValue("user_total_onhold",output[0]);
        //    profile.setProfileValue("user_total_dropped",output[0]);
        //    profile.setProfileValue("user_total_plantowatch",output[0]);
            HashMap<String, String> hash = new HashMap<>();


       int i = 0;
        while (node.get("data").get("MediaListCollection").get("lists").get(i) != null) {
            int j = 0;
            JsonNode currentNode = node.get("data").get("MediaListCollection").get("lists").get(i);
        //    while (node.get("data").get("MediaListCollection").get("lists").get(i).get(j) != null) {

            while (currentNode.get("entries").get(j)!=null) {
                JsonNode animeLogJson = currentNode.get("entries").get(j);
                String status=node.get("data").get("MediaListCollection").get("lists").get(i).get("name").textValue();

                profile.addProfileValue("user_total_"+status,status);
                if (!hash.containsKey(animeLogJson.get("id").asText())) {
                    hash.put(animeLogJson.get("id").asText(),"true");
                   // System.out.println(animeLogJson.get("media").get("title").get("romaji"));
                    AnimeLog log = new AnimeLog();
                    log.score = Double.parseDouble(animeLogJson.get("score").asText());

                    log.setValue("series_episodes",animeLogJson.get("media").get("episodes").intValue()+"");


                    JsonNode startedAt = animeLogJson.get("startedAt");
                    log.setValue("my_start_date",startedAt.get("year").intValue()+"-"+startedAt.get("month").intValue()+"-"+startedAt.get("day").intValue());
                    JsonNode completedAt = animeLogJson.get("completedAt");
                    log.setValue("my_finish_date",completedAt.get("year").intValue()+"-"+completedAt.get("month").intValue()+"-"+completedAt.get("day").intValue());

                    JsonNode series_start = animeLogJson.get("media").get("startDate");
                    log.setValue("series_start",series_start.get("year").intValue()+"-"+series_start.get("month").intValue()+"-"+series_start.get("day").intValue());
                    JsonNode series_end = animeLogJson.get("media").get("endDate");
                    log.setValue("series_end",series_end.get("year").intValue()+"-"+series_end.get("month").intValue()+"-"+series_end.get("day").intValue());



                    log.setValue("series_title_english",animeLogJson.get("media").get("title").get("english").textValue());

                    log.setValue("image",animeLogJson.get("media").get("coverImage").get("medium").textValue());
                    addLogValueIfNotNull(log,animeLogJson.get("media").get("coverImage"),"color");
                    log.rawData = animeLogJson;

                    profile.addAnime(log);
                }
                j++;
            }



       //     }

            i++;

        }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return profile;
    }

    public static void addLogValueIfNotNull(AnimeLog log, JsonNode node, String value) {
        if (node.get(value) == null) {

        } else {

            log.setValue("color", node.get("color").textValue());
        }
    }



    public static AnimeProfile getAnimeProfileXML(String input) throws IOException {

        AnimeProfile profile = new AnimeProfile();

        String[] output = input.split("<anime>");
        profile.setProfileValueXML("user_name",output[0]);
        profile.setProfileValueXML("user_total_watching",output[0]);
        profile.setProfileValueXML("user_total_completed",output[0]);
        profile.setProfileValueXML("user_total_onhold",output[0]);
        profile.setProfileValueXML("user_total_dropped",output[0]);
        profile.setProfileValueXML("user_total_plantowatch",output[0]);

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
