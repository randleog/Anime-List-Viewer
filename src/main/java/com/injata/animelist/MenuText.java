package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuText extends MenuElement {
    public String text;
    public double fontSize= 30;
    private String key;

    private String textType="null";
    public MenuText(String text, int x, int y, MenuDirections direction) {
        super(x, y, direction);
        this.width = Math.max(((text.length()+4)*fontSize/1.6666667),this.width);
        this.text = text;
        textType = "static";
    }

    public MenuText(String text, int x, int y,String key, MenuDirections direction) {
        super(x, y, direction);
        this.width = Math.max(((text.length()+4)*fontSize/1.6666667),this.width);
        this.text = text;
        this.key = key;
        textType = "counter";
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return this.text;
    }

    @Override
    public void drawElement(GraphicsContext g) {
     //   text = HelloApplication.textPool.getOrDefault(key,text);
        cacheX=getVisibleX();
        cacheY=getVisibleY();
     //   System.out.println(getVisibleX());
      //  System.out.println(getVisibleY());
        g.setFill(Color.WHITE);
        g.setFont(Font.font("monospace",fontSize));
        switch (textType) {
            case "counter" ->{
                g.fillText(text + HelloApplication.textPool.getOrDefault(key,""), cacheX, cacheY);
            }
            case "static" ->{
                g.fillText(text , cacheX, cacheY);
            }
        }


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
        return "text";
    }
}
