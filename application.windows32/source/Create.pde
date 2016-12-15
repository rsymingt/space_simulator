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

  void displayCurrentLine() {
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

  void displayText() {
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

  void display() {
    stroke(255);
    fill(0);
    beginShape();
    for (int i = 0; i < predictedLine.size (); i ++) {
      curveVertex(predictedLine.get(i).x, predictedLine.get(i).y);
    }
    endShape();
  }

  void create() {
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
  
  void setFirst(boolean f){
    first = f;
  }
}
