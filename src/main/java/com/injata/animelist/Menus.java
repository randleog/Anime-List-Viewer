package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Menus {


    public static ArrayList<MenuElement> getFirstMenu() {
        ArrayList<MenuElement> menu = new ArrayList<>();
        menu.add(new MenuTextField("","Enter Username", "timeline", 500,500,300,100));
        MenuTextField errorInfo = new MenuTextField("Searching for Anilist Profile...","","Error",400,700,1,1);
        errorInfo.textPoolRef="Error";
        menu.add(errorInfo);
        return menu;
    }

    public static ArrayList<MenuElement> getTimelineMenu() {
        ArrayList<MenuElement> menu = new ArrayList<>();
        menu.add(new MenuButton("back","back","back",0,0,150,100));
     //   menu.add(new MenuAnimeTimeline(0,0));
        return menu;
    }


}
