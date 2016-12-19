class Camera {

  PVector location, locationB;
  float zoom;
  boolean calculating;
  int selected;

  Camera() {
    location = new PVector(-width/2, -height/2);
    locationB = new PVector(0, 0);
    
    selected = -1;

    zoom = 0.05;
    calculating = false;

    addMouseWheelListener(new java.awt.event.MouseWheelListener() { 
      public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) { 
        mouseWheel(evt.getWheelRotation());
      }
    }
    );
  }

  void run() {
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

  void checkMouseScroll() {
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
  void mouseWheel(int delta) {
    if (delta < 0) {
      zoom *= 1.2;
    } else if (delta > 0) {
      zoom *= 0.8;
    }
  }

  float getZoom() {
    return zoom;
  }

  PVector getLoc() {
    return location;
  }
  
  void select(int e){
    selected = e;
  }
}
