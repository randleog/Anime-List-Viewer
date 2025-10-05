package com.injata.animelist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.Clipboard;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuTextField extends MenuElement {
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 75;


    public static final int TEXT_HEIGHT = 20;


    private String promptText;
    private String text;
    private boolean isHover = false;
    public String keyText;

    public String textPoolRef;

    public double fontSize = 30;

    private int baseWidth = 0;
    private boolean isControl = false;
    private boolean isShift = false;

    public MenuTextField(int x, int y) {
        super(x, y);

        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.baseWidth = DEFAULT_WIDTH;
        this.promptText = "";
        this.text = "";
        keyText = "";

    }

    public MenuTextField(String text, String promptText, int x, int y, int width, int height) {
        super(x, y);

        this.width = width;
        this.height = height;
        this.promptText = promptText;
        this.baseWidth = width;
        this.text = text;
        this.keyText = text;

    }

    public MenuTextField(String text, String promptText, String keyText, int x, int y, int width, int height, MenuDirections direction) {
        super(x, y, direction);

        this.width = width;
        this.height = height;
        this.promptText = promptText;
        this.text = text;
        this.keyText = keyText;
        this.baseWidth = width;
    }

    public double getFontSize() {
        return this.fontSize;
    }

    @Override
    public void drawElement(GraphicsContext g) {
        cacheX = getVisibleX();
        cacheY = getVisibleY();

        if (this.parent.focusedItem == this) {
            g.setStroke(Color.WHITE);
            g.strokeRect(cacheX, cacheY, this.width, this.height);

        } else if (isHover) {
            g.setFill(Color.rgb(255, 255, 255, 0.1));

        } else {
            g.setFill(Color.rgb(0, 0, 0, 0.5));
        }

        g.setFill(Color.rgb(0, 0, 0, 0.1));
        g.fillRect(cacheX, cacheY, this.width, this.height);
        g.setFont(Font.font("monospace", fontSize));
        if (textPoolRef != null) {
            //   System.out.println("should be printing rn " + HelloApplication.textPool.get(textPoolRef) + " + " + textPoolRef );
            g.setFill(Color.WHITE);
            g.fillText(HelloApplication.textPool.getOrDefault(textPoolRef, ""), cacheX + TEXT_HEIGHT, cacheY + height - TEXT_HEIGHT);
        } else {
            if (text.isEmpty()) {
                g.setFill(Color.GRAY);
                g.fillText(promptText, cacheX + TEXT_HEIGHT, cacheY + height - TEXT_HEIGHT);
            } else {
                g.setFill(Color.WHITE);
                g.fillText(text, cacheX + TEXT_HEIGHT, cacheY + height - TEXT_HEIGHT);
            }
        }
        g.fillRect(cacheX+((caret) * fontSize / 1.6666667) + TEXT_HEIGHT,cacheY + height - TEXT_HEIGHT+5,fontSize / 1.6666667,2);

    }

    @Override
    public boolean interactElement(String info, boolean releasing, double xp, double yp) {


        if (info.isEmpty()) {
            boolean prevHover = isHover;

            if (xp < cacheX + width && xp > cacheX && yp < cacheY + height && yp > cacheY) {
                isHover = true;

                if (releasing) {

                    // triggerAction();

                    if (this.parent.focusedItem == this) {
                        //     this.parent.focusedItem = null;
                    } else {
                        this.parent.focusedItem = this;
                    }
                }
            } else {
                isHover = false;
            }
            return isHover != prevHover;
        } else {
            if (releasing) {
                releaseText(info);
            } else {
                typeText(info);
                this.width = Math.max(this.baseWidth, ((text.length() + 4) * fontSize / 1.6666667));
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
        if (parent != null && this.parent.focusedItem != this) {
            return;
        }
        if (text.length() > 1) {
            switch (text) {
                case "BACK_SPACE" -> {

                }
                case "DELETE" -> {

                }
                case "ENTER" -> {

                }
                case "CONTROL" -> {
                    isControl = false;
                }
                case "SHIFT" -> {
                    isShift = false;
                }
                default -> {
                    //System.out.println("unhandled key release input for " + text);
                }
            }
        }
    }

    private void signalUpdatedText() {
        HelloApplication.actionButton(keyText + "_" + "違お:" + text, this);
    }

    private int caret = 0;

    private void typeText(String text) {
        if (parent != null && this.parent.focusedItem != this) {
            return;
        }


        if (text.length() > 1) {
            switch (text) {
                case "SPACE" -> {
                    addText(" ");
                }
                case "BACK_SPACE" -> {

                    if (!this.text.isEmpty()) {

                        if (this.text.length()-1< caret) {

                        }if (this.text.length()-1== caret) {
                            this.text = this.text.substring(0, caret);
                        } else {
                            this.text = this.text.substring(0, caret-1)+this.text.substring(caret );

                        }
                        caret--;
                    }
                }
                case "DELETE" -> {

                    if (!this.text.isEmpty()) {
                        if (this.text.length()-1< caret) {

                        }if (this.text.length()-1== caret) {
                            this.text = this.text.substring(0, caret);
                        } else {
                            this.text = this.text.substring(0, caret)+this.text.substring(caret +1);

                        }
                        signalUpdatedText();
                    }
                }
                case "ENTER" -> {
                    HelloApplication.actionButton(keyText + ":" + this.text, this);
                }
                case "CONTROL" -> {
                    isControl = true;
                }
                case "LEFT" -> {
                    caret--;

                }
                case "RIGHT" -> {
                    caret++;

                }

                case "SHIFT" -> {
                    isShift = true;
                }
                default -> {

                    // System.out.println("unhandled key press input for " + text);
                }
            }
            caret = Math.max(Math.min(caret,this.text.length()),0);
            return;
        }
        if (text.equals("V") && isControl) {
            text = Clipboard.getSystemClipboard().getString();

        }

        addText(text);
        //DELETE
        //ENTER
    }

    private void addText(String text) {
        caret +=text.length();

        this.text = this.text.substring(0, caret-1)+(isShift ? text : text.toLowerCase())+this.text.substring(caret-1);
        caret = Math.min(caret,this.text.length());

        signalUpdatedText();
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


    public String getInfo() {
        return text + " " + promptText + " menutext";
    }


}
