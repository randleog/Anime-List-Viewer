package com.injata.animelist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


//todo:
//https://injata.atlassian.net/jira/software/projects/CPG/list
public class HelloApplication extends Application {

    public static final int MAX_ACTIVITY_COUNT = 100;
    public static boolean imageChanged = false;

    public static String directory = "data" + "/";

    public static boolean useFile = false;
    public static String USER_ACTIVITY_FOLDER = "activity/";
    public static String toolTip = "";

    public static long currentTime = System.currentTimeMillis();

    public static int CANVAS_WIDTH = 2560;
    public static int CANVAS_HEIGHT = 1440;

    private static boolean indentByTime = true;

    public static Canvas canvas;

    public static boolean choose_file = false;

    public static Date currentDate;


    public static boolean initialise_zoom = false;


    public static long launchTime;

    public static boolean shouldUpdateFrame = true;

    public static AnimeProfile profile;


    public static double mouseX = 0;
    public static double mouseY = 0;

    //should add "sequel mode" instead of individual shows it shows the timeline of an anime title including later seasons and ova as part of one timeline log.

    // should add x and y axis selection options, and better grouping options (group by year of release instead of by anime title for example)


    public static MenuPage currentMenu;

    private static Stage mainStage;

    public static HashMap<String, String> textPool = new HashMap<>();


    @Override
    public void start(Stage stage) throws IOException {

        if (!(new File(directory).exists())) {
            new File(directory).mkdir();
        }
        AnimeLog.loadColors();


        launchTime = System.currentTimeMillis();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        CANVAS_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
        CANVAS_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().width);

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


        //mine is 6168637
        //test anime is 108268#
        //  System.out.println(queryAnimeHistory(6168637,108268));

    }

    public static void updateTextPool(boolean updateGraphics, String key, String text) {
        textPool.put(key, text);

        System.out.println("updated " + key + " to be: " + text);
        if (updateGraphics) {
            displayTimeline();
        }
    }

    private static void getDisplay(Stage stage) {
        //   currentMenu = Menus.getTimelineMenu();
        currentMenu = Menus.getFirstMenu();
        currentMenu.setWidth(CANVAS_WIDTH);
        currentMenu.setHeight(CANVAS_HEIGHT);
        currentMenu.height = CANVAS_HEIGHT;

        HBox pane = new HBox();

        //   System.out.println(queryAnilistAPI());

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);


        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                CANVAS_WIDTH = newSceneWidth.intValue();
                canvas.setWidth(CANVAS_WIDTH);
                currentMenu.setWidth(CANVAS_WIDTH);
                displayTimeline();


            }
        });

        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
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
        canvas.getGraphicsContext2D().fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);


        stage.setTitle("Anime list");
        Scene scene = new Scene(pane, CANVAS_WIDTH, CANVAS_HEIGHT);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                stage.setFullScreen(false);
            }
            currentMenu.interactElement(keyEvent.getCode() + "", false, 0, 0);
            displayTimeline();
        });
        scene.setOnKeyReleased(keyEvent -> {

            currentMenu.interactElement(keyEvent.getCode() + "", true, 0, 0);
            displayTimeline();
        });

        stage.setScene(scene);


        stage.show();


        displayTimeline();


        canvas.setOnMouseClicked(mouseEvent -> {
            currentMenu.interactElement("", true, mouseEvent.getX(), mouseEvent.getY());
            displayTimeline();
        });
        canvas.getGraphicsContext2D().setImageSmoothing(true);
        canvas.setOnMousePressed(dragEvent -> {
            currentMenu.mouseDown(dragEvent.getX(), dragEvent.getY());
            displayTimeline();
        });
        canvas.setOnMouseDragged(dragEvent -> {

            mouseX = dragEvent.getX();
            mouseY = dragEvent.getY();


            currentMenu.drag(dragEvent.getX(), dragEvent.getY());

            displayTimeline();
        });

        canvas.setOnMouseReleased(dragEvent -> {
            currentMenu.mouseRelease(dragEvent.getX(), dragEvent.getY());


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

            if (scrollEvent.getDeltaY() == 0) {
                return;
            }
            currentMenu.scroll(scrollEvent.getDeltaY(), scrollEvent.getX(), scrollEvent.getY());

            displayTimeline();
        });

        canvas.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            boolean update = currentMenu.interactElement("", false, mouseEvent.getX(), mouseEvent.getY());
            //    if (dragEvent.getX() < 0) {
            //      moveMouse(new Point((int)CANVAS_WIDTH,(int)dragEvent.getY()));

            //   }
            // x = Math.min(100,x);
            // y = Math.min(100,y);
            //   if (update) {
            displayTimeline();

            //   }
        });


