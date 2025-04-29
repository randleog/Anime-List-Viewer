package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;

public class MenuText extends MenuElement {
    public String text;
    public MenuText(String text, int x, int y) {
        super(x, y);

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
