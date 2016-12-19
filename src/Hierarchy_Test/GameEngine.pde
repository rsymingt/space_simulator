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

    recieve(new Ellipse(0, 0, 1.989 * pow(10, 30), 6.958 * pow(10, 9), 0, 0, "Sun"));

    solarSystemInput();

    planetSelector = false;
    drawBoxes = false;

    mass = 5.9736 * Math.pow(10, 21);
    radius = 6.371 * pow(10, 6);
  }
  
  void mouseWheel(MouseEvent event) {
    camera.mouseWheel(event.getCount());
  }

  void runGame() {
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

  void displayMenu() {
    for (int i = 0; i < menuButtons.size (); i++) {
      menuButtons.get(i).draw(drawBoxes);
    }
  }

  void checkMouse() {
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

  void displayYears() {
    String yr = nf(years/3600, 1, 2);
    text("Years: " + yr, 0, 20);
  }

  void checkReset() {
    //RESETS
    if (keyPressed && key == 'r') {
      objects.clear();
      if (creating.size() != 0)
        creating.get(0).setFirst(true);
      recieve(new Ellipse(0, 0, 1.9 * pow(10, 30), 6.95 * pow(10, 9), 5, 0, "Sun"));
    }
    //CLEARS CREATION
    if (keyPressed && key == 'c') {
      creating.clear();
    }
    //CHANGES VELOCITY STUFF
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

  void runBackground() {
    background(0);
    textSize(10);
    text("FrameRate: " + frameRate, 0, 10);
  }

  void displayObjects() {
    for (int i = 0; i < objects.size (); i ++) {
      objects.get(i).display();
    }
  }

  void checkPause() {
    //PAUSES GAME
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

  void checkResetCreatorVariables() {
    
    //RESETS CREATOR VARS
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

  void runObjects() {
    for (int i = 0; i < objects.size (); i ++) {
      objects.get(i).applyForce(physics.getAcc(objects.get(i), objects));
      objects.get(i).run();
    }
  }

  Camera getCamera() {
    return camera;
  }

  double getCreatedMass() {
    return mass;
  }

  double getCreatedRadius() {
    return radius;
  }

  Physics getPhysics() {
    return physics;
  }
}