//        displayScoreDistribution(profile);
        stage.setMaximized(true);


    }


    public static long animationSpeed = 15; //16 = 60fps.
    public static long animationLength = 1125;
    //animations should be 60fps in general. higher fps animation is only useful for direct user interaction like drag
    //Target slighly higher than 60fps so no frame drops on bad monitors
    //therefore ideal value = 15


    public static void drawBriefly() {
        long iterationsRequired = animationLength / animationSpeed;
        if (profile == null) {
            return;
        }
        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {

                for (int i = 0; i < iterationsRequired; i++) {
                    try {
                        Thread.sleep(animationSpeed);
                        Platform.runLater(new Runnable() {

                            public void run() {
                                displayTimeline();
                            }
                        });

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

        thread.start();
    }

    private static int searchingIndex = 0;
    private static String lastSearch = "";
    private static ArrayList<AnimeLog> searchResults = new ArrayList<>();

    private static void searchText(String input) {
        String anime = input;
        System.out.println("searching for " + anime);


        lastSearch = anime;
        ArrayList<AnimeLog> list = profile.getAnimes();
        searchResults = new ArrayList<>();
        for (AnimeLog log : list) {
            if (log.getDisplayName().toLowerCase().contains(anime.toLowerCase())) {
                searchResults.add(log);
            }
        }


        if (searchingIndex >= searchResults.size()) {
            searchingIndex = 0;
        }
        if (searchResults.size() < 1) {
            return;

        }
        AnimeLog found = searchResults.get(searchingIndex);
        System.out.println("found " + found.getInfo() + " " + searchingIndex);
        updateTextPool(false, "searchResults", (searchingIndex + 1) + "/" + searchResults.size());
        currentMenu.interactElement("search", false, -found.x, -found.y);
    }


    public static void actionButton(String action, MenuElement element) {

        String command = action.split(":")[0];

        String input = (action.split(":").length > 1 ? action.split(":")[1] : "").toLowerCase();

        switch (command) {
            case "back" -> {
                currentMenu = Menus.getFirstMenu();
                //    getDisplay(mainStage);
            }
            case "sort" -> {

                profile.orderList(input, textPool.getOrDefault("sort_reverse", "1").equals("1"));
                //    getDisplay(mainStage);
            }
            case "updateList" -> {

                profile.orderList(textPool.getOrDefault("sort", "start"), textPool.getOrDefault("sort_reverse", "1").equals("1"));
            }
            case "search_違お" -> {
                if (input.isEmpty()) {
                    updateTextPool(false, "searchResults", "");
                    searchingIndex = 0;
                    searchResults = new ArrayList<>();
                    lastSearch = "";
                } else {
                    searchText(input);
                }


            }
            case "search" -> {
                if (lastSearch.equals(input)) {
                    searchingIndex++;
                }
                searchText(input);


            }
            case "timeline" -> {
                updateTextPool(true, "Error", "fetching Anilist profile");

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        MenuAnimeTimeline timeline = new MenuAnimeTimeline(0, 0, MenuDirections.TOP_LEFT);
                        timeline.height = Integer.MAX_VALUE;
                        String profileText = queryAnilistAPI(input);

                        if (profileText.charAt(0) == '!') {
                            updateTextPool(true, "Error", profileText);

                        } else {
                            ///if manga = true:
                            updateTextPool(true, "Error", "getting Manga");
                            AnimeProfile mangaProfile = getAnimeProfileJson(queryAnilistAPIManga(input), input, "Manga: ");


                            updateTextPool(true, "Error", "compiling information");
                            profile = getAnimeProfileJson(profileText, input, "");
                            for (AnimeLog anime : mangaProfile.getAnimes()) {

                                profile.addAnime(anime);
                            }

                            profile.applyLineGraph();
                            timeline.setProfile(profile);
                            updateTextPool(true, "Error", "displaying graphics");
                            currentMenu = Menus.getTimelineMenu();
                            currentMenu.addElement(timeline);
                            currentMenu.addElement(new MenuButton("back", "back", 0, 0, 150, 100, MenuDirections.BOTTOM_LEFT, false, "back"));

                            profile.orderList("start", textPool.getOrDefault("sort_reverse", "1").equals("1"));
                            textPool.put("sort", "start");
                            updateTextPool(true, "Error", "");


                        }


                        startLoadingHistory();
                    }
                });

                thread.start();


                //    getDisplay(mainStage);
            }
            default -> {

            }
        }
    }


    public static double getCanvasWidth() {
        return CANVAS_WIDTH;
    }

    public static double getCanvasHeight() {
        return CANVAS_HEIGHT;
    }


    static double timeWaiting = 3000;

    public static void startLoadingHistory() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                for (Integer[] val : waitingToLoadHistory) {
                    try {
                        Thread.sleep((long) timeWaiting);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    queryAnimeHistory(val[0], val[1], val[2] == 1);
                    progress++;
                    System.out.println("loaded the activity history for: " +progress + "/" +waitingToLoadHistory.size() + ". " + (waitingToLoadHistory.size()-progress)*timeWaiting/1000 + " seconds left");
                }

            }
        });
        t.start();
    }


    private static void userChooseDir(Stage stage) {


        FileChooser choice = new FileChooser();

        File option = choice.showOpenDialog(stage);


        directory = option.getAbsolutePath();


    }


    public static void displayTimeline() {
        currentTime = System.currentTimeMillis();
        currentDate = new Date(currentTime);
        //   Platform.runLater(()-> {
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0, 0, HelloApplication.CANVAS_WIDTH, HelloApplication.CANVAS_HEIGHT);


        currentMenu.drawElement(canvas.getGraphicsContext2D());

        if (toolTip.length() > 0) {
            canvas.getGraphicsContext2D().setFill(Color.BLACK);
            canvas.getGraphicsContext2D().fillRect(mouseX, mouseY, 100, 100);
            canvas.getGraphicsContext2D().setFill(Color.WHITE);
            //   canvas.getGraphicsContext2D().setFont(Font.font("monso"));
            canvas.getGraphicsContext2D().fillText(toolTip, mouseX, mouseY);
        }


        //   });
    }


    // @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
