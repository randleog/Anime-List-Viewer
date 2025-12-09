package com.injata.animelist;

import com.fasterxml.jackson.databind.JsonNode;
import eu.hansolo.tilesfx.skins.HighLowTileSkin;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AnimeLog extends MenuElement {


    public static int max_score_value = 100;

    public static double gap = 75;
    public ArrayList<String> relations = new ArrayList<>();


    public ArrayList<String[]> activityLog = new ArrayList<>();


    public boolean isManga = false;

    public int episodes;

    public int progress;
    public double score;

    public long startDate;
    public long endDate;


    public long showStartDate;
    public long showEndDate;

    public String mediaType = "";

    public JsonNode rawData;
    public static double minZoomImage = 1.5;

    public static long min_interval_load_image = 100;//need to do the same but for querying the api in general as well. however, that would be with the config saving the last query, so we dont question more than every 2 minutes
    public static long lastImageLoad = 0;

    public int rewatches;


    public HashMap<String, String> animeValues;

    public boolean displayEN = true;

    public static ArrayList<Integer[]> colors;

    public Color color;

    public String format = "Anime";

    public static double lines_visible = 1.5;

    public static double max_text_Size_zoom = 2;
    public static double imageWidth = gap * 0.667;
    public static double imageHeight = gap * 0.95;

    private int duration = 0;

    public int id;

    private int isFirst = -1;

    private AnimeLog originator = null;

    public static enum relations {
        ADAPTION, SUMMARY, CHARACTER, SEQUEL, PREQUEL, ALTERNATIVE, OTHER, SIDE_STORY, SPIN_OFF, PARENT;
    }

    boolean hasCheckedDays = false;
    double days = 0;

    private ArrayList<AnimeActivityLog> history;

    public void setActivityHistory(ArrayList<AnimeActivityLog> history) {
        this.history = history;
    }

    public double getDays() {
        if (!hasCheckedDays) {
            days = Util.getAnimeDayDifference(startDate, endDate);
        }
        return days;

    }

    public boolean areYouAFirst() {
        if (isFirst == -1) {
            isFirst = (getRelation("PREQUEL") == -1) ? 1 : 0;
        }

        return isFirst == 1;
    }


    //note: make an enum for ADAPTION, SUMMARY, CHARACTER, SEQUEL, PREQUEL, ALTERNATIVE, OTHER, SIDE_STORY, SPIN_OFF, PARENT
    public int getRelation(String relationType) {
        for (String s : relations) {
            if (s.contains(relationType)) {

                return Integer.parseInt(s.split(",")[0]);
            }
        }
        return -1;
    }


    public static void loadColors() {
        colors = new ArrayList<>();

        colors.add(new Integer[]{0, 0, 0});
        colors.add(new Integer[]{30, 0, 0});
        colors.add(new Integer[]{55, 0, 0});
        colors.add(new Integer[]{80, 0, 0});
        colors.add(new Integer[]{105, 0, 0});
        colors.add(new Integer[]{130, 0, 0});
        colors.add(new Integer[]{180, 0, 0});
        colors.add(new Integer[]{255, 30, 0});
        colors.add(new Integer[]{255, 70, 0});
        colors.add(new Integer[]{250, 80, 0});
        colors.add(new Integer[]{240, 100, 0});
        colors.add(new Integer[]{230, 115, 0});
        colors.add(new Integer[]{210, 135, 0});
        colors.add(new Integer[]{190, 150, 0});
        colors.add(new Integer[]{180, 180, 0});
        colors.add(new Integer[]{170, 180, 0});
        colors.add(new Integer[]{160, 190, 0});
        colors.add(new Integer[]{150, 200, 0});
        colors.add(new Integer[]{0, 255, 0});
        colors.add(new Integer[]{0, 255, 180});
        colors.add(new Integer[]{0, 255, 320});

    }

    public static Color getColorFromScore(double score) {


        double v = Math.ceil(((score / 100.0) * colors.size()) * 100.0) / 100.0 - 1;

        Integer[] rgb1 = colors.get((int) (v));
        Integer[] rgb2 = colors.get((int) Math.ceil(v));


        double rgb2Fraction = ((v) - (int) (v));
        double rgb1Fraction = 1 - ((v) - (int) (v));
        //      System.out.println(v + " r:"+((int)Math.min(255,(rgb1[0]*rgb1Fraction+rgb2[0]*rgb2Fraction)))+" g:"+Math.min(255,(int)(rgb1[1]*rgb1Fraction+rgb2[1]*rgb2Fraction))+ " b:"+Math.min(255,(int)(rgb1[2]*rgb1Fraction+rgb2[2]*rgb2Fraction)));

        return Color.rgb((int) Math.min(255, (rgb1[0] * rgb1Fraction + rgb2[0] * rgb2Fraction)), Math.min(255, (int) (rgb1[1] * rgb1Fraction + rgb2[1] * rgb2Fraction)), Math.min(255, (int) (rgb1[2] * rgb1Fraction + rgb2[2] * rgb2Fraction)));
    }


    public void fillTextIfVisible(GraphicsContext g, String text, double x, double y, double zoomScale) {
        if (zoomScale < MenuAnimeTimeline.zoom_Text_Visible) {
            g.fillRect(x, y - zoomScale * 4, zoomScale * 5 * text.split("\n")[0].length(), zoomScale * 6);
        } else {
            if (!(x > HelloApplication.getCanvasWidth() || x < 0 || y > HelloApplication.getCanvasHeight() || y < 0)) {
                g.fillText(text, x, y);
            }
        }
    }


    public boolean findingImage = false;
    public Image image;

    public status animestatus;

    public void setProgress(int progress) {
        this.progress = progress;
    }


    public void draw(GraphicsContext g, double zoomScale, AnimeProfile profile) {


        double yv = getVisibleY() * zoomScale;
        double xv = getVisibleX() * zoomScale;


        g.setFill(getScoreColor());


        //pre watch line
        g.fillRect(xv
                , yv + zoomScale * (gap / 4) + zoomScale * 5.5,
                Util.getRelativeValue(profile.startDate, getEndDate()) * zoomScale + zoomScale * 1000,
                2 * zoomScale);
        //  canvas.getGraphicsContext2D().setFill(Color.WHITE);


        fillTextIfVisible(g, (int) Math.ceil(Util.getAnimeDayDifference(startDate, endDate) + 1) + " days, " + getValue("series_episodes") + " eps "
                , zoomScale * 910 + xv + Util.getRelativeValue(profile.startDate, getEndDate()) * zoomScale + zoomScale * Util.determinedDiff
                , yv + zoomScale * (gap / 4), zoomScale);


        //watch time line
        g.fillRect(zoomScale * 1000 + xv + Util.getRelativeValue(profile.startDate, getStartDate()) * zoomScale
                , yv + zoomScale * (gap / 4) + zoomScale * 5.5,
                Math.max(Util.getRelativeValue(getStartDate(), getEndDate()) * zoomScale, zoomScale) + zoomScale * Util.determinedDiff,
                10 * zoomScale);

        double startPointX = Util.getRelativeValue(profile.startDate, getShowStartDate());
        double endPointX = Util.getRelativeValue(profile.startDate, getShowEndDate());

        g.fillRect(zoomScale * 1000 + xv + startPointX * zoomScale
                , yv + zoomScale * (gap / 4) + zoomScale * 1.5,
                zoomScale * 1,
                10 * zoomScale);

        g.fillRect(zoomScale * 1000 + xv + endPointX * zoomScale
                , yv + zoomScale * (gap / 4) + zoomScale * 1.5,
                zoomScale * 1,
                10 * zoomScale);
        //   if ()

        //  System.out.println(zoomScale*1000+xv+getRelativeValue(profile.startDate, log.getStartDate()));

        double day = Util.getRelativeValue(profile.startDate, getEndDate()) / Util.determinedDiff + parent.x / Util.determinedDiff - imageWidth / Util.determinedDiff + 1000 / Util.determinedDiff;//getAnimeDayDifference(profile.startDate,log.getEndDate());
        if (day + 1 < 0) {

            fillTextIfVisible(g, "< " + String.format("%.02f", day + 1) + "d", 60 * zoomScale, yv + zoomScale * (gap / 4), zoomScale);
        } else {

            fillTextIfVisible(g, String.format("%.02f", day + 1) + "d >", 60 * zoomScale, yv + zoomScale * (gap / 4), zoomScale);

        }


        g.setFill(Color.WHITE);

        g.fillRect(zoomScale * 1000 + xv + startPointX * zoomScale
                , yv + zoomScale * (gap / 4) + zoomScale * 1.5,
                zoomScale * 1,
                10 * zoomScale);
        //  canvas.getGraphicsContext2D().fillText("air date: "+ log.getValue("series_start"), zoomScale*920+xv+startPointX*zoomScale,yv+zoomScale*(gap/4)+zoomScale*21);
        fillTextIfVisible(g, "air date: " + getValue("series_start"), zoomScale * 920 + xv + startPointX * zoomScale
                , yv + zoomScale * (gap / 4) + zoomScale * 25, zoomScale);

        if (endPointX != startPointX) {
            g.fillRect(zoomScale * 1000 + xv + endPointX * zoomScale
                    , yv + zoomScale * (gap / 4) + zoomScale * 1.5,
                    zoomScale * 1,
                    10 * zoomScale);
            fillTextIfVisible(g, "end date: " + getValue("series_end"),
                    zoomScale * 920 + xv + endPointX * zoomScale
                    , yv + zoomScale * (gap / 4) + zoomScale * 25, zoomScale);

        }


        if (!(xv > HelloApplication.getCanvasWidth() || yv > HelloApplication.getCanvasHeight() || yv < 0)) {
            if (minZoomImage < zoomScale) {
                if (!findingImage) {

                    findingImage = true;
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            while (System.currentTimeMillis() - lastImageLoad < min_interval_load_image) {
                                try {
                                    Thread.sleep(min_interval_load_image);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
//System.out.println(id);
                            lastImageLoad = System.currentTimeMillis();
                            image = new Image(getValue("image"), 100, 150, false, false);


                        }
                    });

                    thread.start();


                }
            }
            if (image == null) {
                g.setFill(Color.valueOf(getValue("color")));
                g.fillRect(0, yv - (gap / 2) * zoomScale, imageWidth * zoomScale, zoomScale * imageHeight);
            } else {
                g.drawImage(image, 0, yv - (gap / 2) * zoomScale, imageWidth * zoomScale, zoomScale * imageHeight);
            }
            g.setFill(Color.WHITE);
            fillTextIfVisible(g, getDisplayString(), 60 * zoomScale, yv - 10 * zoomScale, zoomScale);


        }


        if (HelloApplication.mouseY + 150 > yv && HelloApplication.mouseY - 150 * zoomScale < yv) {
            if (originator == null) {
                // System.out.println(id);
                originator = profile.getOriginator(id);
            }
            if (originator == this) {

            } else {
                fillTextIfVisible(g, "Sequel!", xv + Util.getRelativeValue(profile.startDate, getEndDate()) * zoomScale + 1000 * zoomScale
                        , originator.getVisibleY() * zoomScale + zoomScale * (gap / 4) + zoomScale * 25, zoomScale);
                //   g.fillRect(xv + Util.getRelativeValue(profile.startDate, getEndDate()) * zoomScale+1000*zoomScale
                //        , originator.getVisibleY()*zoomScale + zoomScale * (gap / 4) + zoomScale * 25,
                //        10* zoomScale ,
                //        2 * zoomScale);

            }


            if (history != null) {

                for (AnimeActivityLog log : history) {
                    g.setFill(log.getActivityType().getColor());
                    // System.out.println(profile.startDate + "  "+ log.getStart());
                    log.getActivityType().draw(g, xv, yv, zoomScale, gap, log, profile);


                }
            }
        }
    }


    public void getAnimePaceAtDays(long startDate, HashMap<String, Double> dayLog, String mediaKey) {
        if (animestatus==status.PAUSED) {
            return;
        }
        int start = (int) ((getStartDate() - startDate) / 86400000);
        int end = (int) ((getEndDate() - startDate) / 86400000);
        if (start < 0) {
            start = end;
        }

        double multiplier = getDuration()*2;
        if (multiplier == 0) {
            multiplier = 2.0;
        }
        if (mediaKey.equalsIgnoreCase("Manga")) {
            multiplier = multiplier*6;
        }

      //  if (format != null)
        if (format != null && format.equalsIgnoreCase("NOVEL")) {
            multiplier = 2.0*multiplier;
        }
        //  int lastAccountedEpisode = 1;
        int lastAccountedTime = start;
        int completedEpisodes = getProgress();
        if (animestatus==status.COMPLETED || animestatus == status.REPEATING) {
            completedEpisodes = getEpisodes();
        }
        int accountedForEpisodes = 0;
        int lastEpisodeAccounted = 0;

        int accountedForRewatchEpisodes = 0;
        int lastRewatchedEpisodeAccounted = 0;

        if (history != null) {

            for (AnimeActivityLog log : history) {
                switch (log.getActivityType()) {
                    case AnimeActivityLog.ACTIVITY_TYPE.WATCHED_EPISODE,AnimeActivityLog.ACTIVITY_TYPE.READ_CHAPTER -> {

                        int logDay = Math.min(Math.max((int)((log.getStart()-startDate)/ 86400000),start),end);
                        addToDayLog(dayLog,log.getEpisodeCount(),lastAccountedTime,logDay,logDay-lastAccountedTime,mediaKey,0,multiplier);
                        accountedForEpisodes+=log.getEpisodeCount();
                        lastEpisodeAccounted = log.getLastEpisode();

                        lastAccountedTime = logDay;
                    }
                    case AnimeActivityLog.ACTIVITY_TYPE.REWATCHED_EPISODE,AnimeActivityLog.ACTIVITY_TYPE.REREAD_CHAPTER -> {

                        int logDay = (int)((log.getStart()-startDate)/ 86400000);
                        if (lastAccountedTime ==0) {
                            lastAccountedTime = logDay;
                        }
                        addToDayLog(dayLog,log.getEpisodeCount(),lastAccountedTime,logDay,logDay-lastAccountedTime,"Rewatch",0,multiplier);


                        accountedForRewatchEpisodes+=log.getEpisodeCount();
                        lastRewatchedEpisodeAccounted = log.getLastEpisode();


                        lastAccountedTime = logDay;
                    }
                    case AnimeActivityLog.ACTIVITY_TYPE.REWATCHED -> {


                        int logDay = (int)((log.getStart()-startDate)/ 86400000);
                        addToDayLog(dayLog,(getEpisodes()-accountedForRewatchEpisodes),lastAccountedTime,logDay,logDay-lastAccountedTime,"Rewatch",0,multiplier);

                        accountedForRewatchEpisodes = 0;
                        lastRewatchedEpisodeAccounted = 0;
                        lastAccountedTime = 0;


                    }
                    case AnimeActivityLog.ACTIVITY_TYPE.COMPLETED -> {


                        addToDayLog(dayLog,(getEpisodes()-lastEpisodeAccounted),lastAccountedTime,end,end-lastAccountedTime,mediaKey,0,multiplier);
                        accountedForEpisodes+=(getEpisodes()-lastEpisodeAccounted);
                        lastAccountedTime = 0;
                        //     lastEpisodeAccounted = log.getLastEpisode();
                    }
                }

            }
        } else {
            addToDayLog(dayLog,completedEpisodes,start,end,end-start,mediaKey,0,multiplier);
            accountedForEpisodes = getEpisodes();
        }
        if (animestatus==status.COMPLETED || animestatus==status.REPEATING ) {
            int topup = Math.max((getEpisodes()- accountedForEpisodes),0);
            addToDayLog(dayLog, topup, start, end, end - start, mediaKey, 1,multiplier);
        }
        //  System.out.println("accounted for episodes: " + accountedForEpisodes + "/" + getProgress() );


    }


    public void addToDayLog(HashMap<String, Double> dayLog, double pace, int startDay, int endDay, int dayCount, String mediaKey, int ongoing, double multiplier) {
        if (dayCount > 0) {
            for (int i = 0; i <= dayCount; i++) {

                dayLog.put(((startDay + i) + "_" + mediaKey + "Pace"), dayLog.getOrDefault(((startDay + i)  + "_" + mediaKey + "Pace"), 0.0) + pace*multiplier /(1.0*( (dayCount + 1))));
                //System.out.println("pace of " + (endDay - startDay + 1));

                dayLog.put(((startDay + i)  + "_" + mediaKey + "Count"), dayLog.getOrDefault(((startDay + i)  + "_" + mediaKey + "Count"), 0.0) + ongoing);


            }
        } else if (startDay == endDay) {
            dayLog.put((startDay + "_" + mediaKey + "Pace"), dayLog.getOrDefault((startDay + "_" + mediaKey + "Pace"), 0.0) + pace*multiplier);

            dayLog.put((startDay + "_" + mediaKey + "Count"), dayLog.getOrDefault((startDay + "_" + mediaKey + "Count"), 0.0) + 1);

        }


    }

    @Override
    public void drawElement(GraphicsContext g) {

    }

    @Override
    public boolean interactElement(String info, boolean mouseDown, double xp, double yp) {
        return false;
    }

    @Override
    public boolean scroll(double delta, double xp, double yp) {
        return false;
    }

    @Override
    public boolean drag(double xp, double yp) {
        return false;
    }

    @Override
    public boolean mouseRelease(double xp, double yp) {
        return false;
    }

    @Override
    public boolean mouseDown(double xp, double yp) {
        return false;
    }

    @Override
    public String getInfo() {
        return "";
    }

    public enum status {
        COMPLETED {
            public String textValue() {
                return "COMPLETED";
            }
        },
        DROPPED {
            public String textValue() {
                return "DROPPED";
            }
        },
        REPEATING {
            public String textValue() {
                return "REPEATING";
            }
        },
        PAUSED {
            public String textValue() {
                return "PAUSED";
            }
        },
        PLANNING {
            public String textValue() {
                return "PLANNING";
            }
        },
        CURRENT {
            public String textValue() {
                return "CURRENT";
            }
        };

        public abstract String textValue();

    }


    public AnimeLog() {
        super(0, 0);
        startDate = 0;
        endDate = 0;
        score = 0;
        progress = 0;
        episodes = 0;
        animeValues = new HashMap<>();
        animestatus = status.PAUSED;
        rewatches = 0;


    }

    public String getDisplayString() {
        return getDisplayName() + "\n"
                + getValue("my_score") + "/100 " + getValue("my_status") + " re-watch:" + rewatches + "\n"
                + getValue("my_start_date") + " to " + getValue("my_finish_date");
    }

    public String getDisplayName() {
        return getValue("series_title" + (displayEN ? "_english" : "")) + " [" + format + "]";
    }


    public int getDuration() {
        return duration;
    }


    private static boolean has_used_non_point5 = false;
    private static boolean has_used_Decimal = false;
    private static double highestRawScore = 0;

    public void setScore(double score) {
        this.score = score;

        if ((int) score < score) {
            has_used_Decimal = true;
            if ((int) score + 0.5 != score) {
                has_used_non_point5 = true;
            }
        }
        if (score > highestRawScore) {
            highestRawScore = score;
        }
    }

    public void setValue(String type, String input) {
        if (input == null) {
            return;
        }
        switch (type) {

            case "my_status" -> {
                animestatus = status.valueOf(input);
                animeValues.put(type, input);
            }
            case "duration" -> {
                duration = Integer.parseInt(input);
            }
            case "series_episodes" -> episodes = Integer.parseInt(input);
            case "my_watched_episodes" -> progress = Integer.parseInt(input);
            case "my_score" -> {
                score = Integer.parseInt(input);

            }
            case "repeat" -> rewatches = Integer.parseInt(input);
            case "my_start_date" -> {
                input = input.replace("null", "0");
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = f.parse(input);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                startDate = d.getTime();
                animeValues.put(type, input);
            }
            case "my_finish_date" -> {
                input = input.replace("null", "0");
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = f.parse(input);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                endDate = d.getTime();
                animeValues.put(type, input);
            }
            case "series_start" -> {
                input = input.replace("null", "0");
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = f.parse(input);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                showStartDate = d.getTime();
                animeValues.put(type, input);
            }
            case "series_end" -> {
                input = input.replace("null", "0");
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = f.parse(input);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                showEndDate = d.getTime();
                animeValues.put(type, input);
            }

            case "animeid" -> {
                System.out.println("seriously what");
                // id = Integer.parseInt(input);
            }
            default -> animeValues.put(type, input);

        }
    }

    private double oldY;
    private long animation_length = 500;


    @Override
    public double getY() {
        if (HelloApplication.currentTime - lastPosUpdate < animation_length) {

            //   System.out.println(((HelloApplication.currentTime-lastPosUpdate)/1500.0) +  " " +(1-((HelloApplication.currentTime-lastPosUpdate)/1500.0)) );
            return ((HelloApplication.currentTime - lastPosUpdate) / (animation_length * 1.0)) * y + (1 - ((HelloApplication.currentTime - lastPosUpdate) / (animation_length * 1.0))) * oldY;
        } else {
            return this.y;
        }
    }

    private static long lastPosUpdate = System.currentTimeMillis();

    public void updateOrder(double y) {

        lastPosUpdate = HelloApplication.currentTime;
        oldY = this.y;
        this.y = y;
    }

    public long getEndDate() {


        if (endDate > 0) {
            return endDate;
        }
        if (animestatus == status.CURRENT) {
            return HelloApplication.launchTime;
        }
        return getStartDate();
    }

    public long getStartDate() {
        return startDate <= 0 ? Long.MAX_VALUE : startDate;
    }

    public int getRewatches() {
        return rewatches;
    }

    public long getShowEndDate() {
        return showEndDate <= 0 ? getShowStartDate() : showEndDate;
    }

    public long getShowStartDate() {
        return showStartDate <= 0 ? Long.MAX_VALUE : showStartDate;
    }

    public int getTimeWatching() {

        return (int) Math.ceil(Util.getAnimeDayDifference(startDate, endDate));
    }

    public double getWatchPace() {

        int timeWatching = getTimeWatching() + 1;
        if (timeWatching <= 0) {
            timeWatching = Integer.MAX_VALUE;
        }

        return ((progress * 1.0) / Math.max(timeWatching, 1.0));
    }

    public int getEpisodes() {
        return episodes;
    }

    public int getProgress() {
        return progress;
    }


    private int displayScore = -2;

    public int getScore() {

        if (displayScore == -2) {

            if (has_used_Decimal) {
                System.out.println(max_score_value);
                if (has_used_non_point5) {
                    max_score_value = (int) (highestRawScore * 10);
                } else {
                    max_score_value = (int) (highestRawScore * 2);
                }
            } else {
                max_score_value = (int) highestRawScore;
            }


            displayScore = (int) (((score) / max_score_value) * 100.0);
        }
        return displayScore;

    }

    public Color getScoreColor() {
        if (score == 0) {
            return Color.GREY;
        }

        //if (this.animestatus==status.PAUSED || this.animestatus==status.DROPPED || ) {

        //   }


        return getColorFromScore(getScore());

    }

    public int getValueInt(String type) {
        return switch (type) {
            case "series_episodes" -> episodes;
            case "my_watched_episodes" -> progress;
            case "my_score" -> getScore();
            default -> Integer.parseInt(animeValues.getOrDefault(type, "0"));

        };
    }

    public String getValue(String type) {
        return switch (type) {
            case "series_episodes" -> episodes + "";
            case "my_watched_episodes" -> progress + "";
            case "my_score" -> getScore() + "";
            case "color" -> animeValues.getOrDefault(type, "#ffffff");
            default -> animeValues.getOrDefault(type, "-1");

        };
    }
}
