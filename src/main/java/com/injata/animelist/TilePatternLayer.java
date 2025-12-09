package com.injata.animelist;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

//the user needs to be able to customise the "wall dimensions" so that it can be any shape, of specific dimensions the user sets.
// the user then sets the pattern by making shapes in a certain space,
//then those shapes are overlayed on top of the previous pattern made by the user, and the user can specify the exact postiion the overlay will be.
//then it tells the user the number of total tiles they will need, and the specifications of the tiles which will need to be cut.
public class TilePatternLayer {

    ArrayList<Integer[]> rectangles = new ArrayList<>(); //info given by the user
    ArrayList<Integer[]> xyPositions = new ArrayList<>();//calculated based on the start position of the top left of each rectangle

    public  void setRectangles(ArrayList<Integer[]> rectangles) {
        this.rectangles = rectangles;
    }
    public boolean intersects(int[] tile, Integer[] rectangle) { //0,1 = x,y   2,3 = w,h



        return false;
    }
    public double getIntersectAmountTile(int[] tile) { //0,1 = x,y   2,3 = w,h
        double totalAreaInside = 0;
        double x = tile[0];
        double y = tile[1];
        double width = tile[2];
        double height = tile[3];
        for (Integer[] rectangle : xyPositions) {
            double rx = rectangle[0];
            double ry = rectangle[1];
            double rwidth = rectangle[2];
            double rheight = rectangle[3];

            double intersectLeftWidth = x-rx;
        }
        return 0.0;
    }

//width=5, xpos =5,    rwidth =5, rxpos=5    =

// width=3, xpos =5,    rwidth =5, rxpos=5    = 3
//width=5, xpos =5,    rwidth =3, rxpos=5    = 3


//width=5, xpos =6,    rwidth =5, rxpos=5    = 4
//xpos =6   xpos2 = 11     rxpos =5,   rxpos= 10       =4
    //11-6   10-5
    //6-11

}
/*
public class RectanglesUnion {



    public static int calculateSpace(int[][] rectangles) {
        HashMap<String,Integer> map = new HashMap<>();
        int area = 0;
        for (int i=0; i < rectangles.length; i++) {
            int x1 = Math.min(rectangles[i][0],rectangles[i][2]);
            int y1 = Math.min(rectangles[i][1],rectangles[i][3]);
            int x2 = Math.max(rectangles[i][0],rectangles[i][2]);
            int y2 = Math.max(rectangles[i][1],rectangles[i][3]);
            for (int j =x1; j < x2; j++) {
                for (int k =y1; k < y2; k++) {
                    String key = j+","+k;
                    if (!map.containsKey(key)) {
                        area++;
                    }

                    map.put(key,1);
                }
            }
        }
        return area;
    }
}

 */