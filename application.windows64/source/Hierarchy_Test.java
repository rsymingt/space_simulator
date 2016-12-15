import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.JOptionPane; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Hierarchy_Test extends PApplet {

GameEngine game;

double metersC;
ArrayList<Object> objects;
 

float timeSpeed;
int timePassed;
double zoomVelConstant;
float years;
float planetMult;
ArrayList<Button> menuButtons;

float xC;
float yC;

boolean lockScreen = true;

public void setup() {
  size(displayWidth, displayHeight);
  frameRate(60);
  if(frame != null)
  frame.setResizable(true);
  objects = new ArrayList<Object>();
  timeSpeed = 525949/60;
  zoomVelConstant = 1000;
  years = 0;
  planetMult = 50;
  timePassed = 0;
  menuButtons = new ArrayList();
  
  xC = 0;
  yC = 0;
  
  menuButtons.add(new Button("None", new PVector(xC, yC), 32, color(0), color(255, 0, 0)));
  xC += menuButtons.get(menuButtons.size()-1).getWidth()+25;

  game = new GameEngine();
  metersC = 8.333333f * pow(10, -7);
}

public void draw() {
  game.runGame();
}

public void recieve(Object o) {
  objects.add(o);
  menuButtons.add(new Button(((Ellipse)o).nameGet(), new PVector(xC, yC), 32, color(0), color(255, 0, 0)));
  xC += menuButtons.get(menuButtons.size()-1).getWidth()+25;
  if(xC >= width-menuButtons.get(menuButtons.size()-1).getWidth()){
    xC = 0;
    yC += 40;
  }
}
class Button {
  PVector pos;
  int textColor, hoverColor;
  float size, tWidth;
  String text;

  Button(String text, PVector pos, float size, int textColor, int hoverColor) {
    this.pos = pos;
    this.textColor = textColor;
    this.hoverColor = hoverColor;
    this.size = size;
    this.text = text;
    textSize(size);
    tWidth = textWidth(text);
  }
  
  public void draw(boolean on) {
    textSize(size);
    if (containsMouse()) fill(hoverColor);
    else fill(textColor);
    text(text, pos.x, pos.y + size);
    if (on)
      rect(pos.x, pos.y, tWidth, size);
  }
  
  public boolean containsMouse() {
    if (mouseX > pos.x && mouseX < pos.x + tWidth && mouseY > pos.y && mouseY < pos.y + size )
      return true;
    else return false;
  }
  
  public float getWidth(){
    return tWidth;
  }
}
class Camera {

  PVector location, locationB;
  float zoom;
  boolean calculating;
  int selected;

  Camera() {
    location = new PVector(-width/2, -height/2);
    locationB = new PVector(0, 0);
    
    selected = -1;

    zoom = 0.05f;
    calculating = false;

    addMouseWheelListener(new java.awt.event.MouseWheelListener() { 
      public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) { 
        mouseWheel(evt.getWheelRotation());
      }
    }
    );
  }

  public void run() {
    //check change camera
    checkMouseScroll();
    
    if(selected != -1){
      location.x = (float)(((Ellipse)objects.get(selected)).getLoc().x * metersC );
      location.y = (float)(((Ellipse)objects.get(selected)).getLoc().y * metersC );
    }
      translate(-location.x, -location.y);
      translate(width/2+location.x, height/2+location.y);
      scale(zoom);
      translate(-width/2-location.x, -height/2-location.y);
      println(zoom);
  }

  public void checkMouseScroll() {
    if ((mousePressed && mouseButton == LEFT) || calculating) {
      if (!calculating) {
        locationB.set(mouseX, mouseY);
        calculating = true;
      } else if (calculating) {
        location.add(PVector.div(PVector.sub(locationB, new PVector(mouseX, mouseY)), zoom));
        calculating = false;
      }
    }
  }
  public void mouseWheel(int delta) {
    if (delta < 0) {
      zoom *= 1.2f;
    } else if (delta > 0) {
      zoom *= 0.8f;
    }
  }

  public float getZoom() {
    return zoom;
  }

  public PVector getLoc() {
    return location;
  }
  
  public void select(int e){
    selected = e;
  }
}
class Create {

  PVector location;
  PVector checkReDisplay;

  ArrayList<PVector> predictedLine;
  boolean first;

  ArrayList<Object> o;
  ArrayList<Object> checking;

  Create(float x, float y) {
    location = new PVector(x, y);
    predictedLine = new ArrayList<PVector>();
    checkReDisplay = new PVector(0, 0);
    
    checking = new ArrayList<Object>();
    o = new ArrayList<Object>();
    first = true;
  }

