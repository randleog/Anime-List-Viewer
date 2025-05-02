package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuCheckbox extends MenuElement{


    private String text;
    private boolean isHover = false;
    private String keyText;
    private double baseWidth;
    private final double fontSize = 30;


    public MenuCheckbox(String text, String key,int size,int x, int y, MenuDirections direction) {
        super(x, y, direction);

        this.height =size;
        this.baseWidth =size;
        this.width = ((text.length()+4)*fontSize/1.6666667)+size;

        this.text = text;
        this.keyText = key;
    }

    @Override
    public void drawElement(GraphicsContext g) {
        if (isHover) {
            g.setFill(Color.rgb(255, 255, 255, 0.2));

        } else {
            g.setFill(Color.rgb(255, 255, 255, 0.1));
        }
        cacheX = direction.getDrawX(this,g.getCanvas().getWidth());
        cacheY = direction.getDrawY(this,g.getCanvas().getHeight());
        g.fillRect(cacheX+width-baseWidth,cacheY,this.height,this.height);
        g.setFont(Font.font("monospace",fontSize));
        g.setFill(Color.WHITE);
        g.fillText(text, cacheX, cacheY+fontSize);

        if (HelloApplication.textPool.getOrDefault(keyText,text).equals(text)) {
            g.fillRoundRect(cacheX+this.height*0.10+width-baseWidth,cacheY+this.height*0.10,this.height*0.8,this.height*0.8, 10, 10);
        }
    }

    @Override
    public boolean interactElement(String info, boolean mouseDown, double xp, double yp) {
        boolean prevHover = isHover;
        if (xp < cacheX+width && xp > cacheX && yp < height+cacheY && yp > cacheY) {
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