//@JsonTypeName("query")
//public class Query {

    //      public String query = "query ($type: MediaType!, $userName: String!) { MediaListCollection(type: ANIME userName: \"Injata\") { lists { name entries { id media { id title { romaji } } } } } }";
//}

    public static String queryAnilistAPIManga(String username) {
        String query = "query  { MediaListCollection(type: MANGA userName: \"" + username + "\") { user {id} lists { name entries { id score repeat progress advancedScores status customLists notes startedAt { day month year } completedAt { day month year} media { id coverImage {extraLarge large medium color} format title { romaji english } chapters duration startDate {day month year} endDate {day month year} tags { category name } genres meanScore} } } } } ";
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("query", query);

        try {
            URL url = new URL("https://graphql.anilist.co");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
            updateTextPool(true, "Error", "Anilist connection established.\nRequesting information...");

            //   con.connect();


            OutputStream out = con.getOutputStream();
            // System.out.println(json.toString());
            out.write(json.toString().getBytes());

            int statusCode = con.getResponseCode();

            InputStream is = null;

            if (statusCode >= 200 && statusCode < 400) {
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


            updateTextPool(true, "Error", "Anilist API request successful.\nCompiling information...");


            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = responseReader.readLine()) != null) {
                response.append(inputLine);
            }
            responseReader.close();
            if (statusCode == 200) {//&& SAVE_FILES_ENABLED
                File file = new File(directory + username + "_Mangalist.txt");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(response.toString().replace("{", "\n{"));
                fileWriter.close();
                //     File config = new File(username + "_config.txt");
                //    FileWriter fileWriter2 = new FileWriter(file);
                //     fileWriter.write(response.toString().replace("{","\n{"));
                //  fileWriter.close();
                return response.toString().replace("{", "\n{");
            } else {
                updateTextPool(true, "Error", response.toString().replace("{", "\n{"));
                return "!" + response.toString().replace("{", "\n{");
            }


        } catch (IOException e) {

            e.printStackTrace();
            updateTextPool(true, "Error", "IO Exception. please contact Injata (the dev):\n" + e.getMessage());
            return "IO Exception. please contact Injata (the dev):\n" + e.getMessage();
        }

    }


    private static long lastActivityQuery = 0;


    private static ArrayList<Integer[]> waitingToLoadHistory = new ArrayList<>();
    //query for finding the anime activity history of a specific anime:
    /* Page(page:0,perPage: 100) { activities(userId: 6168637, type: ANIME_LIST, mediaId: 108268) { ... on ListActivity { media { id title { english } } createdAt likeCount progress status type siteUrl}} }}

     */

    //checklist:
    //per anime, i can query the most recent 100 activity logs of that user
    //as part of the main query, i have added info about the id of the anime, and the anime relations (sequel and prequel)
    //
    //#1 I should next save the id per anime to the animelog
    //#2 then i should use delayed saving to slowly collect the info about each anime watch history.
    //#3 i need to add a setting to show anime only per franchise so you have the full list of logs in one line
    public static String queryAnimeHistory(int userId, int animeId, boolean ismanga) {

        boolean shouldUseFile = false;
        String fileName =directory + userId + "/" + animeId + "_list.txt";
        if (System.currentTimeMillis() - lastActivityQuery < 1000) {
            shouldUseFile = new File(fileName).exists();
            if (!shouldUseFile) {
                waitingToLoadHistory.add(new Integer[]{userId, animeId, ismanga ? 1 : 0});
                System.out.println("not loading the file due to time ");
                return null;
            }

        } else {
            shouldUseFile = new File(fileName).exists();

            //todo: come back to this part at some point. you might want to refresh the anime activity on occasion in case you missed an activity.
            if (new File(fileName).exists()) {
                //shouldUseFile = new File(userId + "/" + animeId + "_list.txt").lastModified() > System.currentTimeMillis() - 10000000;
            }

        }


        if (shouldUseFile) {

            try {
                //    FileReader filereader = new FileReader(file);
                String value = Files.readString(Path.of(userId + "/" + animeId + "_list.txt"), StandardCharsets.UTF_8);

                return value;

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        lastActivityQuery = System.currentTimeMillis();
        String type = ismanga ? "MANGA_LIST" : "ANIME_LIST";


        String query = "query  {\n" +
                "  Page(page: 0, perPage: " + MAX_ACTIVITY_COUNT + " ) {\n" +
                "    activities(userId: " + userId + ", type: " + type + ", mediaId:  " + animeId + ") {\n" +
                "      ... on ListActivity {\n" +
                "        media { id title { english }" +
                "        }\n" +
                "        createdAt\n" +
                "        likeCount\n" +
                "        progress\n" +
                "        status\n" +
                "        type\n" +
                "        siteUrl\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("query", query);


        try {
            URL url = new URL("https://graphql.anilist.co");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
            updateTextPool(true, "Error", "Anilist connection established.\nRequesting information...");

            //   con.connect();


            OutputStream out = con.getOutputStream();
            // System.out.println(json.toString());
            out.write(json.toString().getBytes());

            int statusCode = con.getResponseCode();

            InputStream is = null;

            if (statusCode >= 200 && statusCode < 400) {
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


            updateTextPool(true, "Error", "Anilist API request successful.\nCompiling information...");


            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = responseReader.readLine()) != null) {
                response.append(inputLine);
            }
            responseReader.close();
            String filename = directory  + userId + "/";
            if (statusCode == 200) {//&& SAVE_FILES_ENABLED
                if (!new File(filename).exists()) {
                    (new File(filename)).mkdir();
                }
                File file = new File(filename + animeId + "_list.txt");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(response.toString().replace("{", "\n{"));
                fileWriter.close();
                //     File config = new File(username + "_config.txt");
                //    FileWriter fileWriter2 = new FileWriter(file);
                //     fileWriter.write(response.toString().replace("{","\n{"));
                //  fileWriter.close();
                return response.toString().replace("{", "\n{");
            } else {
                updateTextPool(true, "Error", response.toString().replace("{", "\n{"));
                return "!" + response.toString().replace("{", "\n{");
            }


        } catch (IOException e) {

            e.printStackTrace();
            updateTextPool(true, "Error", "IO Exception. please contact Injata (the dev):\n" + e.getMessage());
            return "IO Exception. please contact Injata (the dev):\n" + e.getMessage();
        }

    }


    public static String queryAnilistAPI(String username) {
        String query = "query  { MediaListCollection(type: ANIME userName: \"" + username + "\") { user {id } lists { name entries {  id score repeat progress advancedScores status customLists notes startedAt { day month year } completedAt { day month year} media { format " +
                "relations {\n" +
                "         \n" +
                "            edges { " +
                "              relationType " +
                "              node { id title { romaji } }\n" +
                "            }\n" +
                "          }\n" +
                "id coverImage {extraLarge large medium color}  title { romaji english } episodes duration startDate {day month year} endDate {day month year} tags { category name } genres meanScore} } } } } ";
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("query", query);

        try {
            URL url = new URL("https://graphql.anilist.co");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
            updateTextPool(true, "Error", "Anilist connection established.\nRequesting information...");

            //   con.connect();


            OutputStream out = con.getOutputStream();
            // System.out.println(json.toString());
            out.write(json.toString().getBytes());

            int statusCode = con.getResponseCode();

            InputStream is = null;

            if (statusCode >= 200 && statusCode < 400) {
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


            updateTextPool(true, "Error", "Anilist API request successful.\nCompiling information...");


            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = responseReader.readLine()) != null) {
                response.append(inputLine);
            }
            responseReader.close();
            if (statusCode == 200) {//&& SAVE_FILES_ENABLED
                File file = new File(directory + username + "_list.txt");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(response.toString().replace("{", "\n{"));
                fileWriter.close();
                //     File config = new File(username + "_config.txt");
                //    FileWriter fileWriter2 = new FileWriter(file);
                //     fileWriter.write(response.toString().replace("{","\n{"));
                //  fileWriter.close();
                return response.toString().replace("{", "\n{");
            } else {
                updateTextPool(true, "Error", response.toString().replace("{", "\n{"));
                return "!" + response.toString().replace("{", "\n{");
            }


        } catch (IOException e) {

            e.printStackTrace();
            updateTextPool(true, "Error", "IO Exception. please contact Injata (the dev):\n" + e.getMessage());
            return "IO Exception. please contact Injata (the dev):\n" + e.getMessage();
        }

    }


    // public static ArrayList<AnimeActivity> getAnimeActivityHistory() {
    //if (file exists()) { read file} else, if the time since the last file generated is larger than 20 seconds, write a new file(QueryAnimeHistory) and output that.
    //this should therefore act as a delayed sequencer, as only one of the "writing to file" should occur at any one time.

