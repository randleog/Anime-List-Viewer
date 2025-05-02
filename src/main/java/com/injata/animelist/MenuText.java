package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuText extends MenuElement {
    public String text;
    public double fontSize= 30;
    public MenuText(String text, int x, int y, MenuDirections direction) {
        super(x, y, direction);
        this.width = Math.max(((text.length()+4)*fontSize/1.6666667),this.width);
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return this.text;
    }

    @Override
    public void drawElement(GraphicsContext g) {
        cacheX=direction.getDrawX(this, g.getCanvas().getWidth());
        cacheY=direction.getDrawY(this,g.getCanvas().getHeight());

        g.setFont(Font.font("monospace",fontSize));
        g.fillText(text, cacheX, cacheY);
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
        return "";
    }
}
