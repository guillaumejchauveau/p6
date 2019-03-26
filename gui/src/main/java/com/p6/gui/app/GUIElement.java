package com.p6.gui.app;

import java.awt.*;

public class GUIElement {

  private int coordX;
  private int coordY;
  private Color color;

  public GUIElement(int x, int y, Color color){
    this.coordX = x;
    this.coordY = y;
    this.color = color;
  }

  /* Setters */
  public void setColor(Color color){
    this.color = color;
  }
  public void setPosX(int x){
    this.coordX = x;
  }
  public void setPosY(int y){
    this.coordY = y;
  }

  /* Getters */
  public int getPosX(){
    return this.coordX;
  }
  public int getPosY(){
    return this.coordY;
  }
  public Color getColor(){
    return this.color;
  }
}
