package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MenuPage extends MenuElement{


    private ArrayList<MenuElement> currentMenu;

    private String visibleKey;
    private String freezeKey;
    private String key;
    public boolean itemsStacking = false;

    private MenuSlider menuSlider;

    private static int padding = 15;

    public MenuPage(int x, int y) {
        super(x, y);



        currentMenu  = new ArrayList<>();
    }

    public void setUpMenuSlider() {

        menuSlider = new MenuSlider(-75,0);
        x=x+75;
        menuSlider.direction = this.direction;


        menuSlider.height=50;
        menuSlider.width=50;
        menuSlider.parent=this;
        menuSlider.maxHeight=(int)this.height;
    }

    public MenuPage(String key,int x, int y) {
        super(x, y);
        this.key = key;
        currentMenu  = new ArrayList<>();
        this.visibleKey = key + "_visibility";

        this.freezeKey = key + "_freeze";

    }
    public MenuPage(String key,int x, int y, int maxHeight, MenuDirections direction) {
        super(x, y);
        this.direction = direction;
        this.height = maxHeight;
        this.key = key;
        currentMenu  = new ArrayList<>();
        this.visibleKey = key + "_visibility";
        this.freezeKey = key + "_freeze";

        setUpMenuSlider();

    }


    public void setUpElement(MenuElement element) {
        if (menuSlider !=null) {
            menuSlider.setUpElementHeights(element);

        }

        element.parent = this;

    }

    @Override
    public double getY() {
        if (menuSlider==null) {
            return this.y;
        }

        return this.y- menuSlider.getOffset();
    }

    @Override
    public double getVisibleHeight() {
        return Math.min(HelloApplication.getCanvasHeight(),this.height);
    }

    public void addElement(MenuElement element) {

        setUpElement(element);

        currentMenu.add(element);
    }



    public ArrayList<MenuElement> getCurrentMenu() {
        return currentMenu;
    }


    public void setCurrentMenu(ArrayList<MenuElement> currentMenu) {

       this.currentMenu = currentMenu;
    //   System.out.println(currentMenu.size());
       for (MenuElement element : currentMenu) {

           setUpElement(element);
          // addElement(element);
       }

    }
    @Override
    public void drawElement(GraphicsContext g) {

        if (!HelloApplication.textPool.getOrDefault(visibleKey,"1").equals("1")) {
            return;
        }
        double offset = 0;
        if (menuSlider!=null) {
            offset =  menuSlider.getOffset();
        }

            for (MenuElement element : currentMenu) {

                //      System.out.println(element.getVisibleY() + " " + getVisibleY() + " " +getVisibleHeight());
               // element.drawElement(g);

                //     System.out.println(element.getInfo() + element.getVisibleX() + " " + element.getVisibleY());

                if (element.getVisibleY()- element.getHeight() < getVisibleY()+getVisibleHeight()+ offset) {

                    element.drawElement(g);
                }

                //     System.out.println("should have drawn " + element.getInfo());
            }
        if (menuSlider !=null) {
            menuSlider.drawElement(g);
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
        if (menuSlider !=null) {
            if (menuSlider.interactElement(info,mouseDown,xp,yp) ) {
                return true;
            }
        }


        for (MenuElement element : currentMenu) {

            shouldUpdate = element.interactElement(info,mouseDown,xp,yp);
            if (shouldUpdate) {

                return true;
            }
            //     System.out.println("should have drawn " + element.getInfo());
        }
     //   if (mouseDown) {
      //      focusedItem = null;
      //  }
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
        if (menuSlider !=null) {
            menuSlider.scroll(delta,xp,yp);
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
        if (menuSlider !=null) {
            if (menuSlider.drag(xp,yp)){

                return true;
            }

        }

        boolean shouldUpdate = false;
        for (MenuElement element : currentMenu) {
            shouldUpdate = element.drag(xp,yp);

            if (shouldUpdate)  {
                return true;
            }
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
        if (menuSlider !=null) {
            menuSlider.mouseRelease(xp,yp);
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
        if (menuSlider !=null) {
            if (menuSlider.mouseDown(xp,yp)) {
                return true;
            }
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
        return "menu page";
    }
}
