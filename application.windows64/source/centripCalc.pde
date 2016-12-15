PVector centripCalc(Object o, Object o2){
  
  double mass = ((Ellipse)o).getMass();
  double mass2 = ((Ellipse)o2).getMass();
  
  PVector loc = ((Ellipse)o).getLoc();
  PVector loc2 = ((Ellipse)o2).getLoc();
  
  double G = 6.67384 * pow(10, -11);
  
  float centripV = sqrt((float)(G * mass2) / loc.mag());
  
  PVector velocity = new PVector(0, centripV);
  return velocity;
}

PVector centripCalcO(Object o, Object o2){
  
  double mass = ((Ellipse)o).getMass();
  double mass2 = ((Ellipse)o2).getMass();
  
  PVector loc = ((Ellipse)o).getLoc();
  PVector loc2 = ((Ellipse)o2).getLoc();
  
  double G = 6.67384 * pow(10, -11);
  
  float centripV = sqrt((float)(G * mass2) / loc.mag());
  
  PVector velocity = new PVector(0, -centripV);
  return velocity;
}