  public void displayCurrentLine() {
    Camera c = game.getCamera();

    float endX = c.getLoc().x+ mouseX/c.getZoom() - (width/2)/c.getZoom() + width/2;
    float endY = c.getLoc().y+ mouseY/c.getZoom() - (height/2)/c.getZoom() + height/2;
    stroke(255);
    line((float)(location.x), (float)(location.y), endX, endY);

    PVector vel = new PVector(endX-location.x, endY - location.y);
    PVector loc = new PVector((float)(location.x/metersC), (float)(location.y/metersC));
    loc.div(1000000000);
    vel.div(1000);

    String lX = nf(abs(loc.x), 1, 2);
    String lY = nf(abs(loc.y), 1, 2);
    String v = nf(vel.mag(), 1, 2);

    vel.mult(1000);
    vel.div(c.getZoom()*(float)zoomVelConstant);

    if (first) {
      
      checking.clear();
      o.clear();
      predictedLine.clear();
      
      checking.add(new Ellipse((float)(location.x/metersC), (float)(location.y/metersC), game.getCreatedMass(), game.getCreatedRadius(), vel.x, vel.y, "Sun"));

      for (int i = 0; i < objects.size (); i ++) {
        PVector v1 = ((Ellipse)objects.get(i)).getVel();
        PVector v2 = new PVector(v1.x, v1.y);
        v2.div(timeSpeed);
        o.add(new Ellipse(((Ellipse)objects.get(i)).getLoc().x, ((Ellipse)objects.get(i)).getLoc().y, ((Ellipse)objects.get(i)).getMass(), ((Ellipse)objects.get(i)).getRadius(), v2.x, v2.y, ((Ellipse)objects.get(i)).nameGet()));
      }

      first = false;
    } else {

      //if (checkReDisplay != location && timePassed > 3) {
      ArrayList<PVector> pl = (predictMovement(o, checking));

      for (int i = 0; i < pl.size (); i ++) {
        predictedLine.add(pl.get(i));
      }

      timePassed = 0;
      //}
      display();
      if(checkReDisplay.x != endX || checkReDisplay.y != endY){
        first = true;
      }
      checkReDisplay = new PVector(endX, endY);
    }
  }

  public void displayText() {
    Camera c = game.getCamera();
    float endX = c.getLoc().x+ mouseX/c.getZoom() - (width/2)/c.getZoom() + width/2;
    float endY = c.getLoc().y+ mouseY/c.getZoom() - (height/2)/c.getZoom() + height/2;

    PVector vel = new PVector(endX-location.x, endY - location.y);
    vel.div(c.getZoom()*(float)zoomVelConstant);

    PVector loc = new PVector((float)(location.x/metersC), (float)(location.y/metersC));
    loc.div(1000000000);
    vel.div(1000);

    String lX = nf(abs(loc.x), 1, 2);
    String lY = nf(abs(loc.y), 1, 2);
    String v = nf(vel.mag(), 1, 2);
    
    text("Location: " + lX + " Billion m" + ", " + lY + " Billion m", width/2-100, height-100);
    text("Velocity: " + v + " Thousand m/s", width/2-100, height-90);
  }

  public void display() {
    stroke(255);
    fill(0);
    beginShape();
    for (int i = 0; i < predictedLine.size (); i ++) {
      curveVertex(predictedLine.get(i).x, predictedLine.get(i).y);
    }
    endShape();
  }

  public void create() {
    Camera c = game.getCamera();
    double m = game.getCreatedMass();
    double r = game.getCreatedRadius();
    PVector cLoc = c.getLoc();

    float endX = c.getLoc().x+ mouseX/c.getZoom() - (width/2)/c.getZoom() + width/2;
    float endY = c.getLoc().y+ mouseY/c.getZoom() - (height/2)/c.getZoom() + height/2;
    PVector vel = new PVector(endX-location.x, endY - location.y);
    vel.div(c.getZoom()*(float)zoomVelConstant);

    recieve(new Ellipse((float)(location.x/metersC), (float)(location.y/metersC), m, r, vel.x, vel.y, JOptionPane.showInputDialog("ENTER NAME")));
  }
  
  public void setFirst(boolean f){
    first = f;
  }
}
class GameEngine {

  Camera camera;
  Physics physics;

  double radius;
  double mass;

  boolean planetSelector;


  static final int NUMMENUBUTTONS = 3;

  ArrayList<Create> creating;

  boolean paused = false;
  boolean drawBoxes;

