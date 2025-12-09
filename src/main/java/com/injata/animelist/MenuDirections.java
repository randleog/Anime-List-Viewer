package com.injata.animelist;

public enum MenuDirections {


    TOP_RIGHT {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()-e.getX()-e.getWidth()-rightPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.getY()+topPadding;
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()-e.getX()-e.getWidth();
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.getY();
        }
    },
    TOP_LEFT {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.getX()+leftPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.getY()+topPadding;
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.getX();
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.getY();
        }
    },
    BOTTOM_RIGHT {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()-e.getX()-rightPadding-e.getWidth();
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()-e.getY()-bottomPadding-e.getHeight();
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()-e.getX()-e.getWidth();
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()-e.getY()-e.getHeight();
        }
    },
    BOTTOM_LEFT {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.getX()+leftPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()-e.getY()-bottomPadding-e.getHeight();
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.getX();
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()-e.getY()-e.getHeight();
        }
    },
    CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()/2+e.getX()-e.getWidth()/2;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()/2+e.getY()-e.getHeight()/2;
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()/2+e.getX()-e.getWidth()/2;
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()/2+e.getY()-e.getHeight()/2;
        }
    },
    TOP_CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()/2+e.getX();
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.getY()+topPadding;
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()/2+e.getX();
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.getY();
        }
    },
    BOTTOM_CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()/2+e.getX();
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()-e.getY()-bottomPadding;
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()/2+e.getX();
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()-e.getY();
        }
    },
    LEFT_CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.getX()+leftPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()/2+e.getY();
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.getX();
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()/2+e.getY();
        }
    },
    RIGHT_CENTER {
        public double getDrawX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()-e.getX()-rightPadding;
        }
        public double getDrawY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()/2+e.getY();
            
        }
        public double getLogicalX(MenuElement e,double canvasWidth) {
            return e.parent.getWidth()-e.getX();
        }
        public double getLogicalY(MenuElement e,double canvasHeight) {
            return e.parent.getHeight()/2+e.getY();
        }
    };
    public abstract double getDrawX(MenuElement e, double width);
    public abstract double getDrawY(MenuElement e, double height);

    public abstract double getLogicalX(MenuElement e, double width);
    public abstract double getLogicalY(MenuElement e, double height);
    public static int leftPadding = 15;
    public static int rightPadding = 15;
    public static int topPadding = 15;
    public static int bottomPadding = 15;
}