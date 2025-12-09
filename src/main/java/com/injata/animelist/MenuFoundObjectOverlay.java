package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MenuFoundObjectOverlay extends MenuElement{
    private boolean hasFoundItem = false;
    public MenuFoundObjectOverlay(int x, int y) {
        super(x, y);
    }

    public void foundItem() {
        this.hasFoundItem = true;
    }
    public void notFoundItem() {
        this.hasFoundItem = false;
    }


    @Override
    public void drawElement(GraphicsContext g) {

        if (hasFoundItem) {

            double xpos = getVisibleX();
            double ypos = getVisibleY();
            cacheX=xpos;
            cacheY=ypos;
            g.setFill(Color.YELLOW);
            g.fillRect(cacheX,cacheY,300,125);
            this.width = 0;
            this.height = 0;
            this.x = 0;
            this.y = 0;
         //   hasFoundItem=false;
        }
    }

    @Override
    public boolean interactElement(String info, boolean mouseDown, double xp, double yp) {
      //  if (mouseDown) {
      //      hasFoundItem=false;
      //  }
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
        return null;
    }
}