  GameEngine() {
    camera = new Camera();
    physics = new Physics();
    creating = new ArrayList<Create>();

    recieve(new Ellipse(0, 0, 1.989f * pow(10, 30), 6.958f * pow(10, 9), 0, 0, "Sun"));

    solarSystemInput();

    planetSelector = false;
    drawBoxes = false;

    mass = 5.9736f * Math.pow(10, 21);
    radius = 6.371f * pow(10, 6);
  }

  public void runGame() {
    if (keyPressed && key == 'o') {
      if (planetSelector) {
        planetSelector = false;
      } else {
        planetSelector = true;
      }
      keyPressed = false;
    }
    if (!planetSelector) {
      runBackground();
      if (creating.size() != 0) {
        creating.get(0).displayText();
      }
      displayYears();
      checkReset();
      checkPause();
      checkResetCreatorVariables();
      displayPlanetData();
      text("Mass: " + mass, 0, height - 20);
      text("Radius: " + radius*2, 0, height - 10);
      camera.run();
      displayIndicators();
      checkCreate(creating);
      if (!paused) {
        runObjects();
        timePassed++;
        years ++;
      } else {
        displayObjects();
      }
    } else {
      background(255);
      displayMenu();
      checkMouse();
    }
  }

  public void displayMenu() {
    for (int i = 0; i < menuButtons.size (); i++) {
      menuButtons.get(i).draw(drawBoxes);
    }
  }

  public void checkMouse() {
    if (mousePressed && mouseButton == LEFT) {
      for (int i = 0; i < menuButtons.size (); i ++) {
        if (menuButtons.get(i).containsMouse()) {
          camera.select(i-1);
        }
      }
      mousePressed = false;
    }else if(mousePressed && mouseButton == RIGHT){
      for (int i = 1; i < menuButtons.size (); i ++) {
        if (menuButtons.get(i).containsMouse()) {
          menuButtons.remove(i);
          objects.remove(i-1);
        }
      }
    }
  }

  public void displayYears() {
    String yr = nf(years/3600, 1, 2);
    text("Years: " + yr, 0, 20);
  }

  public void checkReset() {
    if (keyPressed && key == 'r') {
      objects.clear();
      if (creating.size() != 0)
        creating.get(0).setFirst(true);
      recieve(new Ellipse(0, 0, 1.9f * pow(10, 30), 6.95f * pow(10, 9), 5, 0, "Sun"));
    }
    if (keyPressed && key == 'c') {
      creating.clear();
    }
    if (keyPressed && key == 'v') {
      zoomVelConstant = -1;
      while (zoomVelConstant < 0) {
        try {
          zoomVelConstant = (Float.parseFloat(JOptionPane.showInputDialog("x * 10 ^ z\nEnter the x for velocity to zoom Constant"))) * Math.pow(10, Double.parseDouble(JOptionPane.showInputDialog("x * 10 ^ z\nEnter the y for velocity to zoom Constant")));
        }
        catch(Exception e) {
          println("ERROR");
        }
      }
      keyPressed = false;
    }
  }

  public void runBackground() {
    background(0);
    textSize(10);
    text("FrameRate: " + frameRate, 0, 10);
  }

  public void displayObjects() {
    for (int i = 0; i < objects.size (); i ++) {
      objects.get(i).display();
    }
  }

  public void checkPause() {
    if (keyPressed && key == 'p') {
      if (paused) {
        paused = false;
        keyPressed = false;
      } else if (!paused) {
        paused = true;
        keyPressed = false;
      }
    }
  }

  public void checkResetCreatorVariables() {
    if (keyPressed && key == 's') {
      mass = -1;
      radius = -1;
      while (mass < 0 && radius < 0) {
        try {
          mass = (Double.parseDouble(JOptionPane.showInputDialog("x * 10 ^ z\nEnter the x for mass"))) * Math.pow(10, Double.parseDouble(JOptionPane.showInputDialog("x * 10 ^ z\nEnter the y for mass")));
          radius = (Double.parseDouble(JOptionPane.showInputDialog("x * 10 ^ z\nEnter the x for radius"))) * Math.pow(10, Double.parseDouble(JOptionPane.showInputDialog("x * 10 ^ z\nEnter the y for radius")));
        }
        catch(Exception e) {
          println("ERROR" + e);
        }
      }
      keyPressed = false;
    }
  }

  public void runObjects() {
    for (int i = 0; i < objects.size (); i ++) {
      objects.get(i).applyForce(physics.getAcc(objects.get(i), objects));
      objects.get(i).run();
    }
  }

  public Camera getCamera() {
    return camera;
  }

  public double getCreatedMass() {
    return mass;
  }

  public double getCreatedRadius() {
    return radius;
  }

  public Physics getPhysics() {
    return physics;
  }
}
class Object {

  Object() {
  }

