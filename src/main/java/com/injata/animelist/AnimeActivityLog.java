package com.injata.animelist;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AnimeActivityLog {

    public static enum ACTIVITY_TYPE {
        PLANS_TO_READ {

            public Color getColor() {
                return Color.WHITE;
            }
        },
         COMPLETED {

             public Color getColor() {
                return Color.GREEN;
             }
         },
         WATCHED_EPISODE {


             public Color getColor() {
                 return Color.WHITE;
             }
         },
         PLANS_TO_WATCH {

             public Color getColor() {
                 return Color.DARKGREY;
             }
         },
         PAUSED_WATCHING {

             public Color getColor() {
                 return Color.DARKGREY;
             }
         },
         DROPPED {

             public Color getColor() {
                 return Color.DARKGREY;
             }
         },
        REWATCHED {


            public Color getColor() {
                return Color.DARKGREY;
            }
        },
        REWATCHED_EPISODE{

            public Color getColor() {
                return Color.LIME;
            }
        },   READ_CHAPTER{

            public Color getColor() {
                return Color.WHITE;
            }
        },   REREAD_CHAPTER{

            public Color getColor() {
                return Color.LIME;
            }
        };

        abstract Color getColor();

        void draw(GraphicsContext g, double xv, double yv, double zoomScale, double gap, AnimeActivityLog log, AnimeProfile profile) {
            g.fillRect(zoomScale * 1000 + xv + Util.getRelativeValue(profile.startDate, log.getStart()) * zoomScale
                    , yv + zoomScale * (gap / 4) + zoomScale * 3,
                    zoomScale * Math.min(log.getEpisodeCount(), 24),
                    zoomScale * 2);
        }

    }

    private int animeid;
    private long start;
    private long end;

    private ACTIVITY_TYPE activityType;
    private int[] progress;
    private String listType;
    private int likeCount;
    private String siteURL;
    private String name;

    public AnimeActivityLog(int animeid, long start, String activityType, String progress, String listType, int likeCount, String siteURL, String name) {
        this.animeid = animeid;
        this.start = start;
        this.activityType = ACTIVITY_TYPE.valueOf(activityType.toUpperCase().replace(" ","_"));
        this.progress = getProgress(progress);
        this.listType = listType;
        this.likeCount = likeCount;
        this.siteURL = siteURL;
        this.name = name;

    }



    public long getStart() {
        return start*1000;
    }

    public ACTIVITY_TYPE getActivityType() {
        return activityType;
    }

    public String getListType() {
        return listType;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getSiteURL() {
        return siteURL;
    }

    public String getName() {
        return name;
    }

    public int getAnimeId() {
        return animeid;
    }

    public int getEpisodeCount() {
        return progress.length;
    }

    public int getFirstEpisode() {
        return progress[0];
    }
    public int getLastEpisode() {
        return progress[progress.length-1];
    }

    public int[] getWatchList() {
        return progress;
    }

    //inputs:
    //eg "2 - 5"
    //eg "1"
    private int[] getProgress(String input) {
        if (input.contains(" - ")) {
            int firstValue = Integer.parseInt(input.split(" - ")[0]);
            int lastValue = Integer.parseInt(input.split(" - ")[1]);
            int[] output = new int[lastValue-firstValue+1];
            int val = 0;
            for (int i = firstValue; i <= lastValue; i++) {
                output[val]=i;
                val++;
            }
            return output;
            //eg [2,3,4,5] when "2 - 5"
        }
        return new int[]{Integer.parseInt(input)};
    }


}
