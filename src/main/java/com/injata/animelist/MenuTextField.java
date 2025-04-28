package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuTextField extends MenuElement{
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 75;

    public boolean isSelected = false;


    public static final int TEXT_HEIGHT = 20;

    private int width;
    private int height;
    private String promptText;
    private String text;
    private boolean isHover = false;
    public String keyText;

    public String textPoolRef;

    private boolean isShift = false;
    public MenuTextField(int x, int y) {
        super(x,y);

        this.width =DEFAULT_WIDTH;
        this.height =DEFAULT_HEIGHT;
        this.promptText="";
        this.text = "";
        keyText = "";

    }

    public MenuTextField(String text, String promptText,int x, int y, int width, int height) {
        super(x,y);

        this.width =width;
        this.height =height;
        this.promptText=promptText;
        this.text = text;
        this.keyText = text;

    }
    public MenuTextField(String text, String promptText,String keyText,int x, int y, int width, int height) {
        super(x,y);

        this.width =width;
        this.height =height;
        this.promptText=promptText;
        this.text = text;
        this.keyText = keyText;

    }

    @Override
    public void drawElement(GraphicsContext g) {


        if (HelloApplication.focusedItem ==this) {
            g.setFill(Color.rgb(255, 255, 255, 0.1));

        } else {
            g.setFill(Color.rgb(0, 0, 0, 0.5));
        }
        double xpos = this.x;
        double ypos = this.y;
        g.fillRect(xpos,ypos,this.width,this.height);
        g.setFont(Font.font("monospace",30));
        if (textPoolRef !=null) {
         //   System.out.println("should be printing rn " + HelloApplication.textPool.get(textPoolRef) + " + " + textPoolRef );
            g.setFill(Color.WHITE);
            g.fillText(HelloApplication.textPool.getOrDefault(textPoolRef,""), xpos + TEXT_HEIGHT, ypos + height - TEXT_HEIGHT);
        } else {
            if (text.isEmpty()) {
                g.setFill(Color.GRAY);
                g.fillText(promptText, xpos + TEXT_HEIGHT, ypos + height - TEXT_HEIGHT);
            } else {
                g.setFill(Color.WHITE);
                g.fillText(text, xpos + TEXT_HEIGHT, ypos + height - TEXT_HEIGHT);
            }
        }

    }

    @Override
    public boolean interactElement(String info, boolean releasing, double xp, double yp) {


        if (info.isEmpty()) {
            boolean prevHover = isHover;
            if (xp < x + width && xp > x && yp < y + height && yp > y) {
                isHover = true;
                if (releasing) {
                    // triggerAction();
                    HelloApplication.focusedItem = this;
                }
            } else {
                isHover = false;
            }
            return isHover!=prevHover;
        } else {
            if (releasing) {
                releaseText(info);
            }else {
                typeText(info);
            }


        }

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

    private void releaseText(String text) {
        if (text.length() > 1) {
            switch (text) {
                case "BACK_SPACE" -> {

                }
                case "DELETE" -> {

                }
                case "ENTER" -> {

                }
                case "SHIFT" -> {
                    isShift = false;
                }
                default -> {
                    System.out.println("unhandled key release input for " + text);
                }
            }
        }
    }

    private void typeText(String text) {
        if (text.length() > 1) {
            switch (text) {
                case "BACK_SPACE" -> {
                    if (!this.text.isEmpty()) {
                        this.text = this.text.substring(0,this.text.length()-1);
                    }
                }
                case "DELETE" -> {
                    if (!this.text.isEmpty()) {
                        this.text = this.text.substring(1);
                    }
                }
                case "ENTER" -> {
                    HelloApplication.actionButton(keyText+":"+this.text, this);
                }
                case "SHIFT" -> {
                    isShift = true;
                }
                default-> {
                    System.out.println("unhandled key press input for " + text);
                }
            }
            return;
        }
        this.text = this.text +(isShift ? text : text.toLowerCase());


        //DELETE
        //ENTER
    }

    public void triggerAction() {
      //  HelloApplication.actionButton(keyText, this);
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText(String text) {
        return this.text;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptText(String promptText) {
        return this.promptText;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public  int getHeight() {
        return this.height;
    }

    public String getInfo() {
        return text + " " + promptText;
    }



}
