package com.injata.animelist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.hansolo.tilesfx.addons.CanvasSpinner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.ConnectionPendingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


//todo:
//interpolate position when sorting
//score colors
public class HelloApplication extends Application {


    public static boolean imageChanged = false;

    public static String directory = "";

    public static boolean useFile = false;

    public static int CANVAS_WIDTH = 2560;
    public static int CANVAS_HEIGHT = 1440;

    private static boolean indentByTime = true;

    public static Canvas canvas;

    public static boolean choose_file = false;


    public static boolean initialise_zoom = false;



    public static long launchTime;

    public static boolean shouldUpdateFrame = true;

    public static AnimeProfile profile;




    //should add "sequel mode" instead of individual shows it shows the timeline of an anime title including later seasons and ova as part of one timeline log.

    // should add x and y axis selection options, and better grouping options (group by year of release instead of by anime title for example)


    public static MenuPage currentMenu;

    private static Stage mainStage;

    public static HashMap<String, String> textPool = new HashMap<>();



    @Override
    public void start(Stage stage) throws IOException {
        AnimeLog.loadColors();



        launchTime = System.currentTimeMillis();
           GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        CANVAS_WIDTH= Toolkit.getDefaultToolkit().getScreenSize().width;
        CANVAS_HEIGHT= Toolkit.getDefaultToolkit().getScreenSize().height;
        System.out.println( Toolkit.getDefaultToolkit().getScreenSize().width);

        mainStage = stage;

        getDisplay(mainStage);

        mainStage.show();
     /*   Platform.runLater(()->{

                while (shouldUpdateFrame) {
                    try {
                        Thread.sleep(1000);
                        displayTimeline();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }



        });


      */



    /*    stage.setOnCloseRequest(windowEvent -> {
            stage.close();
            errorInfo.setText("");
            getConfig();

            getDisplay(stage);
            stage.show();
        });
        
     */





    }

    private static void updateTextPool(boolean updateGraphics,String key, String text) {
        textPool.put(key,text);

        System.out.println("updated "+key + " to be: " + text);
        if (updateGraphics) {
          displayTimeline();
        }
    }

    private static void getDisplay(Stage stage) {
     //   currentMenu = Menus.getTimelineMenu();
        currentMenu = Menus.getFirstMenu();
        currentMenu.setWidth(CANVAS_WIDTH);
        currentMenu.setHeight(CANVAS_HEIGHT);
        currentMenu.height=CANVAS_HEIGHT;

        HBox pane = new HBox();

        //   System.out.println(queryAnilistAPI());

        canvas = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);






        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                CANVAS_WIDTH = newSceneWidth.intValue();
                canvas.setWidth(CANVAS_WIDTH);
               currentMenu.setWidth(CANVAS_WIDTH);
                displayTimeline();


            }
        });

        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                CANVAS_HEIGHT = newSceneHeight.intValue();

                canvas.setHeight(CANVAS_HEIGHT);

                currentMenu.setHeight(CANVAS_HEIGHT);
                displayTimeline();

            }
        });
        pane.setSpacing(0);
        pane.setAlignment(Pos.BASELINE_CENTER);
        pane.getChildren().add(canvas);


        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);



        stage.setTitle("Anime list");
        Scene scene = new Scene(pane, CANVAS_WIDTH, CANVAS_HEIGHT);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()==KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                stage.setFullScreen(false);
            }
            currentMenu.interactElement(keyEvent.getCode()+"",false,0,0);
            displayTimeline();
        });
        scene.setOnKeyReleased(keyEvent -> {

            currentMenu.interactElement(keyEvent.getCode()+"",true,0,0);
            displayTimeline();
        });

        stage.setScene(scene);




        stage.show();


        displayTimeline();


        canvas.setOnMouseClicked(mouseEvent -> {
            currentMenu.interactElement("",true,mouseEvent.getX(),mouseEvent.getY());
            displayTimeline();
        });
        canvas.getGraphicsContext2D().setImageSmoothing(true);
        canvas.setOnMousePressed(dragEvent -> {
            currentMenu.mouseDown(dragEvent.getX(),dragEvent.getY());
            displayTimeline();
        });
        canvas.setOnMouseDragged(dragEvent -> {






            currentMenu.drag(dragEvent.getX(),dragEvent.getY());

            displayTimeline();
        });

        canvas.setOnMouseReleased(dragEvent -> {
            currentMenu.mouseRelease(dragEvent.getX(),dragEvent.getY());


        });
        /*
        canvas.setOnScrollStarted(scrollEvent -> {
            //       double zoomFactor = scrollEvent.getDeltaY() > 0 ? 1.1 : 0.9;
            //    zoomScale=Math.min(zoomScale*zoomFactor,minZoom);
            //    displayTimeline(profile);

            System.out.println("scroll started");
        });
        canvas.setOnScrollFinished(scrollEvent -> {
            //       double zoomFactor = scrollEvent.getDeltaY() > 0 ? 1.1 : 0.9;
            //    zoomScale=Math.min(zoomScale*zoomFactor,minZoom);
            //    displayTimeline(profile);

            System.out.println("scroll ended");
        });

         */

        canvas.setOnScroll(scrollEvent -> {



            currentMenu.scroll(scrollEvent.getDeltaY(),scrollEvent.getX(),scrollEvent.getY());

            displayTimeline();
        });

        canvas.setOnMouseMoved( mouseEvent -> {
            boolean update = currentMenu.interactElement("",false,mouseEvent.getX(),mouseEvent.getY());
        //    if (dragEvent.getX() < 0) {
          //      moveMouse(new Point((int)CANVAS_WIDTH,(int)dragEvent.getY()));

         //   }
            // x = Math.min(100,x);
            // y = Math.min(100,y);
            if (update) {
                displayTimeline();

            }
        });