  public void run() {
    update();
    display();
  }

  public void display() {
  }

  public void update() {
  }

  public void applyForce(PVector f) {
  }
}

class Ellipse extends Object {

  PVector location;
  PVector velocity;
  PVector acceleration;

  double mass;
  double diameter;

  String name;

  Ellipse(float x, float y, double m, double r, float xV, float yV, String n) {
    location = new PVector(x, y);
    velocity = new PVector(xV, yV);
    acceleration = new PVector(0, 0);

    velocity.mult(timeSpeed);
    mass = m;
    diameter = 2 * r;

    name = n;
  }

  public void display() {
    if (name.equals("Sun")) {
      fill(255,220,0);
      stroke(255,220,0);
      ellipse((float)(location.x*metersC), (float)(location.y*metersC), (float)(diameter*metersC), (float)(diameter*metersC));
    } else {
      fill(170,170,255);
      stroke(170,170,255);
      ellipse((float)(location.x*metersC), (float)(location.y*metersC), (float)(diameter*metersC)*planetMult, (float)(diameter*metersC)*planetMult);
    }
    fill(255);
  }

  public void update() {
    velocity.add(acceleration);
    location.add(velocity);

    acceleration.mult(0);
  }

  public void applyForce(PVector f) {
    f.div((float)mass);
    f.mult(pow(timeSpeed, 2));
    acceleration.add(f);
  }
  
  public double getDiameter(){
    return diameter;
  }

  public PVector getLoc() {
    return location;
  }

  public PVector getVel() {
    return velocity;
  }

  public PVector getAcc() {
    return acceleration;
  }

  public double getMass() {
    return mass;
  }

  public double getRadius() {
    return diameter/2;
  }

  public void changeVel(PVector vel) {

    velocity = vel;
    velocity.mult(timeSpeed);
  }

  public String nameGet() {
    return name;
  }
}
class Physics{
  
  PVector location, velocity, acceleration;
  double mass;
  
  double G;
  
  Physics(){
    setConstants();
  }
  
  public PVector getAcc(Object o, ArrayList<Object> oCheck){
    return getGravitationalForce(o, oCheck);
  }
  
  public PVector getGravitationalForce(Object o, ArrayList<Object> oCheck){
    PVector force = new PVector (0,0);
    PVector forceTot = new PVector(0,0);
    for(int i = 0; i < oCheck.size(); i ++){
      PVector oCheckLoc = ((Ellipse)oCheck.get(i)).getLoc();
      if(((Ellipse)o).getLoc() != oCheckLoc){
      force = PVector.sub(oCheckLoc, ((Ellipse)o).getLoc());
      double distance = force.mag();
      force.normalize();
      
      float strength = (float)((G * ((Ellipse)o).getMass() * ((Ellipse)oCheck.get(i)).getMass()) / Math.pow(distance,2));
      force.mult(strength);
      forceTot.add(force);
      }
    }
    return forceTot;
  }
  
  public void setConstants(){
    G = 6.67384f * pow(10, -11);
  }
}
public PVector centripCalc(Object o, Object o2){
  
  double mass = ((Ellipse)o).getMass();
  double mass2 = ((Ellipse)o2).getMass();
  
  PVector loc = ((Ellipse)o).getLoc();
  PVector loc2 = ((Ellipse)o2).getLoc();
  
  double G = 6.67384f * pow(10, -11);
  
  float centripV = sqrt((float)(G * mass2) / loc.mag());
  
  PVector velocity = new PVector(0, centripV);
  return velocity;
}

