package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MenuSlider extends MenuElement{



    private double lowestX = 10000;
    private double highestX = 0;

    private double lowestY = 10000;
    private double highestY = 0;

    public int maxHeight = 10000;
    public int maxWidth = 20000;

    private double scrollHeight =0;
    private double scrollWidth =0;

    private boolean isHover = false;


    private double offsety = 0;



    public MenuSlider(int x, int y) {
        super(x, y);
    }

    public double getOffset() {

            return offsety;
    }

    public void setUpElementHeights(MenuElement element) {
        if (maxHeight < 10000) {

            if (element.x + element.getWidth() > this.highestX) {
                highestX = element.x + element.getWidth();
            }
            if (element.y + element.getHeight() > this.highestY) {
                highestY = element.y + element.getHeight();
            }

            if (element.x < this.lowestX) {
                lowestX = element.x;
            }
            if (element.y < this.lowestY) {
                lowestY = element.y;
            }

            parent.width = Math.min(highestX - lowestX, maxWidth);
            parent.height = Math.min(highestY - lowestY, maxHeight);
            scrollWidth = highestX - lowestX - parent.width;
            scrollHeight = highestY - lowestY - parent.height;
        } else {
            parent.height = maxHeight;
        }


    }
    @Override
    public void drawElement(GraphicsContext g) {
        if (scrollHeight ==0) {
           // System.out.println("scroll height too small: " + maxHeight);
            return;
        }

        cacheX = getVisibleX();
        cacheY= getVisibleY()+getOffset();
        System.out.println(cacheX + " " + cacheY);
        g.setFill(Color.rgb(255, 255, 255, 0.05));
        g.fillRect(cacheX, cacheY, 50, parent.getVisibleHeight());

        g.strokeRect(cacheX, cacheY+offsety, width, (scrollHeight/(scrollHeight+parent.height))*parent.height);
    }

    @Override
    public boolean interactElement(String info, boolean mouseDown, double xp, double yp) {
       // System.out.println(cacheX+ " " + width);

        if (xp < cacheX+width && xp > cacheX && yp <cacheY+ parent.getVisibleHeight()&& yp > cacheY) {
            isHover = true;


            return true;
        } else {
            isHover = false;
        }
        if (mouseDown && parent.focusedItem==this) {
            return true;
        }
        return false;
    }

    @Override
    public boolean scroll(double delta, double xp, double yp) {
        return false;
    }

    @Override
    public boolean drag(double xp, double yp) {
        if (parent.focusedItem==this) {
            cacheY-=getOffset();
            offsety = yp - getVisibleY()-getOffset()-((scrollHeight/(scrollHeight+parent.height))*parent.height)/2;

            if (offsety > parent.height-(scrollHeight/(scrollHeight+parent.height))*parent.height) {
                offsety = parent.height-(scrollHeight/(scrollHeight+parent.height))*parent.height;
            }

            if (offsety < 0) {
                offsety = 0;
            }
            cacheY+=getOffset();;
            System.out.println(getOffset());
            return true;
        }


        return false;
    }

    @Override
    public boolean mouseRelease(double xp, double yp) {
        if (parent.focusedItem==this) {
            parent.focusedItem = null;
        }
        return false;
    }

    @Override
    public boolean mouseDown(double xp, double yp) {
        if (xp < cacheX+width && xp > cacheX && yp <cacheY+ parent.getVisibleHeight()&& yp > cacheY) {
            parent.focusedItem = this;
            return true;
        }
        return false;
    }

    @Override
    public String getInfo() {
        return "slider";
    }
}
