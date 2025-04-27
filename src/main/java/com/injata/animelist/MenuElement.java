package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;

public abstract class MenuElement {

    public int x;
    public int y;
    public MenuElement(int x, int y) {

        this.x = x;
        this.y = y;

    }



    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public  int getY() {
        return this.y;
    }

    public abstract void drawElement(GraphicsContext g, double zoom, double xp, double yp);
    public abstract boolean interactElement(String info,boolean mouseDown,double zoom, double xp, double yp);



    public abstract String getInfo();
}
