class Object {

  Object() {
  }

  void run() {
    update();
    display();
  }

  void display() {
  }

  void update() {
  }

  void applyForce(PVector f) {
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

  void display() {
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

  void update() {
    velocity.add(acceleration);
    location.add(velocity);

    acceleration.mult(0);
  }

  void applyForce(PVector f) {
    f.div((float)mass);
    f.mult(pow(timeSpeed, 2));
    acceleration.add(f);
  }
  
  double getDiameter(){
    return diameter;
  }

  PVector getLoc() {
    return location;
  }

  PVector getVel() {
    return velocity;
  }

  PVector getAcc() {
    return acceleration;
  }

  double getMass() {
    return mass;
  }

  double getRadius() {
    return diameter/2;
  }

  void changeVel(PVector vel) {

    velocity = vel;
    velocity.mult(timeSpeed);
  }

  String nameGet() {
    return name;
  }
}
