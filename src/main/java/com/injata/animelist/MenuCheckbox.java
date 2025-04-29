package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuCheckbox extends MenuElement{


    private int size;
    private String text;
    private boolean isHover = false;
    private String keyText;


    public MenuCheckbox(String text, String key,int size,int x, int y) {
        super(x, y);
        this.size = size;
        this.text = text;
        this.keyText = key;
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
        g.fillRect(xpos,ypos,this.size,this.size);
        g.setFont(Font.font("monospace",30));
        g.setFill(Color.WHITE);
        g.fillText(text, xpos+size+5, ypos-100);
        if (HelloApplication.textPool.getOrDefault(keyText,text).equals(text)) {
            g.fillRect(xpos+5,ypos-5,this.size-10,this.size-10);
        }
    }

    @Override
    public boolean interactElement(String info, boolean mouseDown, double xp, double yp) {
        boolean prevHover = isHover;
        if (xp < x+size && xp > x && yp < y+size && yp > y) {
            isHover = true;
            if (mouseDown) {
                HelloApplication.textPool.put(keyText,text);
                HelloApplication.actionButton(keyText+":"+text,this);
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

    @Override
    public String getInfo() {
        return "";
    }
}
