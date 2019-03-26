package com.p6.gui.app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Window extends JFrame {

  public ArrayList<GUIElement> elementList = new ArrayList<>();
  public ArrayList<GUIElement> toRepaintList = new ArrayList<>();
  public ArrayList<GUIElement> toReactList = new ArrayList<>();
  private Panel panel = new Panel(elementList);
  public boolean end = false;

  public Window(){

    this.setTitle("P6_Window");
    this.setSize(500, 500);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);

    Random random = new Random();
    for(int i =0; i < 100; i++){
      int x = random.nextInt(450);
      int y = random.nextInt(450);
      GUIElement element = new GUIElement(x,y,Color.green);
      elementList.add(element);
    }

    this.setContentPane(panel);
    this.setVisible(true);

    //react(ElementList.get(1),ElementList.get(2), 250, 250);
    moveElements(elementList);
  }

  private void moveElement(GUIElement element, int x, int y){

    elementList.get(1).setColor(Color.blue);
    elementList.get(2).setColor(Color.blue);

    while(element.getPosX()!=x || element.getPosY() != y){

      int coordX = element.getPosX(), coordY = element.getPosY();

      if(coordX<x){
        coordX = coordX + 1;
        element.setPosX(coordX);
      }else if(coordX>x){
        coordX = coordX - 1;
        element.setPosX(coordX);
      }

      if(coordY<y){
        coordY = coordY + 1;
        element.setPosY(coordY);
      }else if(coordY>y){
        coordY = coordY - 1;
        element.setPosY(coordY );
      }

      // Drawing the panel again
      panel.repaint();

      // Setting pauses between each move to see a movement
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void react(GUIElement element1, GUIElement element2, int x, int y){

    moveElement(element1,x,y);
    moveElement(element2,x,y);

    elementList.remove(element1);
    elementList.remove(element2);
    System.out.println("1,2 removed");
    panel.repaint();

    GUIElement product = new GUIElement(250, 250, Color.pink);
    elementList.add(product);
    System.out.println("product added");
    panel.repaint();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    product.setColor(Color.green);
    panel.repaint();

  }

  public void newReact(ArrayList<GUIElement> toReactList, int x, int y){

    for(int i = 0; i < toReactList.size(); i++){
      while(toReactList.get(i).getPosX()!=x || toReactList.get(i).getPosY() != y){

        int coordX = toReactList.get(i).getPosX(), coordY = toReactList.get(i).getPosY();

        if(coordX<x){
          coordX = coordX + 1;
        }else if(coordX>x){
          coordX = coordX - 1;
        }

        if(coordY<y){
          coordY = coordY + 1;
        }else if(coordY>y){
          coordY = coordY - 1;
        }

        toReactList.get(i).setPosX(coordX);
        toReactList.get(i).setPosY(coordY );
        toRepaintList.add(toReactList.get(i));
      }
    }
  }

  public void moveElements(ArrayList<GUIElement> elementList){

    while(end==false){

      Random randMoves = new Random();
      for(int i = 0; i < elementList.size(); i++){
        int x = randMoves.nextInt(2);
        int y = randMoves.nextInt(2);
        if(x==1){
          elementList.get(i).setPosX(elementList.get(i).getPosX() + 10);
        } else if(x==-1){
          elementList.get(i).setPosX(elementList.get(i).getPosX() - 10);
        }
        if(y==1){
          elementList.get(i).setPosY(elementList.get(i).getPosY() + 10);
        } else if(y==-1){
          elementList.get(i).setPosY(elementList.get(i).getPosY() - 10);
        }
        toRepaintList.add(elementList.get(i));
      }
      /*toReactList.add(elementList.get(1));
      toReactList.add(elementList.get(2));
      newReact(toReactList, 250, 250);*/

      Repaint(toRepaintList);
    }
  }


  public void Repaint(ArrayList<GUIElement> toRepaintList){

    for(int i = 0; i < toRepaintList.size(); i++){

      elementList.get(i).setPosX(toRepaintList.get(i).getPosX());
      elementList.get(i).setPosY(toRepaintList.get(i).getPosY());
      elementList.get(i).setColor(toRepaintList.get(i).getColor());
    }

    System.out.println("Moving");
    panel.repaint();

    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
