package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Menus {


    public static MenuPage getFirstMenu() {
        ArrayList<MenuElement> menu = new ArrayList<>();

        menu.add(new MenuTextField("","Enter Username", "timeline", 0,0,300,100,MenuDirections.CENTER));



        MenuTextField errorInfo = new MenuTextField("Searching for Anilist Profile...","","Error",0,300,1,1,MenuDirections.CENTER);
        errorInfo.textPoolRef="Error";
        menu.add(errorInfo);

        MenuPage menuPage = new MenuPage(0,0);
        menuPage.setCurrentMenu(menu);
        return menuPage;

    }

    public static MenuPage getTimelineMenu() {
        ArrayList<MenuElement> menu = new ArrayList<>();

        menu.add(new MenuCheckbox("score","sort",50,0,150, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("start","sort",50,0,225, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("finish","sort",50,0,300, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("timewatching","sort",50,0,375, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("episodes","sort",50,0,450, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("rewatches","sort",50,0,525, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("pace","sort",50,0,600, MenuDirections.TOP_RIGHT));
        menu.add(new MenuToggleButton("Reverse","reverse","sort_reverse",0,700,100,75, MenuDirections.TOP_RIGHT));

        MenuPage timelineSettingsTab = new MenuPage("timeline_Settings_Tab",0,0);
        timelineSettingsTab.setCurrentMenu(menu);
     //   menu.add(new MenuAnimeTimeline(0,0));
        MenuPage menuPage = new MenuPage(0,0);
        ArrayList<MenuElement> menu2 = new ArrayList<>();
        menu2.add(timelineSettingsTab);
        menu2.add(new MenuButton("back","back","back",0,0,150,100, MenuDirections.BOTTOM_RIGHT));


        menu2.add(new MenuToggleButton("settings","settings","timeline_Settings_Tab_visibility",0,0,150,75, MenuDirections.TOP_RIGHT));
        menuPage.setCurrentMenu(menu2);
        return menuPage;
    }


}
