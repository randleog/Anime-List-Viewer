package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MenuPage extends MenuElement{


    private ArrayList<MenuElement> currentMenu;

    private String visibleKey;
    private String freezeKey;
    private String key;

    public MenuPage(int x, int y) {
        super(x, y);

        currentMenu  = new ArrayList<>();
    }

    public MenuPage(String key,int x, int y) {
        super(x, y);
        this.key = key;
        currentMenu  = new ArrayList<>();
        this.visibleKey = key + "_visibility";
        this.freezeKey = key + "_freeze";
    }

    public void addElement(MenuElement element) {
        element.parent = this;
        currentMenu.add(element);
    }

    public ArrayList<MenuElement> getCurrentMenu() {
        return currentMenu;
    }


    public void setCurrentMenu(ArrayList<MenuElement> currentMenu) {

       this.currentMenu = currentMenu;
       for (MenuElement element : this.currentMenu) {
           element.parent = this;
       }
    }
    @Override
    public void drawElement(GraphicsContext g) {
        if (!HelloApplication.textPool.getOrDefault(visibleKey,"1").equals("1")) {
            return;
        }
            for (MenuElement element : currentMenu) {
                element.drawElement(g);

                //     System.out.println("should have drawn " + element.getInfo());
            }

    }

    @Override
    public boolean interactElement(String info, boolean mouseDown, double xp, double yp) {
        if (!HelloApplication.textPool.getOrDefault(visibleKey,"1").equals("1")) {
            return false;
        }
        if (!HelloApplication.textPool.getOrDefault(freezeKey,"1").equals("1")) {
            return false;
        }
        boolean shouldUpdate = false;
        for (MenuElement element : currentMenu) {

            shouldUpdate = element.interactElement(info,mouseDown,xp,yp);
            if (shouldUpdate) {
                return true;
            }
            //     System.out.println("should have drawn " + element.getInfo());
        }
        return shouldUpdate;
    }

    @Override
    public boolean scroll(double delta, double xp, double yp) {
        if (!HelloApplication.textPool.getOrDefault(visibleKey,"1").equals("1")) {
            return false;
        }
        if (!HelloApplication.textPool.getOrDefault(freezeKey,"1").equals("1")) {
            return false;
        }
        boolean shouldUpdate = false;
        for (MenuElement element : currentMenu) {
            shouldUpdate = element.scroll(delta,xp,yp);

            //     System.out.println("should have drawn " + element.getInfo());
        }
        return shouldUpdate;
    }

    @Override
    public boolean drag(double xp, double yp) {
        if (!HelloApplication.textPool.getOrDefault(visibleKey,"1").equals("1")) {
            return false;
        }
        if (!HelloApplication.textPool.getOrDefault(freezeKey,"1").equals("1")) {
            return false;
        }
        boolean shouldUpdate = false;
        for (MenuElement element : currentMenu) {
            shouldUpdate = element.drag(xp,yp);

            //     System.out.println("should have drawn " + element.getInfo());
        }
        return shouldUpdate;
    }

    @Override
    public boolean mouseRelease(double xp, double yp) {
        if (!HelloApplication.textPool.getOrDefault(visibleKey,"1").equals("1")) {
            return false;
        }
        if (!HelloApplication.textPool.getOrDefault(freezeKey,"1").equals("1")) {
            return false;
        }
        boolean shouldUpdate = false;
        for (MenuElement element : currentMenu) {
            shouldUpdate = element.mouseRelease(xp,yp);

            //     System.out.println("should have drawn " + element.getInfo());
        }
        return shouldUpdate;
    }

    @Override
    public boolean mouseDown(double xp, double yp) {
        if (!HelloApplication.textPool.getOrDefault(visibleKey,"1").equals("1")) {
            return false;
        }
        if (!HelloApplication.textPool.getOrDefault(freezeKey,"1").equals("1")) {
            return false;
        }
        boolean shouldUpdate = false;
        for (MenuElement element : currentMenu) {
            shouldUpdate = element.mouseDown(xp,yp);

            //     System.out.println("should have drawn " + element.getInfo());
        }
        return shouldUpdate;

    }

    @Override
    public String getInfo() {
        return "";
    }
}
