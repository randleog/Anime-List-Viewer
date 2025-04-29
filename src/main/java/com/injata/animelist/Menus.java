package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Menus {


    public static MenuPage getFirstMenu() {
        ArrayList<MenuElement> menu = new ArrayList<>();
        menu.add(new MenuTextField("","Enter Username", "timeline", 500,500,300,100));
        MenuTextField errorInfo = new MenuTextField("Searching for Anilist Profile...","","Error",400,700,1,1);
        errorInfo.textPoolRef="Error";
        menu.add(errorInfo);

        MenuPage menuPage = new MenuPage(0,0);
        menuPage.setCurrentMenu(menu);
        return menuPage;

    }

    public static MenuPage getTimelineMenu() {
        ArrayList<MenuElement> menu = new ArrayList<>();
        menu.add(new MenuButton("back","back","back",0,0,150,100));
        menu.add(new MenuCheckbox("finish","sort",100,250,0));
        menu.add(new MenuCheckbox("score","sort",100,400,0));
     //   menu.add(new MenuAnimeTimeline(0,0));
        MenuPage menuPage = new MenuPage(0,0);
        menuPage.setCurrentMenu(menu);
        return menuPage;
    }


}
