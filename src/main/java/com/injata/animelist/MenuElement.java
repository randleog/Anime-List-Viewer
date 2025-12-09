package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;


public abstract class MenuElement {



    public double x;
    public double y;
    public MenuDirections direction;
    public MenuElement parent;

    public double width;
    public double height;

    public MenuElement focusedItem;
    protected double cacheX = 0;
    protected double cacheY = 0;

    public MenuElement(int x, int y) {

        this.direction = MenuDirections.TOP_LEFT;
        this.x = x;
        this.y = y;

    }

    public MenuElement(int x, int y, MenuDirections direction) {

        this.direction = direction;
        this.x = x;
        this.y = y;

    }

    public double getVisibleX() {
        double offset =0;
        if (parent!=null) {
            offset =parent.getLogicalX();
        }
        return direction.getDrawX(this,HelloApplication.getCanvasWidth())+offset;
    }

    public double getVisibleHeight() {
        return height;
    }

    public double getVisibleY() {
        double offset =0;
        if (parent!=null) {
            offset =parent.getLogicalY();
        }
        return direction.getDrawY(this,HelloApplication.getCanvasHeight())+offset;
    }


    public double getLogicalX() {
        double offset =0;
        if (parent!=null) {
            offset =parent.getLogicalX();
        }
        return direction.getLogicalX(this,HelloApplication.getCanvasWidth())+offset;
    }

    public double getLogicalY() {
        double offset =0;
        if (parent!=null) {
        //    offset =parent.getLogicalY();
        }
        return direction.getLogicalY(this,HelloApplication.getCanvasHeight())+offset;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public  double getY() {
        return this.y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return this.width;
    }

    public  double getHeight() {
        return this.height;
    }

    public abstract void drawElement(GraphicsContext g);
    public abstract boolean interactElement(String info,boolean mouseDown, double xp, double yp);

    public abstract boolean scroll(double delta, double xp, double yp);

    public abstract boolean drag(double xp, double yp);

    public abstract boolean mouseRelease(double xp, double yp);
    public abstract boolean mouseDown(double xp, double yp);

    public abstract String getInfo();
}
