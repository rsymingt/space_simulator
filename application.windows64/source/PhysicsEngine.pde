class Physics{
  
  PVector location, velocity, acceleration;
  double mass;
  
  double G;
  
  Physics(){
    setConstants();
  }
  
  PVector getAcc(Object o, ArrayList<Object> oCheck){
    return getGravitationalForce(o, oCheck);
  }
  
  PVector getGravitationalForce(Object o, ArrayList<Object> oCheck){
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
  
  void setConstants(){
    G = 6.67384 * pow(10, -11);
  }
}
