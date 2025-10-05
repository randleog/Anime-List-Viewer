package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class Menus {


    public static MenuPage getFirstMenu() {
        ArrayList<MenuElement> menu = new ArrayList<>();

        menu.add(new MenuTextField("","Enter Username", "timeline", 0,0,300,100,MenuDirections.CENTER));



        MenuText errorInfo = new MenuText("",-300,300,"Error",MenuDirections.CENTER);

        menu.add(errorInfo);

        MenuPage menuPage = new MenuPage(0,0);
        menuPage.setWidth(HelloApplication.CANVAS_WIDTH);
        menuPage.setHeight(HelloApplication.CANVAS_HEIGHT);
        menuPage.direction = MenuDirections.TOP_LEFT;
        menuPage.setCurrentMenu(menu);
        return menuPage;

    }

    public static MenuPage getTimelineMenu() {
        ArrayList<MenuElement> menu = new ArrayList<>();

        menu.add(new MenuCheckbox("score","sort",50,0,75, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("start","sort",50,0,150, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("finish","sort",50,0,225,MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("timewatching","sort",50,0,300, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("episodes","sort",50,0,375, MenuDirections.TOP_RIGHT));
        menu.add(new MenuCheckbox("rewatches","sort",50,0,450, MenuDirections.TOP_RIGHT));
       menu.add(new MenuCheckbox("pace","sort",50,0,525, MenuDirections.TOP_RIGHT));

        menu.add(new MenuButton("Reverse","reverse",0,650,100,75, MenuDirections.TOP_RIGHT, true, "!sort_reverse","updateList"));
        menu.add(new MenuButton("include Planning","",0,750,100,75, MenuDirections.TOP_RIGHT, true, "!includestatus_PLANNING","updateList"));
        menu.add(new MenuButton("include Dropped","",0,850,100,75, MenuDirections.TOP_RIGHT, true,"!includestatus_DROPPED","updateList"));
        menu.add(new MenuButton("include Paused","",0,950,100,75, MenuDirections.TOP_RIGHT, true,"!includestatus_PAUSED","updateList"));
        menu.add(new MenuButton("include Watching","",0,1050,100,75, MenuDirections.TOP_RIGHT, true,"!includestatus_CURRENT","updateList"));
        menu.add(new MenuButton("include Completed","",0,1150,100,75, MenuDirections.TOP_RIGHT, true,"!includestatus_COMPLETED","updateList"));
        menu.add(new MenuButton("include Manga","",0,1250,100,75, MenuDirections.TOP_RIGHT, true,"!includetype_Manga","updateList"));

        MenuPage timelineSettingsTab = new MenuPage("timeline_Settings_Tab",0,75, 800, MenuDirections.TOP_RIGHT);

        timelineSettingsTab.itemsStacking=true;
        timelineSettingsTab.setCurrentMenu(menu);

        MenuPage menuPage = new MenuPage(0,0);
        ArrayList<MenuElement> menu2 = new ArrayList<>();
        menu2.add(timelineSettingsTab);
      //  menu2.add(new MenuText("zoom: ",200,10,"zoom", MenuDirections.TOP_RIGHT));



        menu2.add(new MenuToggleButton("settings","settings","timeline_Settings_Tab_visibility",0,0,150,75, MenuDirections.TOP_RIGHT));
        menu2.add(new MenuText("",265,50,"searchResults",MenuDirections.TOP_RIGHT));
        HelloApplication.updateTextPool(false,"searchResults","");
        HelloApplication.updateTextPool(false,"animation_level","2");
        menu2.add(new MenuTextField("","search", "search",350, 0,150,75,MenuDirections.TOP_RIGHT));
        menuPage.setCurrentMenu(menu2);
        menuPage.setWidth(HelloApplication.CANVAS_WIDTH);
        menuPage.setHeight(HelloApplication.CANVAS_HEIGHT);
        return menuPage;
    }


}