//}


    public static ArrayList<AnimeActivityLog> getAnimeActivity(AnimeLog log, int userid) {

        String output = queryAnimeHistory(userid, log.id, log.isManga);
        if (log.animestatus != AnimeLog.status.COMPLETED) {
            return null;
        }

        if (output == null) {
            return null;
        }
    //    System.out.println(output);
        try {
            JsonNode node = null;
            try {
                node = new ObjectMapper().readTree(output);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


            // profile.setProfileValue("user_name", username);

            //profile.id = node.get("data").get("MediaListCollection").get("user").get("id").asInt();
            //  HashMap<String, String> hash = new HashMap<>();

            ArrayList<AnimeActivityLog> activities = new ArrayList<>();
            int i = 0;
            while (node.get("data").get("Page").get("activities").get(i) != null) {

                long start = node.get("data").get("Page").get("activities").get(i).get("createdAt").longValue();
                String activityType = node.get("data").get("Page").get("activities").get(i).get("status").textValue();
                String progress;
                if (node.get("data").get("Page").get("activities").get(i).get("progress").isNull())  {
                    progress = "-1";
                } else {
                    progress = node.get("data").get("Page").get("activities").get(i).get("progress").textValue();
                }


                String listType = node.get("data").get("Page").get("activities").get(i).get("type").textValue();
                int likeCount = node.get("data").get("Page").get("activities").get(i).get("likeCount").intValue();
                String siteURL = node.get("data").get("Page").get("activities").get(i).get("siteUrl").textValue();

                activities.add(new AnimeActivityLog(log.id,start,activityType,progress,listType,likeCount,siteURL,log.getDisplayName()));

                //AnimeActivityLog(int animeid, long start, String activityType, String progress, String listType, int likeCount, String siteURL, String name) {
                //"progress":"1","status":"read chapter","type":"MANGA_LIST"
                //{"media":{"id":181372,"title":
                i++;
            }
            if (i==0) {
                return null;
            } else {
                return activities;
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }


      //  return null;
    }

    public static AnimeProfile getAnimeProfileJson(String input, String username, String type) {
        AnimeProfile profile = new AnimeProfile();
        try {
            JsonNode node = new ObjectMapper().readTree(input);


            profile.setProfileValue("user_name", username);

            profile.id = node.get("data").get("MediaListCollection").get("user").get("id").asInt();


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


                while (currentNode.get("entries").get(j) != null) {
                    JsonNode animeLogJson = currentNode.get("entries").get(j);
                    String status = node.get("data").get("MediaListCollection").get("lists").get(i).get("name").textValue();

                    profile.addProfileValue("user_total_" + status, status);
                    if (!hash.containsKey(animeLogJson.get("media").get("id").asText())) {
                        hash.put(animeLogJson.get("media").get("id").asText(), "true");
                        // System.out.println(animeLogJson.get("media").get("title").get("romaji"));
                        AnimeLog log = new AnimeLog();
                        log.id = animeLogJson.get("media").get("id").intValue();

                        System.out.println(log.id + " is the id of " + animeLogJson.get("media").get("id").intValue());
                        log.setScore(Double.parseDouble(animeLogJson.get("score").asText()));

                        log.setValue("repeat", animeLogJson.get("repeat").asText());


                        if (animeLogJson.get("media").has("format")) {
                            log.format = animeLogJson.get("media").get("format").textValue();
                        }

                        if (type.equals("Manga: ")) {
                            log.isManga = true;
                            log.setValue("series_episodes", animeLogJson.get("media").get("chapters").intValue() + "");


                        } else {
                            log.setValue("series_episodes", animeLogJson.get("media").get("episodes").intValue() + "");
                        }
                        log.setProgress(animeLogJson.get("progress").intValue());
                        // log.setValue("series_episodes",animeLogJson.get("media").get("chapters").intValue()+"");
                        log.setValue("duration", animeLogJson.get("media").get("duration").intValue() + "");
                        log.setValue("my_status", animeLogJson.get("status").textValue());

                        JsonNode startedAt = animeLogJson.get("startedAt");
                        log.setValue("my_start_date", startedAt.get("year").intValue() + "-" + startedAt.get("month").intValue() + "-" + startedAt.get("day").intValue());
                        JsonNode completedAt = animeLogJson.get("completedAt");
                        log.setValue("my_finish_date", completedAt.get("year").intValue() + "-" + completedAt.get("month").intValue() + "-" + completedAt.get("day").intValue());

                        JsonNode series_start = animeLogJson.get("media").get("startDate");
                        log.setValue("series_start", series_start.get("year").intValue() + "-" + series_start.get("month").intValue() + "-" + series_start.get("day").intValue());
                        JsonNode series_end = animeLogJson.get("media").get("endDate");
                        log.setValue("series_end", series_end.get("year").intValue() + "-" + series_end.get("month").intValue() + "-" + series_end.get("day").intValue());


                        log.setValue("series_title_english", type + animeLogJson.get("media").get("title").get("english").textValue());
                        if (log.getValue("series_title_english").equals("-1")) {
                            log.setValue("series_title_english", type + animeLogJson.get("media").get("title").get("romaji").textValue());
                        }
                        log.mediaType = type;

                        log.setValue("image", animeLogJson.get("media").get("coverImage").get("medium").textValue());
                        addLogValueIfNotNull(log, animeLogJson.get("media").get("coverImage"), "color");
                        log.rawData = animeLogJson;

                        profile.addAnime(log);


                        int x = 0;

                        if (animeLogJson.get("media").get("relations") != null) {
                            while (animeLogJson.get("media").get("relations").get("edges").get(x) != null) {
                                log.relations.add(animeLogJson.get("media").get("relations").get("edges").get(x).get("node").get("id").intValue() + "," + "," + animeLogJson.get("media").get("relations").get("edges").get(x).get("relationType").textValue());
                                //    System.out.println(animeLogJson.get("media").get("relations").get("edges").get(x).get("node").get("id").intValue() + "," + animeLogJson.get("media").get("relations").get("edges").get(x).get("relationType").textValue());
                                x++;
                            }
                        }
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




    public static String getSingluarValue(String input, String key) {
        String[] output = input.replace("</", "<").split("<" + key + ">");

        if (output.length > 1) {
            return input.replace("</", "<").split("<" + key + ">")[1];
        } else {
            return "-1";
        }

    }



    public static String getAnimeTitle(String input) {
        return unWrapCDATA(getSingluarValue(input, "series_title"));
    }

    public static String unWrapCDATA(String input) {
        return input.substring(9).split("]]>")[0];
    }



    public static void main(String[] args) {
        if (args.length > 0) {
            directory = args[0];
        }
        launch();
    }


}

