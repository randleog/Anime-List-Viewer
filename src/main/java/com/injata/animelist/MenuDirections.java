package com.injata.animelist;

public enum MenuDirections {


    TOP_RIGHT {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return canvasWidth-e.x-e.getWidth()-rightPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.y+topPadding;
        }
    },
    TOP_LEFT {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.x+leftPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.y+topPadding;
        }
    },
    BOTTOM_RIGHT {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return canvasWidth-e.x-rightPadding-e.getWidth();
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return canvasHeight-e.y-bottomPadding-e.getHeight();
        }
    },
    BOTTOM_LEFT {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.x+leftPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return canvasHeight-e.y-bottomPadding;
        }
    },
    CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return canvasWidth/2+e.x-e.getWidth()/2;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return canvasHeight/2+e.y-e.getHeight()/2;
        }
    },
    TOP_CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return canvasWidth/2+e.x;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.y+topPadding;
        }
    },
    BOTTOM_CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return canvasWidth/2+e.x;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return canvasHeight-e.y-bottomPadding;
        }
    },
    LEFT_CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.x+leftPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return canvasHeight/2+e.y;
        }
    },
    RIGHT_CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return canvasWidth-e.x-rightPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return canvasHeight/2+e.y;
        }
    };
    public abstract double getDrawX(MenuElement e, double width);
    public abstract double getDrawY(MenuElement e, double height);
    public static int leftPadding = 15;
    public static int rightPadding = 15;
    public static int topPadding = 15;
    public static int bottomPadding = 15;
}