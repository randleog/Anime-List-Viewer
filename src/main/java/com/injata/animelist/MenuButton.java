package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuButton extends MenuElement{
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 75;


    public static final int TEXT_HEIGHT = 20;

    private int width;
    private int height;
    private String promptText;
    private String text;
    private boolean isHover = false;
    private String keyText;
    public MenuButton(int x, int y) {
        super(x,y);

        this.width =DEFAULT_WIDTH;
        this.height =DEFAULT_HEIGHT;
        this.promptText="";
        this.text = "";
        keyText = "";

    }

    public MenuButton(String text, String promptText,int x, int y, int width, int height) {
        super(x,y);

        this.width =width;
        this.height =height;
        this.promptText=promptText;
        this.text = text;
        this.keyText = text;

    }
    public MenuButton(String text, String promptText,String keyText,int x, int y, int width, int height) {
        super(x,y);

        this.width =width;
        this.height =height;
        this.promptText=promptText;
        this.text = text;
        this.keyText = keyText;

    }

    @Override
    public void drawElement(GraphicsContext g) {

        if (isHover) {
            g.setFill(Color.rgb(255, 255, 255, 0.1));

        } else {
            g.setFill(Color.rgb(0, 0, 0, 0.5));
        }
        double xpos = this.x;
        double ypos = this.y;
        g.fillRect(xpos,ypos,this.width,this.height);
        g.setFont(Font.font("monospace",30));
        if (text.isEmpty()) {
            g.setFill(Color.GRAY);
            g.fillText(promptText, xpos+TEXT_HEIGHT, ypos+height-TEXT_HEIGHT);
        } else {
            g.setFill(Color.WHITE);
            g.fillText(text, xpos+TEXT_HEIGHT, ypos+height-TEXT_HEIGHT);
        }

    }

    @Override
    public boolean interactElement(String info,boolean mouseDown, double xp, double yp) {
        boolean prevHover = isHover;
        if (xp < x+width && xp > x && yp < y+height && yp > y) {
            isHover = true;
            if (mouseDown) {
                triggerAction();
            }
        } else {
            isHover = false;
        }


        return isHover!=prevHover;
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

    public void triggerAction() {
        HelloApplication.actionButton(keyText, this);
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText(String text) {
        return this.text;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptText(String promptText) {
        return this.promptText;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public  int getHeight() {
        return this.height;
    }

    public String getInfo() {
        return text + " " + promptText;
    }



}