//        displayScoreDistribution(profile);
        stage.setMaximized(true);



    }






    public static void actionButton(String action, MenuElement element) {

        String command = action.split(":")[0];

        String input = action.split(":").length > 1 ? action.split(":")[1] : "";
        switch (command) {
            case "back" ->{
                currentMenu = Menus.getFirstMenu();
           //    getDisplay(mainStage);
            }
            case "sort" ->{

                profile.orderList(input,  textPool.getOrDefault("sort_reverse","1").equals("1"));
                //    getDisplay(mainStage);
            }
            case "updateList" ->{

                profile.orderList(textPool.getOrDefault("sort","start"),  textPool.getOrDefault("sort_reverse","1").equals("1"));
            }
            case "timeline" ->{
                updateTextPool(true,"Error", "fetching Anilist profile");

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        MenuAnimeTimeline timeline= new MenuAnimeTimeline(0,0, MenuDirections.TOP_LEFT);
                        timeline.height =Integer.MAX_VALUE;

                        String profileText = queryAnilistAPI(input);
                        if (profileText.charAt(0) == '!') {
                            updateTextPool(true, "Error", profileText);

                        } else {
                            updateTextPool(true, "Error", "compiling information");
                            profile = getAnimeProfileJson(profileText, input);
                            timeline.profile = profile;
                            updateTextPool(true, "Error", "displaying graphics");
                            currentMenu = Menus.getTimelineMenu();
                            currentMenu.addElement(timeline);
                            profile.orderList("start", textPool.getOrDefault("sort_reverse","1").equals("1"));
                            textPool.put("sort","start");
                            updateTextPool(true, "Error", "");
                        }


                    }
            });

            thread.start();





                //    getDisplay(mainStage);
            }
            default ->{

            }
        }
    }


    public static double getCanvasWidth() {
        return CANVAS_WIDTH;
    }
    public static double getCanvasHeight() {
        return CANVAS_HEIGHT;
    }




    private static void userChooseDir(Stage stage) {


        FileChooser choice = new FileChooser();

        File option = choice.showOpenDialog(stage);


        directory= option.getAbsolutePath();



    }


    public static void displayTimeline() {
     //   Platform.runLater(()-> {
            canvas.getGraphicsContext2D().setFill(Color.BLACK);
            canvas.getGraphicsContext2D().fillRect(0, 0, HelloApplication.CANVAS_WIDTH, HelloApplication.CANVAS_HEIGHT);


        currentMenu.drawElement(canvas.getGraphicsContext2D());
     //   });
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
            updateTextPool(true,"Error","Anilist connection established.\nRequesting information...");

         //   con.connect();



            OutputStream out = con.getOutputStream();
           // System.out.println(json.toString());
            out.write(json.toString().getBytes());

            int statusCode = con.getResponseCode();

            InputStream is = null;

            if (statusCode >=200 && statusCode < 400) {
                is = con.getInputStream();
                System.out.println("managed to get input");

            } else {

                is = con.getErrorStream();
                System.out.println("Response Code:"
                        + con.getResponseCode());
                System.out.println(
                        "Response Message:"
                                + con.getResponseMessage());
        //        return "Response Code:\n"
          //              + con.getResponseCode()+"\n"+ "Response Message:\n" + con.getResponseMessage();
            }


            updateTextPool(true,"Error","Anilist API request successful.\nCompiling information...");



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
           //     File config = new File(username + "_config.txt");
            //    FileWriter fileWriter2 = new FileWriter(file);
           //     fileWriter.write(response.toString().replace("{","\n{"));
              //  fileWriter.close();
                return response.toString().replace("{","\n{");
            } else {
                updateTextPool(true,"Error",response.toString().replace("{","\n{"));
                return "!"+response.toString().replace("{","\n{");
            }


        } catch (IOException e) {

            e.printStackTrace();
            updateTextPool(true,"Error","IO Exception. please contact Injata (the dev):\n" + e.getMessage());
            return "IO Exception. please contact Injata (the dev):\n" + e.getMessage();
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
                    log.setValue("repeat",animeLogJson.get("repeat").asText());

                    log.setValue("series_episodes",animeLogJson.get("media").get("episodes").intValue()+"");
                    log.setValue("my_status",animeLogJson.get("status").textValue());

                    JsonNode startedAt = animeLogJson.get("startedAt");
                    log.setValue("my_start_date",startedAt.get("year").intValue()+"-"+startedAt.get("month").intValue()+"-"+startedAt.get("day").intValue());
                    JsonNode completedAt = animeLogJson.get("completedAt");
                    log.setValue("my_finish_date",completedAt.get("year").intValue()+"-"+completedAt.get("month").intValue()+"-"+completedAt.get("day").intValue());

                    JsonNode series_start = animeLogJson.get("media").get("startDate");
                    log.setValue("series_start",series_start.get("year").intValue()+"-"+series_start.get("month").intValue()+"-"+series_start.get("day").intValue());
                    JsonNode series_end = animeLogJson.get("media").get("endDate");
                    log.setValue("series_end",series_end.get("year").intValue()+"-"+series_end.get("month").intValue()+"-"+series_end.get("day").intValue());



                    log.setValue("series_title_english",animeLogJson.get("media").get("title").get("english").textValue());
                    if (log.getValue("series_title_english").equals("-1")) {
                        log.setValue("series_title_english",animeLogJson.get("media").get("title").get("romaji").textValue());
                    }

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
