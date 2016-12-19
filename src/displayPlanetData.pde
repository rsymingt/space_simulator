void displayPlanetData() {

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
