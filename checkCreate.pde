void checkCreate(ArrayList<Create> c) {
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