public PVector centripCalcO(Object o, Object o2){
  
  double mass = ((Ellipse)o).getMass();
  double mass2 = ((Ellipse)o2).getMass();
  
  PVector loc = ((Ellipse)o).getLoc();
  PVector loc2 = ((Ellipse)o2).getLoc();
  
  double G = 6.67384f * pow(10, -11);
  
  float centripV = sqrt((float)(G * mass2) / loc.mag());
  
  PVector velocity = new PVector(0, -centripV);
  return velocity;
}
public void checkCreate(ArrayList<Create> c) {
  Camera cam = game.getCamera();
  
  if (mousePressed && c.size() == 0 && mouseButton == RIGHT) {
    //c.add(new Create(mouseX, mouseY));
    c.add(new Create(cam.getLoc().x+ mouseX/cam.getZoom() - (width/2)/cam.getZoom() + width/2,  cam.getLoc().y+ mouseY/cam.getZoom() - (height/2)/cam.getZoom() + height/2));
    mousePressed = false;
  }
  else if (mousePressed && c.size() != 0 && mouseButton == RIGHT) {
    c.get(0).create();
    
    c.clear();
    mousePressed = false;
  }


  if (c.size() != 0) {
    c.get(0).displayCurrentLine();
  }
}
public void displayIndicators() {

  for (int i = 1; i < objects.size(); i ++) {
    Ellipse e = (Ellipse)objects.get(i);
    Camera c = game.getCamera();
    textSize(175);
    text(e.nameGet(), (float)(e.getLoc().x*metersC)-300,(float)(e.getLoc().y*metersC - ((((e.getDiameter()*metersC*planetMult))/2)+500)));
  }
}
public void displayPlanetData() {

  float xCounter = 0;
  float yCounter = 50;
  float l = 200;

  for (int i = 0; i <  objects.size (); i ++) {
    Ellipse e = (Ellipse)objects.get(i);
    text("Name:" + e.nameGet(), xCounter, yCounter+5);
    text("Position X: " + e.getLoc().x, xCounter, yCounter + 20);
    text("Position Y: " + e.getLoc().y, xCounter, yCounter + 30);
    text("Speed X: " + nf(e.getVel().x/timeSpeed/1000, 0, 3) + "Thousand m/s", xCounter, yCounter + 40);
    text("Speed Y: " + nf(e.getVel().y/timeSpeed/1000,0,3) + "Thousand m/s", xCounter, yCounter + 50);
    text("Radius: " + e.getDiameter()/2, xCounter, yCounter + 60);
    text("Mass: " + e.getMass(), xCounter, yCounter + 70);
    xCounter+= l;
    if (xCounter >= width-l) {
      xCounter = 0;
      yCounter += 95;
    }
  }
}
public ArrayList<PVector> predictMovement(ArrayList<Object> o, ArrayList<Object> c) {

  ArrayList<PVector> predictedLine2 = new ArrayList<PVector>();
  
  for (int i = 0; i < 30; i ++) {

    c.get(0).applyForce(game.getPhysics().getAcc(c.get(0), o));
    c.get(0).update();
    //println(game.getPhysics().getAcc(c, o));
    stroke(255);
    fill(0);
    predictedLine2.add(new PVector((float)(((Ellipse)c.get(0)).getLoc().x*metersC), (float)(((Ellipse)c.get(0)).getLoc().y*metersC)));
    println(o.size());
    //println(((Ellipse)checking.get(0)).getLoc());

    for (int z = 0; z < o.size (); z ++) {

      o.get(z).applyForce(game.getPhysics().getAcc(o.get(z), o));
      o.get(z).applyForce(game.getPhysics().getAcc(o.get(z), c));
      o.get(z).update();
    }
    fill(0);
  }
  
  return predictedLine2;
}
public void solarSystemInput(){
  
  recieve(new Ellipse(7.791f * pow(10, 10), 0, 328.5f * pow(10, 21), 2.44f * pow(10, 6), 0, 0, "Mercury"));
  recieve(new Ellipse(1.08f * pow(10,11), 0, 4.86f * pow(10,24), 6.05f * pow(10,6), 0, 0, "Venus")); //<>//
  recieve(new Ellipse(1.496f * pow(10,11), 0, 5.972f * pow(10,24), 6.371f * pow(10, 6), 0, 0, "Earth"));
  recieve(new Ellipse(2.279f * pow(10,11), 0, 6.39f * pow(10,23), 3.39f * pow(10,6), 0, 0, "Mars"));
  recieve(new Ellipse(7.785f * pow(10,11), 0, 1.89f * pow(10, 27), 6.99f * pow(10, 7), 0, 0, "Jupiter"));
  recieve(new Ellipse(1.43f * pow(10, 12), 0, 5.68f *pow(10,26), 5.82f * pow(10,7), 0, 0, "Saturn"));
  recieve(new Ellipse(2.87f * pow(10, 12), 0, 8.6f * pow(10,25), 2.53f * pow(10,7), 0, 0, "Uranus"));
  recieve(new Ellipse(4.5f * pow(10, 12), 0, 1.02f * pow(10,26), 2.53f * pow(10, 7), 0, 0, "Neptune"));
  recieve(new Ellipse(5.9f * pow(10, 12), 0 ,1.3f * pow(10, 22), 1.184f * pow(10, 6), 0, 0, "Pluto"));
  
  for(int i = 1; i < objects.size(); i ++){
    
    if(i%2 == 0){
    ((Ellipse)objects.get(i)).changeVel(centripCalc(objects.get(i), objects.get(0)));
    }else{
      ((Ellipse)objects.get(i)).changeVel(centripCalcO(objects.get(i), objects.get(0)));
    }
  }
  
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "Hierarchy_Test" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
