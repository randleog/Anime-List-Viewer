package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuToggleButton extends MenuElement{
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 75;


    public double fontSize = 30;
    public static final int TEXT_HEIGHT = 40;


    private String promptText;
    private String text;
    private boolean isHover = false;
    private String keyText;




    public MenuToggleButton(String text, String promptText,String keyText,int x, int y, int width, int height, MenuDirections direction) {
        super(x,y,direction);

        this.width =width;
        this.width = Math.max(((text.length()+4)*fontSize/1.6666667),this.width);
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


        cacheX=getVisibleX();
        cacheY=getVisibleY();
        g.fillRect(cacheX,cacheY,this.width,this.height);
        g.setFont(Font.font("monospace",fontSize));
        if (text.isEmpty()) {
            g.setFill(Color.GRAY);
            g.fillText(promptText, cacheX+height/2, cacheY+height/1.5);
        } else {
            g.setFill(Color.WHITE);
            g.fillText(text, cacheX+height/2, cacheY+height/1.5);
        }

        if (HelloApplication.textPool.getOrDefault(keyText,"1").equals("1")) {
            g.setStroke(Color.WHITE);
            g.strokeRect(cacheX,cacheY,this.width,this.height);
        }

    }

    @Override
    public boolean interactElement(String info,boolean mouseDown, double xp, double yp) {
        boolean prevHover = isHover;

        if (xp < cacheX+width && xp > cacheX && yp < cacheY+height && yp > cacheY) {
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
        HelloApplication.textPool.put(keyText,HelloApplication.textPool.getOrDefault(keyText,"1").equals("1") ? "0" : "1");
        System.out.println("set " + keyText + " to " + HelloApplication.textPool.get(keyText));

        HelloApplication.actionButton(keyText,this);
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



    public String getInfo() {
        return text + " " + promptText + " togglebutton";
    }



}
