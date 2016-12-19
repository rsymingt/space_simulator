void displayIndicators() {

  for (int i = 1; i < objects.size(); i ++) {
    Ellipse e = (Ellipse)objects.get(i);
    Camera c = game.getCamera();
    textSize(175);
    text(e.nameGet(), (float)(e.getLoc().x*metersC)-300,(float)(e.getLoc().y*metersC - ((((e.getDiameter()*metersC*planetMult))/2)+500)));
  }
}
