package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuButton extends MenuElement{
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 75;


    public static final int TEXT_HEIGHT = 20;

    private String promptText;
    private String text;
    private boolean isHover = false;
    private String[] actions;

    private double fontSize = 30;

    private double cacheX = 0;
    private double cacheY = 0;

    private boolean shouldFocus;



    public MenuButton(String text, String promptText,int x, int y, int width, int height, MenuDirections direction, boolean shouldFocus, String... actions) {
        super(x,y,direction);
        this.shouldFocus = shouldFocus;
        this.width =width;
        this.width = Math.max(((text.length()+4)*fontSize/1.6666667),this.width);
        this.height =height;
        this.promptText=promptText;
        this.text = text;
        this.actions = actions;

    }



    @Override
    public void drawElement(GraphicsContext g) {

        if (isHover) {
            g.setFill(Color.rgb(255, 255, 255, 0.1));

        } else {
            g.setFill(Color.rgb(0, 0, 0, 0.5));
        }
        double xpos = getVisibleX();
        double ypos = getVisibleY();
        cacheX=xpos;
        cacheY=ypos;
        g.fillRect(xpos,ypos,this.width,this.height);
        g.setFont(Font.font("monospace",fontSize));
        if (text.isEmpty()) {
            g.setFill(Color.GRAY);
            g.fillText(promptText, cacheX+height/2, cacheY+height/1.5);
        } else {
            g.setFill(Color.WHITE);
            g.fillText(text, cacheX+height/2, cacheY+height/1.5);
        }


        if (shouldFocus) {
            g.setStroke(Color.WHITE);
            g.strokeRect(cacheX,cacheY,this.width,this.height);
        }
    }

    @Override
    public boolean interactElement(String info,boolean mouseDown, double xp, double yp) {
        boolean prevHover = isHover;
        double xpos = cacheX;
        double ypos = cacheY;
        if (xp < xpos+width && xp > xpos && yp < ypos+height && yp > ypos) {
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

        for (String action : actions) {
            if (action.contains("!")) {
                action = action.replace("!","");
                HelloApplication.textPool.put(action,HelloApplication.textPool.getOrDefault(action,"1").equals("1") ? "0" : "1");
                shouldFocus=!shouldFocus;
            } else if (action.contains("?")) {
                action = action.replace("?","");
                HelloApplication.textPool.put(action.split(":")[0],action.split(":")[1]);
                HelloApplication.actionButton(action.split(":")[0]+":"+action.split(":")[1],this);
                shouldFocus=!shouldFocus;
            } else {
                HelloApplication.actionButton(action, this);

            }
        }

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
        return text + " " + promptText + " menubutton";
    }



}
