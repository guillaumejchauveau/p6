package com.p6.gui.app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Panel extends JPanel {

  public ArrayList<GUIElement> list;

  public Panel(ArrayList<GUIElement> list){
    this.list = list;
  }

  public void paintComponent(Graphics g){
    //System.out.println("Executing !");

    /* Drawing background */
    g.setColor(Color.white);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    for(int i = 0;i < list.size();i++){

      /* Drawing an element */
      g.setColor(list.get(i).getColor());
      g.fillOval(list.get(i).getPosX(), list.get(i).getPosY(), 10, 10);
      g.setColor(Color.black);
      g.drawOval(list.get(i).getPosX(), list.get(i).getPosY(), 10, 10);
    }
  }
}

