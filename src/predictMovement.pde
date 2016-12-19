ArrayList<PVector> predictMovement(ArrayList<Object> o, ArrayList<Object> c) {

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
